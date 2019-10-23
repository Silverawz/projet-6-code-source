package com.deroussen.service;

import java.util.List;

import com.deroussen.entities.User;

public interface UserService {
	public User findUserByEmail(String email);
	public User findByUserId(Long id);
	public void saveUser(User user);
	public List<Long> findReservationUserIdWithTopoId(Long topoId);
}
