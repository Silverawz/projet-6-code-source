package com.deroussen.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.deroussen.entities.Spot;

public interface SpotService {
	public Spot findSpotByName(String name);
	public Spot findById(Long id);
	public void saveSpot(Spot spot);
	public Page<Spot> findBySpot_nameContains(String mc, Pageable page);
	
}
