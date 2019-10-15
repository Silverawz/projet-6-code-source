package com.deroussen.controller;

import java.util.List;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.deroussen.dao.LongueurRepository;
import com.deroussen.entities.Longueur;
import com.deroussen.entities.Secteur;
import com.deroussen.entities.Spot;
import com.deroussen.entities.Voie;
import com.deroussen.service.LongueurService;
import com.deroussen.service.SecteurService;
import com.deroussen.service.SpotService;
import com.deroussen.service.VoieService;

@Controller
@SessionAttributes("userEmail")
public class LongueurController {
	
	@Autowired
	private LongueurService longueurService;
	@Autowired
	private LongueurRepository longueurRepository;
	@Autowired
	private VoieService voieService;
	@Autowired
	private SecteurService secteurService;
	@Autowired
	private SpotService spotService;
	
	
	@RequestMapping(value={"/listelongueur"}, method=RequestMethod.GET)
	public ModelAndView listeVoie(@RequestParam(name="id") Long voieId,
			@RequestParam(name="page", defaultValue= "0") int page,
			@RequestParam(name="motCle", defaultValue= "") String mc) {
		ModelAndView modelView = new ModelAndView();
		Page <Longueur> longueurs = longueurService.findByLongueurContainsFromVoieId(voieId, mc, PageRequest.of(page, 10));
		Voie voie = voieService.findById(voieId);
		Long secteurId = voie.getSecteur().getSecteur_id();
		Secteur secteur = secteurService.findById(secteurId);
		Long spotId = secteurService.findById(secteurId).getSpot().getSpot_id();
		Spot spot = spotService.findById(spotId);
		modelView.addObject("longueurlist", longueurs.getContent());
		modelView.addObject("pages",new int[longueurs.getTotalPages()]);
		modelView.addObject("currentPage",page);
		modelView.addObject("motCle", mc);
		modelView.addObject("userThatCreateTheSpot", spot.getUser().getEmail());
		modelView.addObject("spot_id", spot.getSpot_id());
		modelView.addObject("spot_lieu", spot.getSpot_lieu());
		modelView.addObject("spot_name", spot.getSpot_name());
		modelView.addObject("secteur_id", secteur.getSecteur_id());
		modelView.addObject("secteur_name", secteur.getSecteur_name());
		modelView.addObject("voie_id", voie.getVoie_id());
		modelView.addObject("voie_name", voie.getVoie_name());
		modelView.setViewName("/spot/listelongueur");
		return modelView;
	}
	
	
	@RequestMapping(value={"/createlongueur"}, method=RequestMethod.GET)
	public ModelAndView formGet(@RequestParam(name="id") Long voieId
			) {
		ModelAndView modelView = new ModelAndView();	
		Voie voie = voieService.findById(voieId);
		Long secteurId = voie.getSecteur().getSecteur_id();
		Secteur secteur = secteurService.findById(secteurId);
		Long spotId = secteur.getSpot().getSpot_id();
		Spot spot = spotService.findById(spotId);
		modelView.addObject("spot_id", spotId);
		modelView.addObject("spot_lieu", spot.getSpot_lieu());
		modelView.addObject("spot_name", spot.getSpot_name());
		modelView.addObject("is_equipped", spot.isIs_equipped());
		modelView.addObject("is_official", spot.isIs_official());
		modelView.addObject("madeByUser", spot.getUser().getFirstname());
		modelView.addObject("secteur_id", secteurId);
		modelView.addObject("secteur_name", secteur.getSecteur_name());
		modelView.addObject("voie_id", voieId);
		modelView.addObject("voie_name", voie.getVoie_name());
		modelView.addObject("voie_cotation", voie.getVoie_cotation());
		modelView.setViewName("spot/createlongueur");
		return modelView;
	}
	
