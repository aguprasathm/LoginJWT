package com.example.SpringSecurityJwt.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.SpringSecurityJwt.Repository.OurUserRepo;

@Service
public class OurUserDetailsService implements UserDetailsService{

	@Autowired
	private OurUserRepo ourUserRepo;
	
	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return ourUserRepo.findByEmail(username).orElseThrow();
    }

}
