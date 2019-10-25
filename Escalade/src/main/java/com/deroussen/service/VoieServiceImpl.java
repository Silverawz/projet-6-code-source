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
	public Page<Voie> findByVoieContainsFromSecteurId(Long id, String motcle, Pageable page) {
		String mcLowerCase = motcle.toLowerCase();	
		List <Voie> voiesWithSecteurId = voieRepository.findBySecteurId(id);
		if(motcle.equals("")) {
			Page <Voie> voiesPageList = new PageImpl<>(voiesWithSecteurId);
			return voiesPageList;
		}
		
		else {
			List <Voie> voiesThatMatchesWithResearch = new ArrayList<>();
			for(Voie voie : voiesWithSecteurId) {
				
				int indicatorThatVoieIsAlreadyPut = 0;
				String voieId = String.valueOf(voie.getVoie_id());
				if(voieId.contains(mcLowerCase)) {
					voiesThatMatchesWithResearch.add(voie);
					indicatorThatVoieIsAlreadyPut++;
				}
				else if(voie.getVoie_name().toLowerCase().contains(mcLowerCase)) {
					voiesThatMatchesWithResearch.add(voie);
					indicatorThatVoieIsAlreadyPut++;
				}
				else if(voie.getVoie_cotation().toLowerCase().contains(mcLowerCase)) {
					voiesThatMatchesWithResearch.add(voie);
					indicatorThatVoieIsAlreadyPut++;	
				}
				else if(indicatorThatVoieIsAlreadyPut == 0) {
					String size = Integer.toString(voie.getLongueurs().size());
					if(size.contains(mcLowerCase)) {
						voiesThatMatchesWithResearch.add(voie);
					}	
				}						
			}
		Page <Voie> voiesPageList = new PageImpl<>(voiesThatMatchesWithResearch);
		return voiesPageList;	
		}
	}




	@Override
	public Voie findByVoie_name(String name) {
		return voieRepository.findByVoie_name(name);
	}

	
	
	
}
