package com.deroussen.service;




import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
		return spotRepository.findBySpot_name(name);
	}



	@Override
	public Spot findById(Long id) {
		return spotRepository.findByid(id);
	}

	@Override
	public Page<Spot> findBySpot_nameContains(String mc, Pageable page) {
		List<Spot> spotsList = spotRepository.findAll();
		if(mc.equals("")) {
			Page <Spot> spotsPageList = new PageImpl<>(spotsList);
			return spotsPageList;
		}
		else {
			List <Spot> spotThatMatchesWithResearch = new ArrayList<>();
			for (Spot spot : spotsList) {
				if(spot.getSpot_name().contains(mc)) {
					spotThatMatchesWithResearch.add(spot);
				}	
			}		
			Page <Spot> spotsPageList = new PageImpl<>(spotThatMatchesWithResearch);
			return spotsPageList;	
		}
	}

	
}
