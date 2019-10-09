package com.deroussen.controller;


import java.util.List;

import javax.validation.Valid;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.deroussen.dao.SecteurRepository;
import com.deroussen.entities.Secteur;
import com.deroussen.service.SecteurService;
import com.deroussen.service.SpotService;

@Controller
@SessionAttributes("userEmail")
public class SecteurController {

	@Autowired
	private SecteurRepository secteurRepository;	
	@Autowired
	private SecteurService secteurService;
	@Autowired
	private SpotService spotService;

	
	@RequestMapping(value={"/createsecteur"}, method=RequestMethod.GET)
	public ModelAndView formGet(
			@RequestParam(name="spotID") Long spotID,
			@RequestParam(name="spotname") String spotname,
			@RequestParam(name="madeByUser") String madeByUser,
			@RequestParam(name="Is_equipped") boolean Is_equipped,
			@RequestParam(name="Is_official") boolean Is_official
			) {
		ModelAndView modelView = new ModelAndView();
		modelView.addObject("spotID", spotID);
		modelView.addObject("spotname", spotname);
		modelView.addObject("madeByUser", madeByUser);
		modelView.addObject("Is_equipped", Is_equipped);
		modelView.addObject("Is_official", Is_official);
		modelView.setViewName("spot/createsecteur");
		return modelView;
	}
	
	@RequestMapping(value={"/createsecteur"}, method=RequestMethod.POST)
	public ModelAndView formPost(@Valid Secteur secteur, BindingResult bindingResult,
			@RequestParam(name="spotID") Long spotID,
			@RequestParam(name="spotname") String spotname,
			@RequestParam(name="madeByUser") String madeByUser,
			@RequestParam(name="Is_equipped") boolean Is_equipped,
			@RequestParam(name="Is_official") boolean Is_official,
			@RequestParam(name="secteurname") String secteurname
			) {
		ModelAndView modelView = new ModelAndView();	
		List <Secteur> secteurs = secteurService.findBySpotId(spotID);
		int sizeList = secteurs.size();
		int matchesWithSizeList = 0;
		C:for(int i = 0; i < secteurs.size(); i++) {
			matchesWithSizeList++;
			if(secteurs.get(i).getSecteurname() == secteur.getSecteurname()) {
				bindingResult.rejectValue("secteurname","error.secteur","The secteur name already exists!");
				break C;
			}
		}
		if(bindingResult.hasErrors()) {
			modelView.setViewName("redirect:/createsecteur?spotname="+spotname+"&spotID="+spotID+"&madeByUser="+madeByUser+"&Is_equipped="+Is_equipped+"&Is_official="+Is_official);
		}
		if (sizeList == matchesWithSizeList) {	
			secteur.setSpot(spotService.findById(spotID));	
			secteurService.saveSecteur(secteur);
			modelView.setViewName("redirect:/createvoie?spotname="+spotname+"&spotID="+spotID+"&madeByUser="+madeByUser+"&Is_equipped="+Is_equipped+"&Is_official="+Is_official
					+"&secteurname="+secteurname+"&secteurID="+secteur.getId());
		}		
		return modelView;
	}
	
	@RequestMapping(value={"/listesecteur"}, method=RequestMethod.GET)
	public ModelAndView listeSpot(Model model,
			@RequestParam(name="spotname") String spotname,
			@RequestParam(name="user") String userThatCreateTheSpot,
			@RequestParam(name="page", defaultValue= "0") int page,
			@RequestParam(name="motCle", defaultValue= "") String mc) {
		ModelAndView modelView = new ModelAndView();
		Page <Secteur> secteurs = secteurRepository.findBySecteurnameContains(mc, PageRequest.of(page, 10));
		model.addAttribute("secteurlist", secteurs.getContent());
		model.addAttribute("pages",new int[secteurs.getTotalPages()]);
		model.addAttribute("currentPage",page);
		model.addAttribute("motCle", mc);
		model.addAttribute("spotname", spotname);
		model.addAttribute("userThatCreateTheSpot", userThatCreateTheSpot);
		modelView.addObject(model);
		modelView.setViewName("/spot/listesecteur");
		return modelView;
	}
	
	@GetMapping("/deletesecteur")
	public String delete(Long id, String motCle, int page) {
		secteurRepository.deleteById(id);
		return "redirect:/listesecteur?page="+page+"&motCle="+motCle;
	}
	
}
