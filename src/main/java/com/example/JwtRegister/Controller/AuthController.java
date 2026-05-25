package com.example.JwtRegister.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.JwtRegister.Dto.UserDto;
import com.example.JwtRegister.Service.AuthService;



@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@PostMapping("/signup")
	public ResponseEntity<UserDto> signUp(@RequestBody UserDto signUpRequest){
		return ResponseEntity.ok(authService.signUp(signUpRequest));
	} 

	@PostMapping("/signin")
	public ResponseEntity<UserDto> signIn(@RequestBody UserDto signInRequest){
		return ResponseEntity.ok(authService.signIn(signInRequest));
	}
	
	@PostMapping("/verify-account")
	public ResponseEntity<String> verifyAccount(@RequestParam String email,@RequestParam String otp){
		return new ResponseEntity<>(authService.verifyAccount(email,otp),HttpStatus.OK);	
	}
	
	@GetMapping("/forgot-password")
	public ResponseEntity<String> forgotPassword(@RequestParam String email){
		return new ResponseEntity<>(authService.forgotPassword(email),HttpStatus.OK);
	}
	
	@PostMapping("/set-password")
	public ResponseEntity<String> setPassword(@RequestParam String email,@RequestHeader String newPassword){
		return new ResponseEntity<>(authService.setPassword(email,newPassword),HttpStatus.OK);	
	}
	
	@PutMapping("/regenerate-otp")
	public ResponseEntity<String> regenerateOtp(@RequestParam String email){
		return new ResponseEntity<>(authService.regenerateOtp(email),HttpStatus.OK);
	}

}
