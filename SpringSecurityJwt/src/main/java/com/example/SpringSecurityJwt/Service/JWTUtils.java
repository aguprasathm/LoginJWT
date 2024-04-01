
package com.example.SpringSecurityJwt.Service;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.Base64;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.SpringSecurityJwt.Dto.ReqRes;
import com.example.SpringSecurityJwt.Entity.OurUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JWTUtils {

	private SecretKey key;
	
	private static final long EXPIRATION_TIME=86400000; //24hr or 86400000 milisecs
	
	public JWTUtils() {
		String secreatString="567890343556679889fggjgnliyresf5689008ijtgr";
		byte[] keyBytes=Base64.getDecoder().decode(secreatString.getBytes(StandardCharsets.UTF_8));
		this.key=new SecretKeySpec(keyBytes,"HmacSHA256");
				
	}
	
	public String generateToken(UserDetails userDetails) {
	    String name = userDetails.getUsername(); // Get the name
	    String role = userDetails.getAuthorities().stream()
	                        .map(GrantedAuthority::getAuthority)
	                        .findFirst()
	                        .orElse("ROLE_USER"); // Assuming a default role
	    
	    return Jwts.builder()
	            .claim("name", name)
	            .claim("role", role)
	            .setIssuedAt(new Date(System.currentTimeMillis()))
	            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
	            .signWith(key)
	            .compact();
	}


	
	public String generateRefreshToken(HashMap<String,Object> claims,UserDetails userDetails) {
		return Jwts.builder()
				.claims(claims)
				.subject(userDetails.getUsername())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
				.signWith(key)
				.compact();
	}
	
	public String extractUsername(String token) {
		return extractClaims(token,Claims::getSubject);
	}


	private <T> T extractClaims(String token, Function<Claims,T> claimsFunction) {

		return claimsFunction.apply(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload());
	}
	
	public boolean isTokenValid(String token,UserDetails userDetails) {
		final String username=extractUsername(token);
		return (username.equals(userDetails.getUsername())&&!isTokenExpired(token));
	}


	public boolean isTokenExpired(String token) {
		// TODO Auto-generated method stub
		return extractClaims(token,Claims::getExpiration).before(new Date(0));
	}
	
}