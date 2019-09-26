package com.deroussen.service;

import com.deroussen.entities.User;

public interface UserService {
	public User findUserByEmail(String email);
	
	public void saveUser(User user);
	
}
