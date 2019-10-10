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
import org.springframework.web.bind.annotation.SessionAttribute;
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
			@RequestParam(name="spotId") Long spotId,
			@RequestParam(name="spotname") String spotname,
			@RequestParam(name="madeByUser") String madeByUser,
			@RequestParam(name="is_equipped") boolean is_equipped,
			@RequestParam(name="is_official") boolean is_official
			) {
		ModelAndView modelView = new ModelAndView();
		modelView.addObject("spotId", spotId);
		modelView.addObject("spotname", spotname);
		modelView.addObject("madeByUser", madeByUser);
		modelView.addObject("is_equipped", is_equipped);
		modelView.addObject("is_official", is_official);
		modelView.setViewName("spot/createsecteur");
		return modelView;
	}
	
	@RequestMapping(value={"/createsecteur"}, method=RequestMethod.POST)
	public ModelAndView formPost(@Valid Secteur secteur, BindingResult bindingResult,
			@RequestParam(name="spotId") Long spotId,
			@RequestParam(name="spotname") String spotname,
			@RequestParam(name="madeByUser") String madeByUser,
			@RequestParam(name="is_equipped") boolean is_equipped,
			@RequestParam(name="is_official") boolean is_official,
			@RequestParam(name="secteurname") String secteurname
			) {
		ModelAndView modelView = new ModelAndView();	
		List <Secteur> secteurs = secteurService.findBySpotId(spotId);
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
			modelView.setViewName("redirect:/createsecteur?spotname="+spotname+"&spotId="+spotId+"&madeByUser="+madeByUser+"&is_equipped="+is_equipped+"&is_official="+is_official);
		}
		if (sizeList == matchesWithSizeList) {	
			secteur.setSpot(spotService.findById(spotId));	
			secteurService.saveSecteur(secteur);
			modelView.setViewName("redirect:/createvoie?spotname="+spotname+"&spotId="+spotId+"&madeByUser="+madeByUser+"&is_equipped="+is_equipped+"&is_official="+is_official
					+"&secteurname="+secteurname+"&secteurId="+secteur.getId());
		}		
		return modelView;
	}
	
	//Liste des secteurs avec parametres (arrive avec un spot conservé)
	@RequestMapping(value={"/listesecteur"}, method=RequestMethod.GET)
	public ModelAndView listeSecteur(
			@RequestParam(name="spotname") String spotname,
			@RequestParam(name="spotId") Long spotId,
			@RequestParam(name="user") String userThatCreateTheSpot,
			@RequestParam(name="page", defaultValue= "0") int page,
			@RequestParam(name="motCle", defaultValue= "") String mc) {
		ModelAndView modelView = new ModelAndView();
		/*
		Page <Secteur> secteursWithSpotId = new PageImpl<>(secteurRepository.findBySpotId(spotId));	
		Page<Secteur> secteurs = secteurRepository.findBySecteurnameContains(mc, PageRequest.of(page, 10));
		*/
		Page <Secteur> secteurs = secteurService.findBySecteurnameContainsFromSpotId(spotId, mc, PageRequest.of(page, 10));
		
		modelView.addObject("secteurlist", secteurs.getContent());
		modelView.addObject("pages",new int[secteurs.getTotalPages()]);
		modelView.addObject("currentPage",page);
		modelView.addObject("spotId", spotId);
		modelView.addObject("motCle", mc);
		modelView.addObject("spotname", spotname);
		modelView.addObject("userThatCreateTheSpot", userThatCreateTheSpot);
		modelView.setViewName("/spot/listesecteur");
		return modelView;
	}
	
	//Liste de tout les secteurs (seuls paramètres = page et motclef)
	@RequestMapping(value={"/listesecteurs"}, method=RequestMethod.GET)
	public ModelAndView listeSecteurNoParam(@RequestParam(name="page", defaultValue= "0") int page,
			@RequestParam(name="motCle", defaultValue= "") String mc) {
		ModelAndView modelView = new ModelAndView();
		Page<Secteur> secteurs = secteurRepository.findBySecteurnameContains(mc, PageRequest.of(page, 10));
		modelView.addObject("secteurlist", secteurs.getContent());
		modelView.addObject("pages",new int[secteurs.getTotalPages()]);
		modelView.addObject("currentPage",page);
		modelView.setViewName("/spot/listesecteur");
		return modelView;
	}
	
	
	@GetMapping("/deletesecteur")
	public ModelAndView delete(String motCle, int page,
			@RequestParam(name="id") Long id,
			@RequestParam(name="spotId") Long spotId,
			@SessionAttribute("userEmail") String userEmail
			) {
		ModelAndView modelView = new ModelAndView();
		if(spotService.findById(spotId).getUser().getEmail().equals(userEmail)) {
			secteurRepository.deleteById(id);	
			modelView.setViewName("redirect:/listesecteur?page="+page+"&motCle="+motCle);
		}
		else {
			modelView.setViewName("errors/access_denied");
		}
		return modelView;
	}
	
	@RequestMapping(value={"/changesecteur"}, method=RequestMethod.GET)
	public ModelAndView changeSecteurGet(@RequestParam(name="spotId") Long spotId,
			@RequestParam(name="spotname") String spotname,
			@RequestParam(name="secteurId") Long secteurId,
			@RequestParam(name="secteurname") String secteurname,
			@SessionAttribute("userEmail") String userEmail
			) {
		ModelAndView modelView = new ModelAndView();
		if(spotService.findById(spotId).getUser().getEmail().equals(userEmail)) {
			modelView.addObject("spotname", spotname);
			modelView.addObject("secteurname", secteurname);
			modelView.addObject("secteurId", secteurId);
			modelView.addObject("spotId", spotId);
			modelView.setViewName("spot/changesecteur");
		}
		else {
			modelView.setViewName("errors/access_denied");
		}
		return modelView;
	}
	
	@RequestMapping(value={"/changesecteur"}, method=RequestMethod.POST)
	public ModelAndView changeSecteurPost(@RequestParam(name="spotname") String spotname,
			@RequestParam(name="spotId") Long spotId,
			@RequestParam(name="secteurId") Long secteurId,
			@RequestParam(name="secteurname") String secteurname
			) {
		ModelAndView modelView = new ModelAndView();
		Secteur secteurUpdate = secteurRepository.getOne(secteurId);
		secteurUpdate.setSecteurname(secteurname);
		secteurRepository.save(secteurUpdate);
		modelView.setViewName("spot/listespot");
		return modelView;
	}
	
}
