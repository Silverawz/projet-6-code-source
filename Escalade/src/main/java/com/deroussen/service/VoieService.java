package com.deroussen.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.deroussen.entities.Voie;

public interface VoieService {
	public Voie findById(Long id);	
	public void saveVoie(Voie voie);
	public List<Voie> findBySecteurId(Long id);
	public Page<Voie> findByVoieContainsFromSecteurId(Long id, String mc, Pageable page);
	public Voie findByVoie_name(String name);
}
