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
	public ModelAndView formGet(@RequestParam(name="id") Long secteurId,
			@RequestParam(name="errorname", defaultValue="") String errorname,
			@RequestParam(name="errorcotation", defaultValue="") String errorcotation
			) {
		ModelAndView modelView = new ModelAndView();
		if(errorname.equals("error_voie_name")) {
			modelView.addObject("error_voie_name", "error");
		}
		if(errorname.equals("error_voie_name_already_taken")){
			modelView.addObject("error_voie_name_already_taken", "error");
		}
		if(errorcotation.equals("error_voie_cotation")) {
			modelView.addObject("error_voie_cotation", "error");
		}
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
		int errorDetected = 0;
		C:for(int i = 0; i < voies.size(); i++) {
			matchesWithSizeList++;
			if(voies.get(i).getVoie_name() == voie.getVoie_name()) {
				bindingResult.rejectValue("voiename","error.voie","The voie name already exists!");
				errorDetected++;
				break C;
			}
		}
		if(voie.getVoie_name().length() > 30 || voie.getVoie_name().length() < 3) {
			if(errorDetected == 1) {
				modelView.addObject("errorname", "error_voie_name_already_taken");
			}
			else {
			modelView.addObject("errorname", "error_voie_name");
			}
			errorDetected++;
		}


		if(cotationVerification(voie.getVoie_cotation()) == false) {	
			modelView.addObject("errorcotation", "error_voie_cotation");
			errorDetected++;
		}

		if(errorDetected != 0) {
			modelView.setViewName("redirect:/createvoie?id="+secteurId);
		}
		if(errorDetected == 0) {
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
			@SessionAttribute("userEmail") String userEmail,
			@RequestParam(name="errorname", defaultValue="") String errorname,
			@RequestParam(name="errorcotation", defaultValue="") String errorcotation
			) {
		ModelAndView modelView = new ModelAndView();
		if(errorname.equals("error_voie_name")) {
			modelView.addObject("error_voie_name", "error");
		}
		if(errorname.equals("error_voie_name_already_taken")){
			modelView.addObject("error_voie_name_already_taken", "error");
		}
		if(errorcotation.equals("error_voie_cotation")) {
			modelView.addObject("error_voie_cotation", "error");
		}
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
		int errorDetected = 0;
		if(voie.getVoie_name().length() > 30 || voie.getVoie_name().length() < 3) {
			modelView.addObject("errorname", "error_voie_name");		
			errorDetected++;
		}
		if(cotationVerification(voie.getVoie_cotation()) == false) {	
			modelView.addObject("errorcotation", "error_voie_cotation");
			errorDetected++;
		}

		
		
		Voie voidUpdate = voieRepository.getOne(voie.getVoie_id());
		Voie voieResearchByName = voieRepository.findByVoie_name(voie.getVoie_name());
		Long secteurId = voidUpdate.getSecteur().getSecteur_id();
		
		if(errorDetected != 0) {
			modelView.setViewName("redirect:/changevoie?id="+voie.getVoie_id());
		}
		
		if(errorDetected == 0) {
			Long idVerificationVoieResearched = (long) -1;
			if(voieResearchByName != null) {
				idVerificationVoieResearched = voieResearchByName.getVoie_id();
			}
			Long idVerificationVoierUpdated = voie.getVoie_id();
			// if both id corresponds then the name hasn't been changed but other informations may have been
			if(voieResearchByName == null || idVerificationVoieResearched.equals(idVerificationVoierUpdated)) {
				voidUpdate.setVoie_name(voie.getVoie_name());
				voidUpdate.setVoie_cotation(voie.getVoie_cotation());
				voieRepository.save(voidUpdate);
				modelView.setViewName("redirect:/listevoie?id="+secteurId);
			}	
		}
		return modelView;
	}
	
	
	
	
	
	public Long findIdOfSpotWithVoieId(Long voieId) {
		Voie voie = voieService.findById(voieId);
		Long secteurId = voie.getSecteur().getSecteur_id();
		Secteur secteur = secteurService.findById(secteurId);
		return secteur.getSpot().getSpot_id();
	}
	

	
	/* Required:
	[1-4]{1}|[5a]{2}|[5b]{2}|[5c]{2}|
	[6a]{2}|[6a+]{3}|[6b]{2}|[6b+]{3}|[6c]{2}|[6c+]{3}|
	[7a]{2}|[7a+]{3}|[7b]{2}|[7b+]{3}|[7c]{2}|[7c+]{3}|
	[8a]{2}|[8a+]{3}|[8b]{2}|[8b+]{3}|[8c]{2}|[8c+]{3}|
	[9a]{2}|[9a+]{3}|[9b]{2}|[9b+]{3}|[9c]{2}
	*/
	public boolean cotationVerification(String cotationInput) {
		if(cotationInput.equals("1") || cotationInput.equals("2") || cotationInput.equals("3") || cotationInput.equals("4")
				|| cotationInput.equals("5a") || cotationInput.equals("5b")|| cotationInput.equals("5c")		
				|| cotationInput.equals("6a") || cotationInput.equals("6a+")|| cotationInput.equals("6b")	
				|| cotationInput.equals("6b+")|| cotationInput.equals("6c") || cotationInput.equals("6c+")	
				|| cotationInput.equals("7a") || cotationInput.equals("7a+")|| cotationInput.equals("7b")	
				|| cotationInput.equals("7b+")|| cotationInput.equals("7c") || cotationInput.equals("7c+")
				|| cotationInput.equals("8a") || cotationInput.equals("8a+")|| cotationInput.equals("8b")	
				|| cotationInput.equals("8b+")|| cotationInput.equals("8c") || cotationInput.equals("8c+")	
				|| cotationInput.equals("9a") || cotationInput.equals("9a+")|| cotationInput.equals("9b")	
				|| cotationInput.equals("9b+")|| cotationInput.equals("9c")){
					return true;
				}
		else {
			return false;
		}

	}
	
	
}
