package com.example.SpringSecurityJwt.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.SpringSecurityJwt.Dto.ReqRes;
import com.example.SpringSecurityJwt.Entity.OurUser;

import com.example.SpringSecurityJwt.Service.AuthService;
import com.example.SpringSecurityJwt.Service.JWTUtils;



@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

	@Autowired
	private AuthService authService;
	
	 private final JWTUtils jwtUtils;

	    public AuthController(AuthService authService, JWTUtils jwtUtils) {
	        this.authService = authService;
	        this.jwtUtils = jwtUtils;
	    }
//	
//	@Autowired
//	private JavaMailSender javaMailSender;
//	
//	 @Value("${app.base.url}")
//	  private String baseUrl;
	
	@PostMapping("/signup")
	public ResponseEntity<ReqRes> signUp(@RequestBody ReqRes signUpRequest){
		return ResponseEntity.ok(authService.signUp(signUpRequest));
	}
	
	@PostMapping("/signin")
	public ResponseEntity<ReqRes> signIn(@RequestBody ReqRes signInRequest){
		return ResponseEntity.ok(authService.signIn(signInRequest));
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes refreshTokenRequest){
		return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
	}
	

    
    
    
}