package com.deroussen.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.deroussen.entities.Role;

@Repository("roleRepository")
public interface RoleRepository extends JpaRepository <Role, Long> {
	Role findByRole(String role);
}
