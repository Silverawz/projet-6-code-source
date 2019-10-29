package com.deroussen.service;




import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.deroussen.entities.Spot;

public interface SpotService {
	public Spot findSpotByName(String name);
	public Spot findById(Long id);
	
	
	
	public List<Spot> findBySpotContainsWithParam(String motcle, String checkbox_id, String checkbox_name, String checkbox_lieu, String checkbox_cotation,
			String checkbox_equipped, String checkbox_official, String checkbox_madeBy, String checkbox_secteur_nbre);
	
	
	public void saveSpot(Spot spot);
	// public Page<Spot> findBySpotContains(String mc, Pageable page, String choice);
	
	
}
