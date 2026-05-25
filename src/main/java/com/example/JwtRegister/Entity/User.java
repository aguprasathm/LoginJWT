package com.example.JwtRegister.Entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String fullName;
	private String email;
	private String password;
	private long phoneNumber;
	private String role;
	private boolean verfied;
	private String otp;
	private LocalDateTime otpGeneratedtime;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	
		return List.of(new SimpleGrantedAuthority(role));
	}
	@Override
	public String getPassword() {
		
		return password;
	}
	@Override
	public String getUsername() {
	
		return fullName;
	}
	@Override
	public boolean isAccountNonExpired() {

		return true;
	}
	@Override
	public boolean isAccountNonLocked() {

		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		
		return true;
	}
	@Override
	public boolean isEnabled() {

		return true;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public long getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	public boolean isVerfied() {
		return verfied;
	}
	public void setVerfied(boolean verfied) {
		this.verfied = verfied;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public LocalDateTime getOtpGeneratedtime() {
		return otpGeneratedtime;
	}
	public void setOtpGeneratedtime(LocalDateTime otpGeneratedtime) {
		this.otpGeneratedtime = otpGeneratedtime;
	}
	
	public User(int id, String fullName, String email, String password, long phoneNumber, String role, boolean verfied,
			String otp, LocalDateTime otpGeneratedtime) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.role = role;
		this.verfied = verfied;
		this.otp = otp;
		this.otpGeneratedtime = otpGeneratedtime;
	}
	public User() {
		super();
	}
	
	
	
}
