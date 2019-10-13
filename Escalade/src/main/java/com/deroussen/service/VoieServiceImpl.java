package com.deroussen.service;

import java.util.ArrayList;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.deroussen.dao.VoieRepository;
import com.deroussen.entities.Voie;

@Service("voieService")
public class VoieServiceImpl implements VoieService {
	
	@Autowired
	private VoieRepository voieRepository;
	

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

	@Override
	public Page<Voie> findByVoienameContainsFromSecteurId(Long id, String mc, Pageable page) {
		List <Voie> voiesWithSecteurId = voieRepository.findBySecteurId(id);
		List <Voie> voiesThatMatchesWithResearch = new ArrayList<>();
		for (Voie voie : voiesWithSecteurId) {
			if(voie.getVoie_name().contains(mc)) {
				voiesThatMatchesWithResearch.add(voie);
			}	
		}
		Page <Voie> voiesPageList = new PageImpl<>(voiesThatMatchesWithResearch);
		return voiesPageList;
	}

	
}
