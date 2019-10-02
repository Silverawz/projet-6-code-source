package com.deroussen.service;

import com.deroussen.entities.Secteur;

public interface SecteurService {

	public Secteur findSecteurByName(String name);	
	public void saveSecteur(Secteur secteur);
}
