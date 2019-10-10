package com.deroussen.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.deroussen.entities.Voie;

public interface VoieService {
	public Voie findById(Long id);
	public Voie findVoieByName(String name);	
	public void saveVoie(Voie voie);
	public List<Voie> findBySecteurId(Long id);
	public Page<Voie> findByVoienameContainsFromSecteurId(Long id, String mc, Pageable page);
}
