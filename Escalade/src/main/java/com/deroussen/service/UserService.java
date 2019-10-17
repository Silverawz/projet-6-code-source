package com.deroussen.service;

import com.deroussen.entities.User;

public interface UserService {
	public User findUserByEmail(String email);
	public User findByUserId(Long id);
	public void saveUser(User user);
	
}
