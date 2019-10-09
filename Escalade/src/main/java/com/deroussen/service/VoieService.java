package com.deroussen.service;

import java.util.List;

import com.deroussen.entities.Voie;

public interface VoieService {
	public Voie findById(Long id);
	public Voie findVoieByName(String name);	
	public void saveVoie(Voie voie);
	public List<Voie> findBySecteurId(Long id);
}
