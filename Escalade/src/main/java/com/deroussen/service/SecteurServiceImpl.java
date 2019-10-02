package com.deroussen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deroussen.dao.SecteurRepository;
import com.deroussen.entities.Secteur;

@Service("secteurService")
public class SecteurServiceImpl implements SecteurService {

	@Autowired
	private SecteurRepository secteurRepository;
	
	@Override
	public Secteur findSecteurByName(String name) {
		return secteurRepository.findBySecteurname(name);
	}

	@Override
	public void saveSecteur(Secteur secteur) {
		secteur.setSecteurname(secteur.getSecteurname());
		secteurRepository.save(secteur);
	}

}
