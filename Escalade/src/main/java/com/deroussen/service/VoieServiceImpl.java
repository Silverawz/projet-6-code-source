package com.deroussen.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.deroussen.dao.VoieRepository;
import com.deroussen.entities.Voie;

@Service("voieService")
public class VoieServiceImpl implements VoieService {
	
	@Autowired
	private VoieRepository voieRepository;
	
	@Override
	public Voie findVoieByName(String name) {
		return voieRepository.findByVoiename(name);
	}

	@Override
	public void saveVoie(Voie voie) {
		voieRepository.save(voie);
	}

	@Override
	public List<Voie> findBySecteurId(Long id) {
		List <Voie> voies = voieRepository.findBySecteurId(id);
		return voies;
	}

	@Override
	public Voie findById(Long id) {
		return voieRepository.findByid(id);
	}

}
