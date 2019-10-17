package com.deroussen.service;




import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.deroussen.dao.SpotRepository;
import com.deroussen.entities.Spot;
import com.deroussen.entities.Voie;

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
	public Page <Spot> findBySpotContains(String motcle, Pageable page, String choice) {
		String mcLowerCase = motcle.toLowerCase();
		List<Spot> spotsList = new ArrayList<Spot>();
		if(choice.contentEquals("everyspots")) {
			spotsList = spotRepository.findAll();
		}
		if(choice.contentEquals("true_true")) {
			List<Spot> spotsList1 = new ArrayList<Spot>();List<Spot> spotsList2 = new ArrayList<Spot>();
			spotsList1 = spotRepository.findByEquipped(true);
			spotsList2 = spotRepository.findByOfficial(true);
			spotsList = iterationList(spotsList1,spotsList2);
		}
		
		if(choice.contentEquals("true_false")) {
			List<Spot> spotsList1 = new ArrayList<Spot>();List<Spot> spotsList2 = new ArrayList<Spot>();
			spotsList1 = spotRepository.findByEquipped(true);
			spotsList2 = spotRepository.findByOfficial(false);
			spotsList = iterationList(spotsList1,spotsList2);
		}
		
		if(choice.contentEquals("false_true")) {
			List<Spot> spotsList1 = new ArrayList<Spot>();List<Spot> spotsList2 = new ArrayList<Spot>();
			spotsList1 = spotRepository.findByEquipped(false);
			spotsList2 = spotRepository.findByOfficial(true);
			spotsList = iterationList(spotsList1,spotsList2);
		}
		
		if(choice.contentEquals("false_false")) {
			List<Spot> spotsList1 = new ArrayList<Spot>();List<Spot> spotsList2 = new ArrayList<Spot>();
			spotsList1 = spotRepository.findByEquipped(false);
			spotsList2 = spotRepository.findByOfficial(false);
			spotsList = iterationList(spotsList1,spotsList2);
		}
		
		if(motcle.equals("")) {
			Page <Spot> spotsPageList = new PageImpl<>(spotsList);
			return spotsPageList;
		}		
		else {
			List <Spot> spotThatMatchesWithResearch = new ArrayList<>();
			for (Spot spot : spotsList) {
				int indicatorThatSpotIsAlreadyPut = 0;
				String spotId = String.valueOf(spot.getSpot_id());
				if(spotId.contains(mcLowerCase)) {
					spotThatMatchesWithResearch.add(spot);
					indicatorThatSpotIsAlreadyPut++;
				}
				else if(spot.getSpot_name().toLowerCase().contains(mcLowerCase) && indicatorThatSpotIsAlreadyPut == 0) {
					spotThatMatchesWithResearch.add(spot);
					indicatorThatSpotIsAlreadyPut++;
				}
				else if(spot.getSpot_lieu().toLowerCase().contains(mcLowerCase) && indicatorThatSpotIsAlreadyPut == 0) {
					spotThatMatchesWithResearch.add(spot);
					indicatorThatSpotIsAlreadyPut++;
				}
				else if(spot.getUser().getFirstname().toLowerCase().contains(mcLowerCase) && indicatorThatSpotIsAlreadyPut == 0) {
					spotThatMatchesWithResearch.add(spot);
					indicatorThatSpotIsAlreadyPut++;	
				}
				else if (indicatorThatSpotIsAlreadyPut == 0){
					String size = Integer.toString(spot.getSecteurs().size());
					if(size.contains(mcLowerCase)) {
						spotThatMatchesWithResearch.add(spot);
						indicatorThatSpotIsAlreadyPut++;	
					}
				}
			}
			Page <Spot> spotsPageList = new PageImpl<>(spotThatMatchesWithResearch);
			return spotsPageList;	
		}
	}

	
	public List<Spot> iterationList(List<Spot> list1, List<Spot> list2) {
		List<Spot> spotsList = new ArrayList<Spot>();
		for (Spot spot : list1) {
			if(list2.contains(spot) && !(spotsList.contains(spot))) {
				spotsList.add(spot);
			}
		}
		for (Spot spot : list2) {
			if(list1.contains(spot) && !(spotsList.contains(spot))) {
				spotsList.add(spot);
			}
		}		
		return spotsList;	
	}

	
	
	
}
