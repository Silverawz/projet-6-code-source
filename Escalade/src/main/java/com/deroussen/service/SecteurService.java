package com.deroussen.service;


import java.util.List;

import com.deroussen.entities.Secteur;


public interface SecteurService {
	public Secteur findById(Long id);
	public Secteur findBySecteurname(String name);
	public void saveSecteur(Secteur secteur);
	public List<Secteur> findBySpotId(Long id);
	
}
