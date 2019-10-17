package com.deroussen.service;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.deroussen.entities.Spot;

public interface SpotService {
	public Spot findSpotByName(String name);
	public Spot findById(Long id);
	
	
	
	
	public void saveSpot(Spot spot);
	public Page<Spot> findBySpotContains(String mc, Pageable page, String choice);
	
	
}
