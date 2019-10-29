package com.deroussen.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;
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
import com.deroussen.entities.Role;
import com.deroussen.entities.Spot;
import com.deroussen.entities.User;
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
			spot.setIs_official(false);		
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
			// @RequestParam(name="page", defaultValue= "0") int page,
			@RequestParam(name="motCle", defaultValue= "") String mc,
			/* @RequestParam(name="choice", defaultValue= "everyspots") String selector, */
			@RequestParam(name="id", defaultValue= "") Long spotId,
			@SessionAttribute(required=false, name="userEmail") String userEmail,		
			@RequestParam(name="checkbox_id", defaultValue= "off") String checkbox_id,
			@RequestParam(name="checkbox_name", defaultValue= "off") String checkbox_name,
			@RequestParam(name="checkbox_lieu", defaultValue= "off") String checkbox_lieu,
			@RequestParam(name="checkbox_cotation", defaultValue= "off") String checkbox_cotation,
			@RequestParam(name="checkbox_equipped", defaultValue= "off") String checkbox_equipped,
			@RequestParam(name="checkbox_official", defaultValue= "off") String checkbox_official,
			@RequestParam(name="checkbox_madeBy", defaultValue= "off") String checkbox_madeBy,
			@RequestParam(name="checkbox_secteur_nbre", defaultValue= "off") String checkbox_secteur_nbre
			) {
		
		ModelAndView modelView = new ModelAndView();
		List <Spot> spots = new ArrayList<>();
		if(spotId == null){
		spots = spotService.findBySpotContainsWithParam(mc, checkbox_id, checkbox_name, checkbox_lieu, checkbox_cotation, checkbox_equipped,
		checkbox_official, checkbox_madeBy, checkbox_secteur_nbre);
		}
		else if(spotId != null) {
			if(spotService.findById(spotId) != null) {
				spots.add(spotService.findById(spotId));
				checkbox_id = "on";
				mc = Long.toString(spotId);
				
			}
			else {
				//error reference to do
			}
		}
		
		
		List<String> listChoices = new ArrayList<>();
		
		
		/*
		String parameterToGetTheSpotResearch;
		Page <Spot> spots;
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
		*/
		
		
		
		if(userEmail != null) {
			User user = userService.findUserByEmail(userEmail);
			Set<Role> userRoles = user.getRoles();
			boolean isAMember = false;	
			C:for (Role role : userRoles) {
				if(role.getRole().equals("MEMBER") || role.getRole().equals("ADMIN")) {
					isAMember = true;
				}
				if(isAMember == true) {
					break C;
				}
			}			
			if(isAMember) {
			modelView.addObject("role","MEMBER");			
			}
		}
		modelView.addObject("checkbox_id", checkbox_id);
		modelView.addObject("checkbox_name", checkbox_name);
		modelView.addObject("checkbox_lieu", checkbox_lieu);
		modelView.addObject("checkbox_cotation", checkbox_cotation);
		modelView.addObject("checkbox_equipped", checkbox_equipped);
		modelView.addObject("checkbox_id", checkbox_id);
		modelView.addObject("checkbox_official", checkbox_official);
		modelView.addObject("checkbox_madeBy", checkbox_madeBy);
		modelView.addObject("checkbox_secteur_nbre", checkbox_secteur_nbre);
		modelView.addObject("spotlist", spots);
		// modelView.addObject("pages",new int[spots.getTotalPages()]);		
		// modelView.addObject("spotlist", spots.getContent());
	//	modelView.addObject("currentPage",page);
		modelView.addObject("motCle", mc);
		modelView.addObject("choices", listChoices);
		// modelView.addObject("selector", selector);
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
		Spot spotResearchByName = spotRepository.findBySpot_name(spot.getSpot_name());
		Long idVerificationSpotResearched = (long) -1;
		if(spotResearchByName != null) {
			idVerificationSpotResearched = spotResearchByName.getSpot_id();
		}	
		Long idVerificationSpotUpdated = spot.getSpot_id();
		// if both id corresponds then the name hasn't been changed but other informations may have been
		if(spotRepository.findBySpot_name(spot.getSpot_name()) == null || idVerificationSpotResearched.equals(idVerificationSpotUpdated)) {	
			spotUpdate.setSpot_name(spot.getSpot_name());
			spotUpdate.setSpot_lieu(spot.getSpot_lieu());
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
		}
		else {
			modelView.setViewName("redirect:/listespot?id="+spot.getSpot_id());
		}		
		return modelView;
	}
	
	
	@RequestMapping(value={"/deletespot"}, method=RequestMethod.GET)
	public ModelAndView delete(@RequestParam(name="id") Long spotId, 
			String motCle,
			@SessionAttribute("userEmail") String userEmail
			) {
		ModelAndView modelView = new ModelAndView();
		if(spotService.findById(spotId).getUser().getEmail().equals(userEmail)) {
			spotRepository.deleteById(spotId);	
			modelView.setViewName("redirect:/listespot");
		}
		else {
			modelView.setViewName("errors/access_denied");
		}
		
		return modelView;
	}
	
	
	@RequestMapping(value={"/spotofficiel"}, method=RequestMethod.GET)
	public ModelAndView spotBecomeOfficialOrNot(@RequestParam(name="id") Long spotId,
			@RequestParam(name="motCle", defaultValue= "") String mc,
			@SessionAttribute("userEmail") String userEmail) {
		ModelAndView modelView = new ModelAndView();
		User user = userService.findUserByEmail(userEmail);
		Set<Role> userRoles = user.getRoles();
		boolean isAMember= false;
		C:for (Role role : userRoles) {
			if(role.getRole().equals("MEMBER") || role.getRole().equals("ADMIN")) {
				isAMember = true;
			}
			if(isAMember == true) {
				break C;
			}
		}		
		if(isAMember) {
			Spot spot = spotRepository.findByid(spotId);
			if(spot.isIs_official() == true) {
				spot.setIs_official(false);
			}
			else {
				spot.setIs_official(true);
			}			
			spotRepository.save(spot);
			modelView.setViewName("redirect:/listespot?id="+spotId);
		}
		else {
			modelView.setViewName("errors/access_denied");
		}	
		return modelView;
	}

	
	
	
	
	
	/*
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
	*/

}


