package com.deroussen.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.deroussen.entities.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository <User, Long>{
	User findByEmail(String email);
	
	@Query("from User where id=?1")
	User findByUserId(Long id);
	
}
