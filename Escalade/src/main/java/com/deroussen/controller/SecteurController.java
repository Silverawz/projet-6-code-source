package com.deroussen.controller;

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

import com.deroussen.entities.Secteur;
import com.deroussen.entities.Spot;
import com.deroussen.service.SecteurService;

@Controller
@SessionAttributes("userName")
public class SecteurController {

	@Autowired
	SecteurService secteurService;
	
	@RequestMapping(value={"/createSecteur"}, method=RequestMethod.GET)
	public ModelAndView formGet(Model model, @RequestParam(name="spotname") String spotname ,@RequestParam(name="madeByUser") String madeByUser) {
		ModelAndView modelView = new ModelAndView();
		modelView.addObject("madeByUser", madeByUser);
		modelView.addObject("spotname", spotname);
		modelView.setViewName("spot/createSecteur");
		return modelView;
	}
	
	@RequestMapping(value={"/createSecteur"}, method=RequestMethod.POST)
	public ModelAndView formPost(@Valid Secteur secteur, BindingResult bindingResult, 
			@RequestParam(name="secteurname") String secteurname, @RequestParam(name="spotname") String spotname) {
		ModelAndView modelView = new ModelAndView();
		Secteur secteurExsists = secteurService.findSecteurByName(secteur.getSecteurname());
		if(secteurExsists != null) {
			bindingResult.rejectValue("secteurname","error.spot","The secteur name already exists!");
		}
		if(bindingResult.hasErrors()) {
			modelView.setViewName("/spot/createsecteur");
		}
		else {
			secteur.setSpotname(spotname);
			secteurService.saveSecteur(secteur);
			modelView.addObject("secteur",  new Secteur());	
			modelView.setViewName("redirect:/createVoie?secteurame="+secteur.getSecteurname());
		}	
		
		return modelView;
	}
	
	
	
}
