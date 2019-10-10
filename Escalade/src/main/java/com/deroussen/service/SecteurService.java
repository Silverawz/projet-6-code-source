package com.deroussen.service;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.deroussen.entities.Secteur;


public interface SecteurService {
	public Secteur findById(Long id);
	public Secteur findBySecteurname(String name);
	public void saveSecteur(Secteur secteur);
	public List<Secteur> findBySpotId(Long id);
	public Page<Secteur> findBySecteurnameContainsFromSpotId(Long id, String mc, Pageable page);
}
