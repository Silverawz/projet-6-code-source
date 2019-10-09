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
import com.deroussen.entities.Voie;
import com.deroussen.service.SecteurService;
import com.deroussen.service.VoieService;


@Controller
@SessionAttributes("userEmail")
public class VoieController {
	
	@Autowired
	private VoieService voieService;
	@Autowired
	private SecteurService secteurService;

	
	
	@RequestMapping(value={"/createvoie"}, method=RequestMethod.GET)
	public ModelAndView formGet(Model model, 
			@RequestParam(name="spotID") Long spotID,
			@RequestParam(name="spotname") String spotname,
			@RequestParam(name="madeByUser") String madeByUser,
			@RequestParam(name="Is_equipped") boolean Is_equipped,
			@RequestParam(name="Is_official") boolean Is_official,
			@RequestParam(name="secteurname") String secteurname,
			@RequestParam(name="secteurID") Long secteurID			
			) {
		ModelAndView modelView = new ModelAndView();
		modelView.addObject("spotID", spotID);
		modelView.addObject("spotname", spotname);
		modelView.addObject("madeByUser", madeByUser);
		modelView.addObject("Is_equipped", Is_equipped);
		modelView.addObject("Is_official", Is_official);
		modelView.addObject("secteurname", secteurname);
		modelView.addObject("secteurID", secteurID);
		modelView.setViewName("spot/createvoie");
		return modelView;
	}
	
	@RequestMapping(value={"/createvoie"}, method=RequestMethod.POST)
	public ModelAndView formPost(@Valid Voie voie, BindingResult bindingResult, 
			@RequestParam(name="spotID") Long spotID,
			@RequestParam(name="spotname") String spotname,
			@RequestParam(name="madeByUser") String madeByUser,
			@RequestParam(name="Is_equipped") boolean Is_equipped,
			@RequestParam(name="Is_official") boolean Is_official,
			@RequestParam(name="secteurname") String secteurname,
			@RequestParam(name="secteurID") Long secteurID,
			@RequestParam(name="voiename") String voiename,
			@RequestParam(name="voiecotation") String voiecotation) {
		
		ModelAndView modelView = new ModelAndView();
		List <Voie> voies = voieService.findBySecteurId(secteurID);
		int sizeList = voies.size();
		int matchesWithSizeList = 0;
		C:for(int i = 0; i < voies.size(); i++) {
			matchesWithSizeList++;
			if(voies.get(i).getVoiename() == voie.getVoiename()) {
				bindingResult.rejectValue("voiename","error.voie","The voie name already exists!");
				break C;
			}
		}
		if(bindingResult.hasErrors()) {
			modelView.setViewName("redirect:/createvoie?spotname="+spotname+"&spotID="+spotID+"&madeByUser="+madeByUser+"&Is_equipped="+Is_equipped+"&Is_official="+Is_official
					+"&secteurname="+secteurname+"&secteurID="+secteurID);
		}
		if (sizeList == matchesWithSizeList) {
			voie.setSecteur(secteurService.findById(secteurID));
			voieService.saveVoie(voie);
			modelView.setViewName("redirect:/createlongueur?spotname="+spotname+"&spotID="+spotID+"&madeByUser="+madeByUser+"&Is_equipped="+Is_equipped+"&Is_official="+Is_official
			+"&secteurname="+secteurname+"&secteurID="+secteurID+"&voiename="+voiename+"&voieID="+voie.getId()+"&voiecotation="+voiecotation);
		}		
		return modelView;
	}
	
	
}
