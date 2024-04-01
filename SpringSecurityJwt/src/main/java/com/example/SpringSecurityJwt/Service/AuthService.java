package com.example.SpringSecurityJwt.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.SpringSecurityJwt.Dto.ReqRes;
import com.example.SpringSecurityJwt.Entity.OurUser;

import com.example.SpringSecurityJwt.Repository.OurUserRepo;



@Service
public class AuthService {
	@Autowired
	private OurUserRepo ouruserRepo;
	@Autowired
	private JWTUtils jWTUtils;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationManager authenticationManager;


	
	public ReqRes signUp(ReqRes registrationRequest) {
	    ReqRes resp = new ReqRes();
	   
	        OurUser ourUser = new OurUser();
	        
	        ourUser.setName(registrationRequest.getName());
	        ourUser.setEmail(registrationRequest.getEmail());
	        ourUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
	        var roles = ourUser.getRole();
	        
	        // Check if role is provided, otherwise set a default role
	        String role = registrationRequest.getRole();
	        if (role == null || role.isEmpty()) {
	            // Set default role
	            role = "USER"; // You can set any default role here
	        }
	        ourUser.setRole(role);
	        
	        OurUser ourUserResult = ouruserRepo.save(ourUser);
	        if (ourUserResult != null && ourUserResult.getId() > 0) {
	            resp.setOurUser(ourUserResult);
	            resp.setMessage("User saved successfully");
	            resp.setStatusCode(200);
	            resp.setRole(role); 
	        }
	  
	    return resp;
	}

	
	
	
	

//	 public ReqRes signIn(ReqRes signinRequest) {
//	        ReqRes response = new ReqRes();
//	        System.out.println("hi");
//	        try {
//	        	System.out.println("hi");
//	            authenticationManager.authenticate(
//	                    new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword())
//	            );
//
//	            UserDetails userDetails = loadUserByUsername(signinRequest.getEmail()); // Load UserDetails
//	            var jwt = jWTUtils.generateToken(userDetails);
//	            var refreshToken = jWTUtils.generateRefreshToken(new HashMap<>(), userDetails);
//	            var role = userDetails.getAuthorities().iterator().next().getAuthority(); // Extract role
//
//	            response.setStatusCode(200);
//	            response.setToken(jwt);
//	            response.setRefreshToken(refreshToken);
//	            response.setExpirationTime("24Hr");
//	            response.setMessage("Successfully signed In");
//	            response.setRole(role);
//	        } catch (AuthenticationException e) {
//	            // Handle authentication failure
//	            response.setStatusCode(401);
//	            response.setMessage("Authentication failed");
//	        }
//
//	        return response;
//	    }
	
	
	 public ReqRes signIn(ReqRes signinRequest){
	        ReqRes response = new ReqRes();

	        try {
	            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(),signinRequest.getPassword()));
	            var user = ouruserRepo.findByEmail(signinRequest.getEmail()).orElseThrow();
	            System.out.println("USER IS: "+ user);
	            var jwt = jWTUtils.generateToken(user);
	            var refreshToken = jWTUtils.generateRefreshToken(new HashMap<>(), user);
	            response.setStatusCode(200);
	            response.setToken(jwt);
	            response.setRefreshToken(refreshToken);
	            response.setExpirationTime("24Hr");
	            response.setMessage("Successfully Signed In");
	        }catch (Exception e){
	            response.setStatusCode(500);
	            response.setError(e.getMessage());
	        }
	        return response;
	    }

	 
	 // Custom method to load UserDetails from repository
	    private UserDetails loadUserByUsername(String email) {
	    	System.out.println(email);
	        OurUser user = ouruserRepo.findByEmail(email)
	                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
	        return new org.springframework.security.core.userdetails.User(
	                user.getEmail(), user.getPassword(), user.getAuthorities()
	        );
	    }
	
	public ReqRes refreshToken(ReqRes refreshTokenrequest) {
		ReqRes response=new ReqRes();
		String ourEmail=jWTUtils.extractUsername(refreshTokenrequest.getToken());
		OurUser users=ouruserRepo.findByEmail(ourEmail).orElseThrow();
		if(jWTUtils.isTokenValid(refreshTokenrequest.getToken(), users)) {
			var jwt=jWTUtils.generateToken(users);
			response.setStatusCode(200);
			response.setToken(jwt);
			response.setRefreshToken(refreshTokenrequest.getToken());
			response.setExpirationTime("24Hr");
			response.setMessage("Successfully signed In");
		}
		response.setStatusCode(500);
		return response;
	}
	
	
	////////////////////////////////////////////

	
}
