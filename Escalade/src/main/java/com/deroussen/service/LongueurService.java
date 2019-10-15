package com.deroussen.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.deroussen.entities.Longueur;
public interface LongueurService {

	
	public List<Longueur> findByVoieId(Long id);
	void saveLongueur(Longueur longueur);
	public Page<Longueur> findByLongueurContainsFromVoieId(Long id, String mc, Pageable page);
	public Longueur findByid(Long id);
	
}
