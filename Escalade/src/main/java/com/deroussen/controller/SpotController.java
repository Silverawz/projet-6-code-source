package com.deroussen.controller;


import javax.validation.Valid;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import com.deroussen.dao.SpotRepository;
import com.deroussen.entities.Spot;
import com.deroussen.service.SpotService;
import com.deroussen.service.UserService;


@Controller
@SessionAttributes("userEmail")
public class SpotController {
	
	@Autowired
	private SpotService spotService;
	@Autowired
	private SpotRepository spotRepository;
	@Autowired
	private UserService userService;

	
	
	@RequestMapping(value={"/createspot"}, method=RequestMethod.GET)
	public ModelAndView formGet() {
		ModelAndView modelView = new ModelAndView();
		modelView.setViewName("spot/createspot");
		return modelView;
	}
	
	@RequestMapping(value={"/createspot"}, method=RequestMethod.POST)
	public ModelAndView formPost(@Valid Spot spot, BindingResult bindingResult,
			@SessionAttribute("userEmail") String userEmail, 
			@RequestParam(name="is_equipped") String is_equipped) {
		ModelAndView modelView = new ModelAndView();
		Spot spotExists = spotService.findSpotByName(spot.getSpotname());
		if(spotExists != null) {
			bindingResult.rejectValue("spotname","This spotname already exists!"); //  TODO
		}
		if(bindingResult.hasErrors()) {
			modelView.setViewName("/spot/createspot"); 
		}
		if(spotExists == null) {
			if(is_equipped.equals("oui")) {
				spot.setIs_equipped(true);
			}
			else {
				spot.setIs_equipped(false);
			}
			spot.setUser(userService.findUserByEmail(userEmail));
			spotService.saveSpot(spot);
			// model.addObject("msg","The spot has been created successfully!"); // TODO
			modelView.setViewName("redirect:/createsecteur?spotname="+spot.getSpotname()+"&spotId="+spot.getId()+"&madeByUser="+userEmail+"&is_equipped="+spot.isIs_equipped()+"&is_official=false");
		}	
		return modelView;
	}
	
	@RequestMapping(value={"/listespot"}, method=RequestMethod.GET)
	public ModelAndView listeSpot(
			@RequestParam(name="page", defaultValue= "0") int page,
			@RequestParam(name="motCle", defaultValue= "") String mc) {
		ModelAndView modelView = new ModelAndView();
		Page <Spot> spots = spotRepository.findBySpotnameContains(mc, PageRequest.of(page, 10));
		modelView.addObject("spotlist", spots.getContent());
		modelView.addObject("pages",new int[spots.getTotalPages()]);
		modelView.addObject("currentPage",page);
		modelView.addObject("motCle", mc);
		modelView.setViewName("/spot/listespot");
		return modelView;
	}
	
	@RequestMapping(value={"/changespot"}, method=RequestMethod.GET)
	public ModelAndView changeSpotGet(@RequestParam(name="spotId") Long spotId) {
		ModelAndView modelView = new ModelAndView();
		Spot spot = spotRepository.getOne(spotId);
		modelView.addObject("spotId", spotId);
		modelView.addObject("spotname", spot.getSpotname());
		modelView.addObject("is_equipped", spot.isIs_equipped());
		modelView.addObject("is_official", spot.isIs_official());
		modelView.setViewName("spot/changespot");
		return modelView;
	}
	
	
	@RequestMapping(value={"/changespot"}, method=RequestMethod.POST)
	public ModelAndView changeSpotPost(@Valid Spot spot, BindingResult bindingResult,
			@RequestParam(name="spotId") Long spotId,
			@RequestParam(name="spotname") String spotname,
			@RequestParam(name="is_equipped") String is_equipped,
			@RequestParam(name="is_official") String is_official
			) {
		ModelAndView modelView = new ModelAndView();
		Spot spotUpdate = spotRepository.getOne(spotId);
		spotUpdate.setSpotname(spotname);
		if(is_equipped.equals("oui")) {
			spotUpdate.setIs_equipped(true);
		}
		else {
			spotUpdate.setIs_equipped(false);
		}
		if(is_official.equals("oui")) {
			spotUpdate.setIs_official(true);
		}
		else {
			spotUpdate.setIs_official(false);
		}
		spotRepository.save(spotUpdate);
		modelView.setViewName("spot/listespot");
		return modelView;
	}
	
	
	@RequestMapping(value={"/deletespot"}, method=RequestMethod.GET)
	public String delete(Long id, String motCle, int page) {
		spotRepository.deleteById(id);
		return "redirect:/listespot?page="+page+"&motCle="+motCle;
	}
	
}
