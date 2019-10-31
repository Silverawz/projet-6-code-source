package com.deroussen.service;




import java.util.ArrayList;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deroussen.dao.SpotRepository;
import com.deroussen.entities.Longueur;
import com.deroussen.entities.Secteur;
import com.deroussen.entities.Spot;
import com.deroussen.entities.Voie;

@Service("spotService")
public class SpotServiceImpl implements SpotService {

	@Autowired
	private SpotRepository spotRepository;

	@Override
	public void saveSpot(Spot spot) {
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

	
	/*
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
*/
	
	
	
	
	
	/*
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
*/
	

	
	
	@Override
	public List<Spot> findBySpotContainsWithParam(String motcle, String checkbox_id, String checkbox_name, String checkbox_lieu,
			String checkbox_cotation, String checkbox_equipped, String checkbox_official, String checkbox_madeBy, String checkbox_secteur_nbre) {	
		String mcLowerCase = motcle.toLowerCase();
		List<Spot> allSpots = spotRepository.findAll();
		List<Spot> spotListResult = new ArrayList<>();

		if(checkbox_id.equals("on") && !mcLowerCase.equals("")) {
			for (Spot spot : allSpots) {
				String idCastedFromLongToString = String.valueOf(spot.getSpot_id());
				if(mcLowerCase.contains(idCastedFromLongToString)){
					spotListResult.add(spot);
				}
			}	
		}
		if(checkbox_name.equals("on") && !mcLowerCase.equals("")) {
			for (Spot spot : allSpots) {
				if(mcLowerCase.contains(spot.getSpot_name().toLowerCase())){
					if(!spotListResult.contains(spot)){
						spotListResult.add(spot);
					}			
				}
			}	
		}
		if(checkbox_lieu.equals("on") && !mcLowerCase.equals("")) {
			for (Spot spot : allSpots) {
				if(mcLowerCase.contains(spot.getSpot_lieu().toLowerCase())){
					if(!spotListResult.contains(spot)){
						spotListResult.add(spot);
					}			
				}
			}	
		}
		if(checkbox_cotation.equals("on") && !mcLowerCase.equals("")) {
			for (Spot spot : allSpots) {				
				for (Secteur secteur : spot.getSecteurs()) {
					for (Voie voie : secteur.getVoies()) {
						if(mcLowerCase.contains(voie.getVoie_cotation().toLowerCase())) {
							if(!spotListResult.contains(spot)){
								spotListResult.add(spot);
							}	
						}						
						for (Longueur longueur : voie.getLongueurs()) {
							if(mcLowerCase.contains(longueur.getLongueur_cotation().toLowerCase())) {
								if(!spotListResult.contains(spot)){
									spotListResult.add(spot);
								}	
							}
						}
					}			
				}
			}

		}
		if(checkbox_equipped.equals("on") && checkbox_official.equals("off")) {
			for (Spot spot : allSpots) {
				if(spot.isIs_equipped() == true){
					if(!spotListResult.contains(spot)){
						spotListResult.add(spot);
					}			
				}
			}	
		}
		if(checkbox_official.equals("on") && checkbox_equipped.equals("off")) {
			for (Spot spot : allSpots) {
				if(spot.isIs_official() == true){
					if(!spotListResult.contains(spot)){
						spotListResult.add(spot);
					}			
				}
			}	
		}	
		if(checkbox_official.equals("on") && checkbox_equipped.equals("on")) {
			for (Spot spot : allSpots) {
				if(spot.isIs_official() == true && spot.isIs_equipped() == true) {
					if(!spotListResult.contains(spot)){
						spotListResult.add(spot);
					}			
				}
			}	
		}
		if(checkbox_madeBy.equals("on") && !mcLowerCase.equals("")) {
			for (Spot spot : allSpots) {
				if(mcLowerCase.contains(spot.getUser().getFirstname().toLowerCase())){
					if(!spotListResult.contains(spot)){
						spotListResult.add(spot);
					}			
				}
			}	
		}
		if(checkbox_secteur_nbre.equals("on") && !mcLowerCase.equals("")) {
			for (Spot spot : allSpots) {
				if(mcLowerCase.contains(Integer.toString(spot.getSecteurs().size()))){
					if(!spotListResult.contains(spot)){
						spotListResult.add(spot);
					}			
				}
			}	
		}
		if(spotListResult.size() != 0) {
			Collections.sort(spotListResult);
		}
		
		if(checkbox_id.equals("off") && checkbox_name.equals("off") && checkbox_lieu.equals("off") && checkbox_cotation.equals("off") &&
		checkbox_equipped.equals("off") && checkbox_official.equals("off") && checkbox_madeBy.equals("off") && checkbox_secteur_nbre.equals("off")
		&& mcLowerCase.equals("")) {
			spotListResult = allSpots;
		}
		return spotListResult;
	}


}
