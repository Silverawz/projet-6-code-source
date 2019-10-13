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

import com.deroussen.dao.SecteurRepository;
import com.deroussen.entities.Secteur;
import com.deroussen.entities.Spot;
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
			@RequestParam(name="id") Long spotId
			) {
		ModelAndView modelView = new ModelAndView();	
		Spot spot = spotService.findById(spotId);
		modelView.addObject("madeByUser", spot.getUser().getEmail());
		modelView.addObject("spot_id", spot.getSpot_id());
		modelView.addObject("spot_name", spot.getSpot_name());
		modelView.addObject("is_equipped", spot.isIs_equipped());
		modelView.addObject("is_official", spot.isIs_official());
		modelView.setViewName("spot/createsecteur");
		return modelView;
	}
	
	@RequestMapping(value={"/createsecteur"}, method=RequestMethod.POST)
	public ModelAndView formPost(@Valid Secteur secteur, BindingResult bindingResult,
			@RequestParam(name="spot_id") Long spotId
			) {
		ModelAndView modelView = new ModelAndView();	
		List <Secteur> secteurs = secteurService.findBySpotId(spotId);
		int numberOfSecteur = secteurs.size();
		int numberThatMustMatchesWithNumberOfSecteurs = 0;
		C:for(int i = 0; i < secteurs.size(); i++) {
			numberThatMustMatchesWithNumberOfSecteurs++;
			if(secteurs.get(i).getSecteur_name() == secteur.getSecteur_name()) {
				bindingResult.rejectValue("secteurname","error.secteur","The secteur name already exists!");
				break C;
			}
		}
		if(bindingResult.hasErrors()) {
			modelView.setViewName("redirect:/createsecteur?id="+spotId);
		}
		if (numberOfSecteur == numberThatMustMatchesWithNumberOfSecteurs) {
			secteur.setSpot(spotService.findById(spotId));
			secteurService.saveSecteur(secteur);
			modelView.setViewName("redirect:/createvoie?id="+secteur.getSecteur_id());
		}		
		return modelView;
	}
	
	//Liste des secteurs with spot id
	@RequestMapping(value={"/listesecteur"}, method=RequestMethod.GET)
	public ModelAndView listeSecteur(
			@RequestParam(name="id") Long spotId,
			@RequestParam(name="page", defaultValue= "0") int page,
			@RequestParam(name="motCle", defaultValue= "") String mc
			) {
		ModelAndView modelView = new ModelAndView();
		Page <Secteur> secteurs = secteurService.findBySecteurnameContainsFromSpotId(spotId, mc, PageRequest.of(page, 10));
		Spot spot = spotService.findById(spotId);
		modelView.addObject("secteurlist", secteurs.getContent());
		modelView.addObject("pages",new int[secteurs.getTotalPages()]);
		modelView.addObject("currentPage",page);
		modelView.addObject("spot_id", spotId);
		modelView.addObject("motCle", mc);
		modelView.addObject("spot_name", spot.getSpot_name());
		modelView.addObject("userThatCreateTheSpot", spot.getUser().getEmail());
		modelView.setViewName("/spot/listesecteur");
		return modelView;
	}
	
	
	@GetMapping("/deletesecteur")
	public ModelAndView delete(String motCle, int page,
			@RequestParam(name="id") Long secteurId,
			@SessionAttribute("userEmail") String userEmail
			) {
		Secteur secteur = secteurService.findById(secteurId);
		Long spotId = secteur.getSpot().getSpot_id();
		ModelAndView modelView = new ModelAndView();	
		if(findUserMailCreatorWithSpotId(spotId).equals(userEmail)) {
			secteurRepository.deleteById(secteurId);	
			modelView.setViewName("redirect:/listesecteur?id="+spotId+"&page="+page+"&motCle="+motCle);
		}
		else {
			modelView.setViewName("errors/access_denied");
		}
		return modelView;
	}
	
	
	
	@RequestMapping(value={"/changesecteur"}, method=RequestMethod.GET)
	public ModelAndView changeSecteurGet(@RequestParam(name="id") Long secteurId,
			@SessionAttribute("userEmail") String userEmail
			) {
		ModelAndView modelView = new ModelAndView();	
		Secteur secteur = secteurService.findById(secteurId);
		Long spotId = secteur.getSpot().getSpot_id();
		Spot spot = spotService.findById(spotId);
		if(findUserMailCreatorWithSpotId(spotId).equals(userEmail)) {
			modelView.addObject("spot_id", spot.getSpot_id());
			modelView.addObject("spot_name", spot.getSpot_name());
			modelView.addObject("secteur_id", secteurId);
			modelView.addObject("secteur_name", secteur.getSecteur_name());
			modelView.setViewName("spot/changesecteur");
		}
		else {
			modelView.setViewName("errors/access_denied");
		}
		return modelView;
	}
	
	@RequestMapping(value={"/changesecteur"}, method=RequestMethod.POST)
	public ModelAndView changeSecteurPost(@Valid Secteur secteur, BindingResult bindingResult
			) {
		ModelAndView modelView = new ModelAndView();
		Secteur secteurUpdate = secteurRepository.getOne(secteur.getSecteur_id());
		secteurRepository.save(secteur);
		Long spotId = secteurUpdate.getSpot().getSpot_id();	
		modelView.setViewName("redirect:/listesecteur?id="+spotId);
		return modelView;
	}
	
	public Long findIdOfSpotWithSecteurId(Long secteurId) {
		return secteurService.findById(secteurId).getSpot().getSpot_id();
	}
	
	public String findUserMailCreatorWithSpotId(Long spotId) {
		return spotService.findById(spotId).getUser().getEmail();
	}
}
