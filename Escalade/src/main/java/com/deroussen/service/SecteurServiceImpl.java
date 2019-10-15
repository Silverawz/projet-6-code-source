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
	public Page<Secteur> findBySecteurContainsFromSpotId(Long id, String motcle, Pageable page) {
		String mcLowerCase = motcle.toLowerCase();
		List <Secteur> secteursWithSpotId = secteurRepository.findBySpotId(id);
		if(motcle.equals("")) {
			Page <Secteur> secteursPageList = new PageImpl<>(secteursWithSpotId);
			return secteursPageList;
		}	
	
		else {
			List <Secteur> secteurThatMatchesWithResearch = new ArrayList<>();
			for (Secteur secteur : secteursWithSpotId) {
				int indicatorThatSecteurIsAlreadyPut = 0;
				String secteurId = String.valueOf(secteur.getSecteur_id());
				if(secteurId.contains(mcLowerCase)) {
					secteurThatMatchesWithResearch.add(secteur);
					indicatorThatSecteurIsAlreadyPut++;
				}
				else if(secteur.getSecteur_name().toLowerCase().contains(mcLowerCase) && indicatorThatSecteurIsAlreadyPut == 0) {
					secteurThatMatchesWithResearch.add(secteur);
					indicatorThatSecteurIsAlreadyPut++;
				}
				else if (indicatorThatSecteurIsAlreadyPut == 0){
					String size = Integer.toString(secteur.getVoies().size());
					if(size.contains(mcLowerCase)) {
						secteurThatMatchesWithResearch.add(secteur);
					}
				}
			}
			Page <Secteur> secteursPageList = new PageImpl<>(secteurThatMatchesWithResearch);
			return secteursPageList;
		}
	}
}
