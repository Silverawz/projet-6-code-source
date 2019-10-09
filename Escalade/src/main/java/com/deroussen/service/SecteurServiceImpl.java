package com.deroussen.service;



import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deroussen.dao.SecteurRepository;
import com.deroussen.entities.Secteur;

@Service("secteurService")
public class SecteurServiceImpl implements SecteurService {

	@Autowired
	private SecteurRepository secteurRepository;



	@Override
	public void saveSecteur(Secteur secteur) {
		secteurRepository.save(secteur);
	}



	@Override
	public Secteur findBySecteurname(String name) {
		return secteurRepository.findBySecteurname(name);
	}



	@Override
	public List<Secteur> findBySpotId(Long id) {
		List <Secteur> secteurs = secteurRepository.findBySpotId(id);
		return secteurs;
	}



	@Override
	public Secteur findById(Long id) {
		return secteurRepository.findByid(id);
	}











}
