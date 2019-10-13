package com.deroussen.service;

import java.util.ArrayList;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.deroussen.dao.LongueurRepository;
import com.deroussen.entities.Longueur;

@Service("longueurService")
public class LongueurServiceImpl implements LongueurService {

	@Autowired
	private LongueurRepository longueurRepository;
	
	
	
	@Override
	public void saveLongueur(Longueur longueur) {
		longueurRepository.save(longueur);
	}

	
	@Override
	public List<Longueur> findByVoieId(Long id) {
		List <Longueur> longueurs = longueurRepository.findByVoieId(id);
		return longueurs;
	}


	@Override
	public Page<Longueur> findByLongueurnameContainsFromVoieId(Long id, String mc, Pageable page) {
			List <Longueur> longueurssWithVoieId = longueurRepository.findByVoieId(id);
			List <Longueur> longueursThatMatchesWithResearch = new ArrayList<>();
			for (Longueur longueur : longueurssWithVoieId) {
				if(longueur.getLongueur_name().contains(mc)) {
					longueursThatMatchesWithResearch.add(longueur);
				}	
			}
			Page <Longueur> longueursPageList = new PageImpl<>(longueursThatMatchesWithResearch);
			return longueursPageList;
		}


	@Override
	public Longueur findByid(Long id) {
		return longueurRepository.findByid(id);
	}
}





	


