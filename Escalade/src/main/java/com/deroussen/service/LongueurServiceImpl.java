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
import com.deroussen.entities.Secteur;

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
	public Page<Longueur> findByLongueurContainsFromVoieId(Long id, String motcle, Pageable page) {
		String mcLowerCase = motcle.toLowerCase();
		List <Longueur> longueurssWithVoieId = longueurRepository.findByVoieId(id);
		if(motcle.equals("")) {
			Page <Longueur> longueursPageList = new PageImpl<>(longueurssWithVoieId);
			return longueursPageList;
		}	
		
		else {
			List <Longueur> longueursThatMatchesWithResearch = new ArrayList<>();
			for(Longueur longueur : longueurssWithVoieId) {
				int indicatorThatlongueurIsAlreadyPut = 0;
				String longueurId = String.valueOf(longueur.getLongueur_id());
				if(longueurId.contains(mcLowerCase)) {
					longueursThatMatchesWithResearch.add(longueur);
					indicatorThatlongueurIsAlreadyPut++;
				}
				else if(longueur.getLongueur_name().toLowerCase().contains(mcLowerCase) && indicatorThatlongueurIsAlreadyPut == 0) {
					longueursThatMatchesWithResearch.add(longueur);
					indicatorThatlongueurIsAlreadyPut++;
				}
				else if (longueur.getLongueur_cotation().toLowerCase().contains(mcLowerCase) && indicatorThatlongueurIsAlreadyPut == 0){
					longueursThatMatchesWithResearch.add(longueur);
				}	
			}
		Page <Longueur> longueursPageList = new PageImpl<>(longueursThatMatchesWithResearch);
		return longueursPageList;
		}
	}


	@Override
	public Longueur findByid(Long id) {
		return longueurRepository.findByid(id);
	}
}





	


