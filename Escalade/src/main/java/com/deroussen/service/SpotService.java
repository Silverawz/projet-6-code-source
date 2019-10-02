package com.deroussen.service;

import com.deroussen.entities.Spot;

public interface SpotService {
	public Spot findSpotByName(String name);
	
	public void saveSpot(Spot spot);
}
