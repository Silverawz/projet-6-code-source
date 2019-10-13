package com.deroussen.service;



import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
		return secteurRepository.findBySecteur_name(name);
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



	@Override
	public Page<Secteur> findBySecteurnameContainsFromSpotId(Long id, String mc, Pageable page) {
		List <Secteur> secteursWithSpotId = secteurRepository.findBySpotId(id);
		List <Secteur> secteursThatMatchesWithResearch = new ArrayList<>();
		for (Secteur secteur : secteursWithSpotId) {
			if(secteur.getSecteur_name().contains(mc)) {
				secteursThatMatchesWithResearch.add(secteur);
			}	
		}
		Page <Secteur> secteursPageList = new PageImpl<>(secteursThatMatchesWithResearch);
		return secteursPageList;
	}







	



















}
