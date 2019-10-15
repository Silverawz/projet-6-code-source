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

import com.deroussen.dao.VoieRepository;
import com.deroussen.entities.Secteur;
import com.deroussen.entities.Spot;
import com.deroussen.entities.Voie;
import com.deroussen.service.SecteurService;
import com.deroussen.service.SpotService;
import com.deroussen.service.VoieService;


@Controller
@SessionAttributes("userEmail")
public class VoieController {
	
	@Autowired
	private VoieService voieService;
	@Autowired
	private VoieRepository voieRepository;
	@Autowired
	private SecteurService secteurService;
	@Autowired
	private SpotService spotService;
	
	
	@RequestMapping(value={"/createvoie"}, method=RequestMethod.GET)
	public ModelAndView formGet(@RequestParam(name="id") Long secteurId	
			) {
		ModelAndView modelView = new ModelAndView();
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
		modelView.setViewName("spot/createvoie");
		return modelView;
	}
	

	@RequestMapping(value={"/createvoie"}, method=RequestMethod.POST)
	public ModelAndView formPost(@Valid Voie voie, BindingResult bindingResult,
			@RequestParam(name="secteur_id") Long secteurId,
			@RequestParam(name="continueToCreateLongueur") boolean continueToCreateLongueur
			) {
		ModelAndView modelView = new ModelAndView();
		List <Voie> voies = voieService.findBySecteurId(secteurId);
		int sizeList = voies.size();
		int matchesWithSizeList = 0;
		C:for(int i = 0; i < voies.size(); i++) {
			matchesWithSizeList++;
			if(voies.get(i).getVoie_name() == voie.getVoie_name()) {
				bindingResult.rejectValue("voiename","error.voie","The voie name already exists!");
				break C;
			}
		}
		if(bindingResult.hasErrors()) {
			modelView.setViewName("redirect:/createvoie?id="+secteurId);
		}
		if (sizeList == matchesWithSizeList) {		 
			voie.setSecteur(secteurService.findById(secteurId));
			voieService.saveVoie(voie);
			if(continueToCreateLongueur == true) {
				modelView.setViewName("redirect:/createlongueur?id="+voie.getVoie_id());
			}
			else {
				modelView.setViewName("redirect:/listevoie?id="+secteurId);
			}
			
			
		}		
		return modelView;
	}
	
	

	

	@RequestMapping(value={"/listevoie"}, method=RequestMethod.GET)
	public ModelAndView listeVoie(@RequestParam(name="id") Long secteurId,
			@RequestParam(name="page", defaultValue= "0") int page,
			@RequestParam(name="motCle", defaultValue= "") String mc) {
		ModelAndView modelView = new ModelAndView();
		Page <Voie> voies = voieService.findByVoieContainsFromSecteurId(secteurId, mc, PageRequest.of(page, 10));
		Secteur secteur = secteurService.findById(secteurId);
		Long spotId = secteurService.findById(secteurId).getSpot().getSpot_id();
		Spot spot = spotService.findById(spotId);
		modelView.addObject("voielist", voies.getContent());
		modelView.addObject("pages",new int[voies.getTotalPages()]);
		modelView.addObject("currentPage",page);
		modelView.addObject("motCle", mc);
		modelView.addObject("userThatCreateTheSpot", spot.getUser().getEmail());
		modelView.addObject("spot_id", spot.getSpot_id());
		modelView.addObject("spot_lieu", spot.getSpot_lieu());
		modelView.addObject("spot_name", spot.getSpot_name());
		modelView.addObject("secteur_id", secteur.getSecteur_id());
		modelView.addObject("secteur_name", secteur.getSecteur_name());
		modelView.setViewName("/spot/listevoie");
		return modelView;
	}
		
	@GetMapping("/deletevoie")
	public ModelAndView delete(String motCle, int page,
			@RequestParam(name="id") Long voieId,
			@SessionAttribute("userEmail") String userEmail
			) {	
		ModelAndView modelView = new ModelAndView();
		Voie voie = voieService.findById(voieId);
		Long secteurId = voie.getSecteur().getSecteur_id();
		Secteur secteur = secteurService.findById(secteurId);
		Long spotId = secteur.getSpot().getSpot_id();;	
		Spot spot = spotService.findById(spotId);
		if(spot.getUser().getEmail().equals(userEmail)){
			voieRepository.deleteById(voieId);
			modelView.setViewName("redirect:/listevoie?id="+secteurId);
		}
		else {
			modelView.setViewName("errors/access_denied");
		}
		return modelView;
	}

	
	
	@RequestMapping(value={"/changevoie"}, method=RequestMethod.GET)
	public ModelAndView changeVoieGet(@RequestParam(name="id") Long voieId,
			@SessionAttribute("userEmail") String userEmail
			) {
		ModelAndView modelView = new ModelAndView();
		Voie voie = voieService.findById(voieId);
		Long secteurId = voie.getSecteur().getSecteur_id();
		Secteur secteur = secteurService.findById(secteurId);
		Long spotId = secteur.getSpot().getSpot_id();
		Spot spot = spotService.findById(spotId);
		System.out.println(voieId);
		if(spot.getUser().getEmail().equals(userEmail)){
			modelView.addObject("spot_name", spot.getSpot_name());
			modelView.addObject("spot_id", spot.getSpot_id());
			modelView.addObject("spot_lieu", spot.getSpot_lieu());
			modelView.addObject("secteur_name", secteur.getSecteur_name() );
			modelView.addObject("secteur_id", secteur.getSecteur_id());
			modelView.addObject("userThatCreateTheSpot", spot.getUser().getEmail());
			modelView.addObject("voie_id", voieId);
			modelView.addObject("voie_name", voie.getVoie_name());
			modelView.addObject("voie_cotation", voie.getVoie_cotation());
			modelView.setViewName("spot/changevoie");
		}
		else {
			modelView.setViewName("errors/access_denied");
		}
		return modelView;
	}
	
	
	@RequestMapping(value={"/changevoie"}, method=RequestMethod.POST)
	public ModelAndView changeVoierPost(@Valid Voie voie,  BindingResult bindingResult
			) {
		ModelAndView modelView = new ModelAndView();
		Voie voidUpdate = voieRepository.getOne(voie.getVoie_id());
		voidUpdate.setVoie_name(voie.getVoie_name());
		voidUpdate.setVoie_cotation(voie.getVoie_cotation());
		voieRepository.save(voidUpdate);
		Long secteurId = voidUpdate.getSecteur().getSecteur_id();
		modelView.setViewName("redirect:/listevoie?id="+secteurId);
		return modelView;
	}
	
	
	
	
	
	public Long findIdOfSpotWithVoieId(Long voieId) {
		Voie voie = voieService.findById(voieId);
		Long secteurId = voie.getSecteur().getSecteur_id();
		Secteur secteur = secteurService.findById(secteurId);
		return secteur.getSpot().getSpot_id();
	}
	

	
	
	
}
