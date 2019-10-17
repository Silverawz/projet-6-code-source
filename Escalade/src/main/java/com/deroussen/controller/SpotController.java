package com.deroussen.controller;


import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.hibernate.mapping.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import com.deroussen.dao.SpotRepository;
import com.deroussen.entities.Spot;
import com.deroussen.entities.Voie;
import com.deroussen.service.SpotService;
import com.deroussen.service.UserService;


@Controller
@SessionAttributes("userEmail")
public class SpotController {
	
	@Autowired
	private SpotService spotService;
	@Autowired
	private SpotRepository spotRepository;
	@Autowired
	private UserService userService;

	
	@RequestMapping(value={"/createspot"}, method=RequestMethod.GET)
	public ModelAndView formGet() {
		ModelAndView modelView = new ModelAndView();
		modelView.setViewName("spot/createspot");
		return modelView;
	}
	
	@RequestMapping(value={"/createspot"}, method=RequestMethod.POST)
	public ModelAndView formPost(@Valid Spot spot, BindingResult bindingResult,
			@SessionAttribute("userEmail") String userEmail,
			@RequestParam(name="continueToCreateSecteur") boolean continueAndCreateASecteur
			) {
		ModelAndView modelView = new ModelAndView();
		Spot spotExists = spotService.findSpotByName(spot.getSpot_name());	
		if(spotExists != null) {
			bindingResult.rejectValue("spotname","This spotname already exists!"); //  TODO
		}
		if(bindingResult.hasErrors()) {
			modelView.setViewName("/spot/createspot"); 
		}
		if(spotExists == null) {
			if(spot.isIs_equipped() == true) {
				spot.setIs_equipped(true);
			}
			else {
				spot.setIs_equipped(false);
			}
			if(spot.isIs_official() == true) {
				spot.setIs_official(true);
			}
			else {
				spot.setIs_official(false);
			}
			spot.setUser(userService.findUserByEmail(userEmail));
			spotService.saveSpot(spot);
			// model.addObject("msg","The spot has been created successfully!"); // TODO
			if(continueAndCreateASecteur == true) {
				modelView.setViewName("redirect:/createsecteur?id="+spot.getSpot_id());
			}
			else {
				modelView.setViewName("redirect:/listespot?id="+spot.getSpot_id());
			}
		}	
		return modelView;
	}
	
	@RequestMapping(value={"/listespot"}, method=RequestMethod.GET)
	public ModelAndView listeSpot(
			@RequestParam(name="page", defaultValue= "0") int page,
			@RequestParam(name="motCle", defaultValue= "") String mc,
			@RequestParam(name="choice", defaultValue= "everyspots") String selector,
			@RequestParam(name="id", defaultValue= "") Long spotId
			) {
		ModelAndView modelView = new ModelAndView();
		List<String> listChoices = new ArrayList<>();
		Page <Spot> spots;
		String parameterToGetTheSpotResearch;
		if (spotId == null){
			listChoices = listOfChoices();	
			parameterToGetTheSpotResearch = comparingChoices(listChoices, selector);
			spots = spotService.findBySpotContains(mc, PageRequest.of(page, 3), parameterToGetTheSpotResearch);			
		}
		else {
			listChoices = listOfChoices();
			Spot spot = spotService.findById(spotId);
			List <Spot> spotsList = new ArrayList<>();
			spotsList.add(spot);
			spots = new PageImpl<>(spotsList);
		}
		
		modelView.addObject("pages",new int[spots.getTotalPages()]);		
		modelView.addObject("spotlist", spots.getContent());
		modelView.addObject("currentPage",page);
		modelView.addObject("motCle", mc);
		modelView.addObject("choices", listChoices);
		modelView.addObject("selector", selector);
		modelView.setViewName("/spot/listespot");
		return modelView;
	}
	                                                           
	@RequestMapping(value={"/changespot"}, method=RequestMethod.GET)
	public ModelAndView changeSpotGet(@RequestParam(name="id") Long spotId) {
		ModelAndView modelView = new ModelAndView();
		Spot spot = spotRepository.getOne(spotId);
		modelView.addObject("spot_id", spotId);
		modelView.addObject("spot_lieu", spot.getSpot_lieu());
		modelView.addObject("spot_name", spot.getSpot_name());
		modelView.addObject("is_equipped", spot.isIs_equipped());
		modelView.addObject("is_official", spot.isIs_official());
		modelView.setViewName("spot/changespot");
		return modelView;
	}
	
	
	@RequestMapping(value={"/changespot"}, method=RequestMethod.POST)
	public ModelAndView changeSpotPost(@Valid Spot spot, BindingResult bindingResult
			) {
		ModelAndView modelView = new ModelAndView();
		Spot spotUpdate = spotRepository.getOne(spot.getSpot_id());
		spotUpdate.setSpot_name(spot.getSpot_name());
		if(spot.isIs_equipped() == true) {
			spotUpdate.setIs_equipped(true);
		}
		else {
			spotUpdate.setIs_equipped(false);
		}
		if(spot.isIs_official() == true) {
			spotUpdate.setIs_official(true);
		}
		else {
			spotUpdate.setIs_official(false);
		}
		spotRepository.save(spotUpdate);
		modelView.setViewName("redirect:/listespot?id="+spot.getSpot_id());
		return modelView;
	}
	
	
	@RequestMapping(value={"/deletespot"}, method=RequestMethod.GET)
	public ModelAndView delete(@RequestParam(name="id") Long spotId, 
			String motCle, int page,
			@SessionAttribute("userEmail") String userEmail
			) {
		ModelAndView modelView = new ModelAndView();
		if(spotService.findById(spotId).getUser().getEmail().equals(userEmail)) {
			spotRepository.deleteById(spotId);	
			modelView.setViewName("redirect:/listespot?page="+page+"&motCle="+motCle);
		}
		else {
			modelView.setViewName("errors/access_denied");
		}
		
		return modelView;
	}
	
	

	public List<String> listOfChoices(){	
		String choice1 = "Tous les spots (peu importe si équipé ou officiel)"; 
		String choice2 = "Le spot est officiel et il est equipé";
		String choice3 = "Le spot est officiel mais il n'est pas equipé"; 
		String choice4 = "Le spot n'est pas officiel mais il est equipé";
		String choice5 = "Le spot n'est ni officiel ni equipé";
		List<String> choices = new ArrayList<>();
		choices.add(choice1);
		choices.add(choice2);
		choices.add(choice3);
		choices.add(choice4);
		choices.add(choice5);	
		return choices;
	}
	
	
	public String comparingChoices(List<String> list, String selector){
		String parameterToGetTheSpot = "";
		if(selector.equals(list.get(0))) {
			parameterToGetTheSpot = "everyspots";
		}
		else if(selector.equals(list.get(1))) {
			parameterToGetTheSpot = "true_true";
		}
		else if(selector.equals(list.get(2))) {
			parameterToGetTheSpot = "false_true";
		}
		else if(selector.equals(list.get(3))) {
			parameterToGetTheSpot = "true_false";
		}
		else if(selector.equals(list.get(4))){
			parameterToGetTheSpot = "false_false";
		}	
		return parameterToGetTheSpot;
	}
	

	
}


