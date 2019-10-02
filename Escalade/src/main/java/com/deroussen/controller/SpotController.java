package com.deroussen.controller;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import com.deroussen.dao.SpotRepository;
import com.deroussen.entities.Spot;
import com.deroussen.service.SpotService;


@Controller
@SessionAttributes("userName")
public class SpotController {
	
	@Autowired
	SpotService spotService;
	@Autowired
	SpotRepository spotRepository;
	

	
	@RequestMapping(value={"/createspot"}, method=RequestMethod.GET)
	public ModelAndView formGet() {
		ModelAndView model = new ModelAndView();
		model.setViewName("spot/createspot");
		return model;
	}
	
	@RequestMapping(value={"/createspot"}, method=RequestMethod.POST)
	public ModelAndView formPost(@Valid Spot spot, BindingResult bindingResult, @SessionAttribute("userName") String userName) {
		ModelAndView model = new ModelAndView();
		Spot spotExists = spotService.findSpotByName(spot.getSpotname());
		
		if(spotExists != null) {
			bindingResult.rejectValue("spotname","error.spot","The spot name already exists!");
		}
		if(bindingResult.hasErrors()) {
			model.setViewName("/spot/createspot");
		}
		else {
			spot.setMadeByUser(userName);
			spotService.saveSpot(spot);
			model.addObject("spot",  new Spot());	
			// model.setViewName("/spot/createsecteur");
			model.setViewName("redirect:/createSecteur?spotName="+spot.getSpotname());
		}	
		return model;
	}
	
	@RequestMapping(value={"/listespot"}, method=RequestMethod.GET)
	public ModelAndView listeSpot(Model model, 
			@RequestParam(name="page", defaultValue= "0") int page,
			@RequestParam(name="motCle", defaultValue= "") String mc) {
		ModelAndView modelView = new ModelAndView();
		Page <Spot> spots = spotRepository.findBySpotnameContains(mc, PageRequest.of(page, 10));
		model.addAttribute("spotlist", spots.getContent());
		model.addAttribute("pages",new int[spots.getTotalPages()]);
		model.addAttribute("currentPage",page);
		model.addAttribute("motCle", mc);
		modelView.addObject(model);
		modelView.setViewName("/spot/listespot");
		return modelView;
	}
	
	@GetMapping("/delete")
	public String delete(Long id, String motCle, int page) {
		spotRepository.deleteById(id);
		return "redirect:/listespot?page="+page+"&motCle="+motCle;
	}
}
