package com.deroussen.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.deroussen.entities.Longueur;
import com.deroussen.service.LongueurService;
import com.deroussen.service.VoieService;

@Controller
@SessionAttributes("userEmail")
public class LongueurController {
	
	@Autowired
	private LongueurService longueurService;
	@Autowired
	private VoieService voieService;
	
	@RequestMapping(value={"/createlongueur"}, method=RequestMethod.GET)
	public ModelAndView formGet(Model model, 
			@RequestParam(name="spotId") Long spotId,
			@RequestParam(name="spotname") String spotname,
			@RequestParam(name="madeByUser") String madeByUser,
			@RequestParam(name="is_equipped") boolean is_equipped,
			@RequestParam(name="is_official") boolean is_official,
			@RequestParam(name="secteurname") String secteurname,
			@RequestParam(name="secteurId") Long secteurId,
			@RequestParam(name="voiename") String voiename,
			@RequestParam(name="voieId") Long voieId,
			@RequestParam(name="voiecotation") String voiecotation
			) {
		ModelAndView modelView = new ModelAndView();
		modelView.addObject("spotId", spotId);
		modelView.addObject("spotname", spotname);
		modelView.addObject("madeByUser", madeByUser);
		modelView.addObject("is_equipped", is_equipped);
		modelView.addObject("is_official", is_official);
		modelView.addObject("secteurname", secteurname);
		modelView.addObject("secteurId", secteurId);
		modelView.addObject("voiename", voiename);
		modelView.addObject("voieId", voieId);
		modelView.addObject("voiecotation", voiecotation);
		modelView.setViewName("spot/createlongueur");
		return modelView;
	}
	
	@RequestMapping(value={"/createlongueur"}, method=RequestMethod.POST)
	public ModelAndView formPost(@Valid Longueur longueur, BindingResult bindingResult, 
			@RequestParam(name="spotId") Long spotId,
			@RequestParam(name="spotname") String spotname,
			@RequestParam(name="madeByUser") String madeByUser,
			@RequestParam(name="is_equipped") boolean is_equipped,
			@RequestParam(name="is_official") boolean is_official,
			@RequestParam(name="secteurname") String secteurname,
			@RequestParam(name="secteurId") Long secteurId,
			@RequestParam(name="voiename") String voiename,
			@RequestParam(name="voieId") Long voieId,
			@RequestParam(name="voiecotation") String voiecotation,
			@RequestParam(name="longueurname") String longueurname,
			@RequestParam(name="longueurcotation") String longueurcotation) {
		ModelAndView modelView = new ModelAndView();
		List <Longueur> longueurs = longueurService.findByVoieId(voieId);
		int sizeList = longueurs.size();
		int matchesWithSizeList = 0;
		C:for(int i = 0; i < longueurs.size(); i++) {
			matchesWithSizeList++;
			if(longueurs.get(i).getLongueurname()== longueur.getLongueurname()) {
				bindingResult.rejectValue("longueurname","error.spot","The longueur name already exists!");
				break C;
			}
		}
		if(bindingResult.hasErrors()) {
			modelView.setViewName("redirect:/createlongueur?spotname="+spotname+"&spotId="+spotId+"&madeByUser="+madeByUser+"&is_equipped="+is_equipped+"&is_official="+is_official
					+"&secteurname="+secteurname+"&secteurId="+secteurId+"&voiename="+voiename+"&voieId="+voieId+"&voiecotation="+voiecotation);
		}
		if (sizeList == matchesWithSizeList) {
			longueur.setVoie(voieService.findById(voieId));	
			longueurService.saveLongueur(longueur);
			modelView.setViewName("redirect:/listespot");
		}		
		return modelView;
	}

}
