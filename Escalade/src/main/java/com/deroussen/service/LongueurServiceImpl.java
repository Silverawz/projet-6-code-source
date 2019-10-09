package com.deroussen.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deroussen.dao.LongueurRepository;
import com.deroussen.entities.Longueur;
import com.deroussen.entities.Voie;

@Service("longueurService")
public class LongueurServiceImpl implements LongueurService {

	@Autowired
	private LongueurRepository longueurRepository;
	
	@Override
	public Longueur findLongueurByName(String name) {
		return longueurRepository.findByLongueurname(name);	
	}

	@Override
	public void saveLongueur(Longueur longueur) {
		longueurRepository.save(longueur);
	}

	@Override
	public List<Longueur> findByVoieId(Long id) {
		List <Longueur> longueurs = longueurRepository.findByVoieId(id);
		return longueurs;
	}

}
