package com.deroussen.service;

import java.util.List;

import com.deroussen.entities.Longueur;
public interface LongueurService {

	public Longueur findLongueurByName(String name);	
	public void saveLongueur(Longueur longueur);
	public List<Longueur> findByVoieId(Long id);
}
