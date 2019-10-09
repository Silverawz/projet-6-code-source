package com.deroussen.service;




import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.deroussen.dao.SpotRepository;
import com.deroussen.entities.Spot;

@Service("spotService")
public class SpotServiceImpl implements SpotService {

	@Autowired
	private SpotRepository spotRepository;

	@Override
	public void saveSpot(Spot spot) {
		spot.setIs_official(false);
		spotRepository.save(spot);
	}

	@Override
	public Spot findSpotByName(String name) {
		return spotRepository.findBySpotname(name);
	}



	@Override
	public Spot findById(Long id) {
		return spotRepository.findByid(id);
	}

	
}
