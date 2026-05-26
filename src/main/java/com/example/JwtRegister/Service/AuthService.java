package com.example.JwtRegister.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.JwtRegister.Dto.UserDto;
import com.example.JwtRegister.Entity.User;
import com.example.JwtRegister.Repository.UserRepository;


import jakarta.mail.MessagingException;


@Service
public class AuthService {

	@Autowired
	private UserRepository ouruserRepo;
	@Autowired
	private JWTUtils jWTUtils;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private EmailUtil emailUtil;
	@Autowired
	private OtpUtil otpUtil;
	
	
	//signUp
	
	public UserDto signUp(UserDto registrationRequest) {
		
		
		String otp=otpUtil.generateOtp();
		try {
			emailUtil.sendOtpEmail(registrationRequest.getEmail(), otp);
		} catch (MessagingException e) {
			throw new RuntimeException("unable to send otp please try again");
		}
		
		
		UserDto resp = new UserDto();
	   
	        User ourUser = new User();

	        ourUser.setFullName(registrationRequest.getFullName());
	        ourUser.setEmail(registrationRequest.getEmail());
	        ourUser.setPhoneNumber(registrationRequest.getPhoneNumber());
	        ourUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
	        ourUser.setOtp(otp);
	        ourUser.setOtpGeneratedtime(LocalDateTime.now());
	        var roles = ourUser.getRole();
	        
	        // Check if role is provided, otherwise set a default role
	        String role = registrationRequest.getRole();
	        if (role == null || role.isEmpty()) {
	            // Set default role
	            role = "USER"; // You can set any default role here
	        }
	        ourUser.setRole(role);
	        
	        User ourUserResult = ouruserRepo.save(ourUser);
	        
	        if (ourUserResult != null && ourUserResult.getId() > 0) {
	            resp.setUser(ourUserResult);
	            resp.setMessage("User saved successfully");
	            resp.setStatusCode(200);
	            resp.setRole(role); 
	        }
	  
	    return resp;
	}
	
	//verifyAccount
	
	public String verifyAccount(String email, String otp) {
		Optional<User> optionalUser = ouruserRepo.findByEmail(email);

		if (optionalUser.isEmpty()) {
			return "User not found with this email: " + email;
		}

		User user = optionalUser.get();

	    if (otp != null && otp.trim().equals(user.getOtp()) && Duration.between(user.getOtpGeneratedtime(),
	            LocalDateTime.now()).getSeconds() < (5 * 60)) {
	        user.setVerfied(true);
	        ouruserRepo.save(user);
	        return "OTP verified. You can log in.";
	    }
	    return "Please verify your OTP and try again.";
	}
	
	


	

	// Custom method to load UserDetails from repository
    private UserDetails loadUserByUsername(String email) {
        User user = ouruserRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), user.getAuthorities()
        );
    }
	
    
    //SignIn
    
    public UserDto signIn(UserDto signinRequest) {
        UserDto response = new UserDto();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword())
            );

            UserDetails userDetails = loadUserByUsername(signinRequest.getEmail());
            User user = ouruserRepo.findByEmail(signinRequest.getEmail())
                                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + signinRequest.getEmail()));
            if (!user.isVerfied()) {
                response.setStatusCode(401);
                response.setMessage("Your account is not verified");
                return response;
            }

            var jwt = jWTUtils.generateToken(userDetails);
            var refreshToken = jWTUtils.generateRefreshToken(new HashMap<>(), userDetails);
            var role = userDetails.getAuthorities().iterator().next().getAuthority(); 

            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully signed In");
            response.setFullName(user.getFullName());
            response.setRole(role);
            response.setVerfied(true);
        } catch (AuthenticationException e) {
            response.setStatusCode(401);
            response.setMessage("Authentication failed");
        }

        return response;
    }
    
    //Forgot Password
    
	public String forgotPassword(String email) {
		User user=ouruserRepo.findByEmail(email).
				orElseThrow(()->new RuntimeException("User not found with this email:"+email));
		try {
			emailUtil.sendsetpasswordEmail(email);
		} catch (MessagingException e) {
			throw new RuntimeException("unable to send set password email please try again");
		}
		return "Please check your email to set new password to your account";
	}
	 
	//SetNew Password
	
	  public String setPassword(String email, String newPassword) {
	        // Validate newPassword (example: minimum length of 8 characters)
//	        if (newPassword.length() < 8) {
//	            throw new IllegalArgumentException("Password must be at least 8 characters long.");
//	        }

	        // Find user by email
	        User user = ouruserRepo.findByEmail(email)
	                                .orElseThrow(() -> new RuntimeException("User not found with this email:" + email));

	        // Hash the newPassword
	        String hashedPassword = passwordEncoder.encode(newPassword);

	        // Update user's password and save to database
	        user.setPassword(hashedPassword);
	        ouruserRepo.save(user);

	        return "New password set successfully. Please login with your new password.";
	    }
	  
	  //Regenerate OTP
	  
	  public String regenerateOtp(String email) {
			User user=ouruserRepo.findByEmail(email).
					orElseThrow(()->new RuntimeException("User not found with this email:"+email));
			String otp=otpUtil.generateOtp();	
			try {
				emailUtil.sendOtpEmail(email, otp);
			} catch (MessagingException e) {
				throw new RuntimeException("unable to send otp please try again");
			}	
			user.setOtp(otp);
			user.setOtpGeneratedtime(LocalDateTime.now());
			ouruserRepo.save(user);
			return "Email sent.. please verify account within 1 minute";
		}
	  
	  
	  
}
