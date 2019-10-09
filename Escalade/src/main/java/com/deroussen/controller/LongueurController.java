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
			@RequestParam(name="spotID") Long spotID,
			@RequestParam(name="spotname") String spotname,
			@RequestParam(name="madeByUser") String madeByUser,
			@RequestParam(name="Is_equipped") boolean Is_equipped,
			@RequestParam(name="Is_official") boolean Is_official,
			@RequestParam(name="secteurname") String secteurname,
			@RequestParam(name="secteurID") Long secteurID,
			@RequestParam(name="voiename") String voiename,
			@RequestParam(name="voieID") Long voieID,
			@RequestParam(name="voiecotation") String voiecotation
			) {
		ModelAndView modelView = new ModelAndView();
		modelView.addObject("spotID", spotID);
		modelView.addObject("spotname", spotname);
		modelView.addObject("madeByUser", madeByUser);
		modelView.addObject("Is_equipped", Is_equipped);
		modelView.addObject("Is_official", Is_official);
		modelView.addObject("secteurname", secteurname);
		modelView.addObject("secteurID", secteurID);
		modelView.addObject("voiename", voiename);
		modelView.addObject("voieID", voieID);
		modelView.addObject("voiecotation", voiecotation);
		modelView.setViewName("spot/createlongueur");
		return modelView;
	}
	
	@RequestMapping(value={"/createlongueur"}, method=RequestMethod.POST)
	public ModelAndView formPost(@Valid Longueur longueur, BindingResult bindingResult, 
			@RequestParam(name="spotID") Long spotID,
			@RequestParam(name="spotname") String spotname,
			@RequestParam(name="madeByUser") String madeByUser,
			@RequestParam(name="Is_equipped") boolean Is_equipped,
			@RequestParam(name="Is_official") boolean Is_official,
			@RequestParam(name="secteurname") String secteurname,
			@RequestParam(name="secteurID") Long secteurID,
			@RequestParam(name="voiename") String voiename,
			@RequestParam(name="voieID") Long voieID,
			@RequestParam(name="voiecotation") String voiecotation,
			@RequestParam(name="longueurname") String longueurname,
			@RequestParam(name="longueurcotation") String longueurcotation) {
		ModelAndView modelView = new ModelAndView();
		List <Longueur> longueurs = longueurService.findByVoieId(voieID);
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
			modelView.setViewName("redirect:/createlongueur?spotname="+spotname+"&spotID="+spotID+"&madeByUser="+madeByUser+"&Is_equipped="+Is_equipped+"&Is_official="+Is_official
					+"&secteurname="+secteurname+"&secteurID="+secteurID+"&voiename="+voiename+"&voieID="+voieID+"&voiecotation="+voiecotation);
		}
		if (sizeList == matchesWithSizeList) {
			longueur.setVoie(voieService.findById(voieID));	
			longueurService.saveLongueur(longueur);
			modelView.setViewName("redirect:/listespot");
		}		
		return modelView;
	}

}
