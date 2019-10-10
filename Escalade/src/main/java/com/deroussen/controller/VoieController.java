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

import com.deroussen.dao.VoieRepository;
import com.deroussen.entities.Secteur;
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
	public ModelAndView formGet(Model model, 
			@RequestParam(name="spotId") Long spotId,
			@RequestParam(name="spotname") String spotname,
			@RequestParam(name="madeByUser") String madeByUser,
			@RequestParam(name="is_equipped") boolean is_equipped,
			@RequestParam(name="is_official") boolean is_official,
			@RequestParam(name="secteurname") String secteurname,
			@RequestParam(name="secteurId") Long secteurId			
			) {
		ModelAndView modelView = new ModelAndView();
		modelView.addObject("spotId", spotId);
		modelView.addObject("spotname", spotname);
		modelView.addObject("madeByUser", madeByUser);
		modelView.addObject("is_equipped", is_equipped);
		modelView.addObject("is_official", is_official);
		modelView.addObject("secteurname", secteurname);
		modelView.addObject("secteurId", secteurId);
		modelView.setViewName("spot/createvoie");
		return modelView;
	}
	
	@RequestMapping(value={"/createvoie"}, method=RequestMethod.POST)
	public ModelAndView formPost(@Valid Voie voie, BindingResult bindingResult, 
			@RequestParam(name="spotId") Long spotId,
			@RequestParam(name="spotname") String spotname,
			@RequestParam(name="madeByUser") String madeByUser,
			@RequestParam(name="is_equipped") boolean is_equipped,
			@RequestParam(name="is_official") boolean is_official,
			@RequestParam(name="secteurname") String secteurname,
			@RequestParam(name="secteurId") Long secteurId,
			@RequestParam(name="voiename") String voiename,
			@RequestParam(name="voiecotation") String voiecotation) {
		
		ModelAndView modelView = new ModelAndView();
		List <Voie> voies = voieService.findBySecteurId(secteurId);
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
			modelView.setViewName("redirect:/createvoie?spotname="+spotname+"&spotId="+spotId+"&madeByUser="+madeByUser+"&is_equipped="+is_equipped+"&is_official="+is_official
					+"&secteurname="+secteurname+"&secteurId="+secteurId);
		}
		if (sizeList == matchesWithSizeList) {
			voie.setSecteur(secteurService.findById(secteurId));
			voieService.saveVoie(voie);
			modelView.setViewName("redirect:/createlongueur?spotname="+spotname+"&spotId="+spotId+"&madeByUser="+madeByUser+"&is_equipped="+is_equipped+"&is_official="+is_official
			+"&secteurname="+secteurname+"&secteurId="+secteurId+"&voiename="+voiename+"&voieId="+voie.getId()+"&voiecotation="+voiecotation);
		}		
		return modelView;
	}
	
	

	
	
	//Liste des voies avec parametres (arrive avec un secteur conservé)
	@RequestMapping(value={"/listevoie"}, method=RequestMethod.GET)
	public ModelAndView listeVoie(
			@RequestParam(name="spotname") String spotname,
			@RequestParam(name="spotId") Long spotId,
			@RequestParam(name="secteurname") String secteurname,
			@RequestParam(name="secteurId") Long secteurId,
			@RequestParam(name="userThatCreateTheSpot") String userThatCreateTheSpot,
			@RequestParam(name="page", defaultValue= "0") int page,
			@RequestParam(name="motCle", defaultValue= "") String mc) {
		ModelAndView modelView = new ModelAndView();
		Page <Voie> voies = voieService.findByVoienameContainsFromSecteurId(secteurId, mc, PageRequest.of(page, 10));
		// Page <Voie> voies = voieRepository.findByVoienameContains(mc, PageRequest.of(page, 10));
		modelView.addObject("voielist", voies.getContent());
		modelView.addObject("pages",new int[voies.getTotalPages()]);
		modelView.addObject("spotname", spotname);
		modelView.addObject("spotId", spotId);
		modelView.addObject("secteurname", secteurname);
		modelView.addObject("secteurId", secteurId);
		modelView.addObject("userThatCreateTheSpot", userThatCreateTheSpot);
		modelView.addObject("currentPage",page);
		modelView.addObject("motCle", mc);
		modelView.setViewName("/spot/listevoie");
		return modelView;
	}
	
	//Liste de toute les voies (seuls paramètres = page et motclef)
	@RequestMapping(value={"/listevoies"}, method=RequestMethod.GET)
	public ModelAndView listeVoie(@RequestParam(name="page", defaultValue= "0") int page,
			@RequestParam(name="motCle", defaultValue= "") String mc) {
		ModelAndView modelView = new ModelAndView();
		Page <Voie> voies = voieRepository.findByVoienameContains(mc, PageRequest.of(page, 10));
		modelView.addObject("voielist", voies.getContent());
		modelView.addObject("pages",new int[voies.getTotalPages()]);
		modelView.addObject("currentPage",page);
		modelView.setViewName("/spot/listevoie");
		return modelView;	
	}
			
	@GetMapping("/deletevoie")
	public ModelAndView delete(String motCle, int page,
			@RequestParam(name="id") Long id,
			@RequestParam(name="spotId") Long spotId,
			@SessionAttribute("userEmail") String userEmail
			) {
		ModelAndView modelView = new ModelAndView();
		if(spotService.findById(spotId).getUser().getEmail().equals(userEmail)){
			voieRepository.deleteById(id);
			modelView.setViewName("redirect:/listevoie?page="+page+"&motCle="+motCle);
		}
		else {
			modelView.setViewName("errors/access_denied");
		}
		return modelView;
	}

	
	
	@RequestMapping(value={"/changevoie"}, method=RequestMethod.GET)
	public ModelAndView changeVoieGet(@RequestParam(name="spotId") Long spotId,
			@RequestParam(name="spotname") String spotname,
			@RequestParam(name="secteurId") Long secteurId,
			@RequestParam(name="secteurname") String secteurname,
			@SessionAttribute("userEmail") String userEmail,
			@RequestParam(name="voieId") Long voieId,
			@RequestParam(name="voiecotation") String voiecotation,
			@RequestParam(name="voiename") String voiename
			) {
		ModelAndView modelView = new ModelAndView();
		if(spotService.findById(spotId).getUser().getEmail().equals(userEmail)){
			modelView.addObject("spotId", spotId);
			modelView.addObject("spotname", spotname);
			modelView.addObject("secteurname", secteurname);
			modelView.addObject("secteurId",secteurId);
			modelView.addObject("voieId", voieId);
			modelView.addObject("voiecotation", voiecotation);
			modelView.addObject("voiename", voiename);
			modelView.setViewName("spot/changevoie");
		}
		else {
			modelView.setViewName("errors/access_denied");
		}
		return modelView;
	}
	
	
	@RequestMapping(value={"/changevoie"}, method=RequestMethod.POST)
	public ModelAndView changeVoierPost(@RequestParam(name="spotId") Long spotId,
			@RequestParam(name="spotname") String spotname,
			@RequestParam(name="secteurname") String secteurname,
			@RequestParam(name="secteurId") Long secteurId,
			@RequestParam(name="voieId") Long voieId,
			@RequestParam(name="voiecotation") String voiecotation,
			@RequestParam(name="voiename") String voiename
			) {
		ModelAndView modelView = new ModelAndView();
		Voie voidUpdate = voieRepository.getOne(voieId);
		voidUpdate.setVoiename(voiename);
		voidUpdate.setVoiecotation(voiecotation);
		voieRepository.save(voidUpdate);
		modelView.setViewName("spot/listespot");
		return modelView;
	}
	

}