	@RequestMapping(value={"/createlongueur"}, method=RequestMethod.POST)
	public ModelAndView formPost(@Valid Longueur longueur, BindingResult bindingResult, 
			@RequestParam(name="voie_id") Long voieId) {
		ModelAndView modelView = new ModelAndView();
		List <Longueur> longueurs = longueurService.findByVoieId(voieId);
		int sizeList = longueurs.size();
		int matchesWithSizeList = 0;
		C:for(int i = 0; i < longueurs.size(); i++) {
			matchesWithSizeList++;
			if(longueurs.get(i).getLongueur_name()== longueur.getLongueur_name()) {
				bindingResult.rejectValue("longueurname","error.spot","The longueur name already exists!");
				break C;
			}
		}
		if(bindingResult.hasErrors()) {
			modelView.setViewName("redirect:/createlongueur?id="+voieId);
		}
		if (sizeList == matchesWithSizeList) {
			longueur.setVoie(voieService.findById(voieId));
			longueurService.saveLongueur(longueur);
			modelView.setViewName("redirect:/listelongueur?id="+voieId);
		}		
		return modelView;
	}

	
	@GetMapping("/deletelongueur")
	public ModelAndView delete(String motCle, int page,
			@RequestParam(name="id") Long longueurId,
			@SessionAttribute("userEmail") String userEmail
			) {	
		ModelAndView modelView = new ModelAndView();
		Longueur longueur = longueurService.findByid(longueurId);
		Long voieId = longueur.getVoie().getVoie_id();
		Voie voie = voieService.findById(voieId);
		Long secteurId = voie.getSecteur().getSecteur_id();
		Secteur secteur = secteurService.findById(secteurId);
		Long spotId = secteur.getSpot().getSpot_id();	
		Spot spot = spotService.findById(spotId);
		if(spot.getUser().getEmail().equals(userEmail)){
			longueurRepository.deleteById(longueurId);
			modelView.setViewName("redirect:/listelongueur?id="+voieId);
		}
		else {
			modelView.setViewName("errors/access_denied");
		}
		return modelView;
	}

	
	@RequestMapping(value={"/changelongueur"}, method=RequestMethod.GET)
	public ModelAndView changeVoieGet(@RequestParam(name="id") Long longueurId,
			@SessionAttribute("userEmail") String userEmail
			) {
		ModelAndView modelView = new ModelAndView();
		Longueur longueur = longueurService.findByid(longueurId);
		Long voieId = longueur.getVoie().getVoie_id();
		Voie voie = voieService.findById(voieId);
		Long secteurId = voie.getSecteur().getSecteur_id();
		Secteur secteur = secteurService.findById(secteurId);
		Long spotId = secteur.getSpot().getSpot_id();
		Spot spot = spotService.findById(spotId);
		if(spot.getUser().getEmail().equals(userEmail)){
			modelView.addObject("spot_name", spot.getSpot_name());
			modelView.addObject("spot_id", spot.getSpot_id());
			modelView.addObject("spot_lieu", spot.getSpot_lieu());
			modelView.addObject("secteur_name", secteur.getSecteur_name() );
			modelView.addObject("secteur_id", secteur.getSecteur_id());
			modelView.addObject("userThatCreateTheSpot", spot.getUser().getEmail());
			modelView.addObject("void_id", voieId);
			modelView.addObject("voie_name", voie.getVoie_name());
			modelView.addObject("voie_cotation", voie.getVoie_cotation());
			modelView.addObject("longueur_id", longueurId);
			modelView.addObject("longueur_name", longueur.getLongueur_name());
			modelView.addObject("longueur_cotation", longueur.getLongueur_cotation());
			modelView.setViewName("spot/changelongueur");
		}
		else {
			modelView.setViewName("errors/access_denied");
		}
		return modelView;
	}
	
	
	@RequestMapping(value={"/changelongueur"}, method=RequestMethod.POST)
	public ModelAndView changeVoierPost(@Valid Longueur longueur,  BindingResult bindingResult
			) {
		ModelAndView modelView = new ModelAndView();
		Longueur longueurUpdate = longueurRepository.getOne(longueur.getLongueur_id());
		longueurUpdate.setLongueur_name(longueur.getLongueur_name());
		longueurUpdate.setLongueur_cotation(longueur.getLongueur_cotation());
		longueurRepository.save(longueurUpdate);
		Long voidId = longueurUpdate.getVoie().getVoie_id();
		modelView.setViewName("redirect:/listelongueur?id="+voidId);
		return modelView;
	}
	

	
}
