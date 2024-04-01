package com.example.SpringSecurityJwt.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SpringSecurityJwt.Entity.OurUser;

public interface OurUserRepo extends JpaRepository<OurUser,Long>{

	Optional<OurUser> findByEmail(String email);

}
