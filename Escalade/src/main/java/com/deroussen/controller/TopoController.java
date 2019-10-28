package com.deroussen.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.deroussen.dao.TopoRepository;
import com.deroussen.entities.Topo;
import com.deroussen.entities.User;
import com.deroussen.service.TopoService;
import com.deroussen.service.UserService;



@Controller
@SessionAttributes("userEmail")
public class TopoController {
	
	
	@Autowired
	private TopoService topoService;
	@Autowired
	private TopoRepository topoRepository;
	@Autowired
	private UserService userService;
	
	@RequestMapping(value={"/listetopo"}, method=RequestMethod.GET)
	public ModelAndView getTopoList(@SessionAttribute("userEmail") String userEmail) {
		ModelAndView modelView = new ModelAndView();
		List <Topo> topos = topoRepository.findAll();
		List <String> alreadyRequest = new ArrayList<>();
		for (Topo topo : topos) {
			if(topo.getUsersAskingForReservation().contains(userService.findUserByEmail(userEmail))) {
				alreadyRequest.add("1");
			}
			else {
				alreadyRequest.add(null);
			}
		}
		modelView.addObject("alreadyRequest", alreadyRequest);
		modelView.addObject("topolist", topos);
		modelView.setViewName("topo/listetopo");
		return modelView;
	}
	
	@RequestMapping(value={"/createtopo"}, method=RequestMethod.GET)
	public ModelAndView formGetTopoCreate() {
		ModelAndView modelView = new ModelAndView();
		modelView.setViewName("topo/createtopo");
		return modelView;
	}
	
	@RequestMapping(value={"/createtopo"}, method=RequestMethod.POST)
	public ModelAndView formPostTopoCreate(@Valid Topo topo, BindingResult bindingResult,
			@SessionAttribute("userEmail") String userEmail) {
		ModelAndView modelView = new ModelAndView();
		Topo topoExists = topoService.findByTopoName(topo.getTopo_name());
		if(topoExists != null) {
			bindingResult.rejectValue("toponame","This toponame already exists!"); //  TODO
		}
		if(bindingResult.hasErrors()) {
			modelView.setViewName("/topo/createtopo"); 
		}
		if(topoExists == null) {
			if(topo.isIs_available() == true) {
				topo.setIs_available(true);
			}
			else {
				topo.setIs_available(false);
			}
		topo.setUserOwnerOfTheTopo(userService.findUserByEmail(userEmail));	
		topoService.saveTopo(topo);	
		modelView.setViewName("topo/createtopo");
		}
		return modelView;
	}
	
	//delete when using the public list returning view to public list
	@RequestMapping(value={"/deletetopo"}, method=RequestMethod.GET)
	public ModelAndView getTopoDelete(@RequestParam(name="id") Long topoId,
			@SessionAttribute("userEmail") String userEmail) {
		ModelAndView modelView = new ModelAndView();
		if(topoService.findById(topoId).getUserOwnerOfTheTopo().getEmail().equals(userEmail)) {
			topoRepository.deleteById(topoId);	
			modelView.setViewName("redirect:/listetopo");
		}
		else {
			modelView.setViewName("errors/access_denied");
		}
		return modelView;
	}
	
	//delete when using the private list returning view to private list of user
	@RequestMapping(value={"/deletetopopersonal"}, method=RequestMethod.GET)
	public ModelAndView getTopoPersonalDelete(@RequestParam(name="id") Long topoId,
			@SessionAttribute("userEmail") String userEmail) {
		ModelAndView modelView = new ModelAndView();
		if(topoService.findById(topoId).getUserOwnerOfTheTopo().getEmail().equals(userEmail)) {
			topoRepository.deleteById(topoId);	
			modelView.setViewName("redirect:/userlistetopo");
		}
		else {
			modelView.setViewName("errors/access_denied");
		}
		return modelView;
	}
	
	//change when using the public list
	@RequestMapping(value={"/changetopo"}, method=RequestMethod.GET)
	public ModelAndView changeTopotGet(@RequestParam(name="id") Long topoId
			) {
		ModelAndView modelView = new ModelAndView();
		Topo topo = topoRepository.getOne(topoId);
		modelView.addObject("topo_id", topoId);
		modelView.addObject("topo_name", topo.getTopo_name());
		modelView.addObject("topo_lieu", topo.getTopo_lieu());
		modelView.addObject("topo_description", topo.getTopo_description());
		modelView.addObject("topo_available", topo.isIs_available());
		modelView.addObject("topo_madeByUser", topo.getUserOwnerOfTheTopo().getFirstname());
		modelView.addObject("topo_madeByUser_email", topo.getUserOwnerOfTheTopo().getEmail());
		modelView.addObject("topo_date_parution", topo.getTopo_date_parution());
		modelView.setViewName("topo/changetopo");
		return modelView;
	}
	
	//change when using the private list in personal space
	@RequestMapping(value={"/changetopopersonal"}, method=RequestMethod.GET)
	public ModelAndView changeTopoPersonaltGet(@RequestParam(name="id") Long topoId
			) {
		ModelAndView modelView = new ModelAndView();
		Topo topo = topoRepository.getOne(topoId);
		modelView.addObject("topo_id", topoId);
		modelView.addObject("topo_name", topo.getTopo_name());
		modelView.addObject("topo_lieu", topo.getTopo_lieu());
		modelView.addObject("topo_description", topo.getTopo_description());
		modelView.addObject("topo_available", topo.isIs_available());
		modelView.addObject("topo_madeByUser", topo.getUserOwnerOfTheTopo().getFirstname());
		modelView.addObject("topo_madeByUser_email", topo.getUserOwnerOfTheTopo().getEmail());
		modelView.addObject("topo_date_parution", topo.getTopo_date_parution());
		modelView.setViewName("topo/changetopopersonal");
		return modelView;
	}
	
	//change when using the public list
	@RequestMapping(value={"/changetopo"}, method=RequestMethod.POST)
	public ModelAndView changeTopoPost(@Valid Topo topo, BindingResult bindingResult
			) {
		ModelAndView modelView = new ModelAndView();
		Topo topoUpdate = topoRepository.getOne(topo.getTopo_id());	
		Topo topoResearchedByName = topoService.findByTopoName(topo.getTopo_name());
		Long idVerificationTopoResearched = (long) -1;
		if(topoResearchedByName != null) {
			idVerificationTopoResearched = topoResearchedByName.getTopo_id();
		}
		Long idVerificationTopoUpdated = topo.getTopo_id();
		// if both id corresponds then the name hasn't been changed but other informations may have been
		if(topoResearchedByName == null || idVerificationTopoResearched.equals(idVerificationTopoUpdated)) { 
			topoUpdate.setTopo_name(topo.getTopo_name());
			topoUpdate.setTopo_lieu(topo.getTopo_lieu());
			topoUpdate.setTopo_description(topo.getTopo_description());		
			topoUpdate.setIs_available(topo.isIs_available());
			topoUpdate.setTopo_date_parution(topo.getTopo_date_parution());
			topoRepository.save(topoUpdate);
			modelView.setViewName("redirect:/listetopo");	
		}
		else {
			modelView.setViewName("redirect:/listetopo");	
		}
		return modelView;
	}
	
	//change when using the private list in personal space
	@RequestMapping(value={"/changetopopersonal"}, method=RequestMethod.POST)
	public ModelAndView changeTopoPersonalPost(@Valid Topo topo, BindingResult bindingResult
			) {
		ModelAndView modelView = new ModelAndView();
		Topo topoUpdate = topoRepository.getOne(topo.getTopo_id());	
		Topo topoResearchedByName = topoService.findByTopoName(topo.getTopo_name());
		Long idVerificationTopoResearched = (long) -1;
		if(topoResearchedByName != null) {
			idVerificationTopoResearched = topoResearchedByName.getTopo_id();
		}
		Long idVerificationTopoUpdated = topo.getTopo_id();
		// if both id corresponds then the name hasn't been changed but other informations may have been
		if(topoResearchedByName == null || idVerificationTopoResearched.equals(idVerificationTopoUpdated)) { 
			topoUpdate.setTopo_name(topo.getTopo_name());
			topoUpdate.setTopo_lieu(topo.getTopo_lieu());
			topoUpdate.setTopo_description(topo.getTopo_description());		
			topoUpdate.setIs_available(topo.isIs_available());
			topoUpdate.setTopo_date_parution(topo.getTopo_date_parution());
			topoRepository.save(topoUpdate);
			modelView.setViewName("redirect:/userlistetopo");	
		}
		else {
			modelView.setViewName("redirect:/userlistetopo");	
		}
		return modelView;
	}
	
	
	
	@RequestMapping(value={"/demandereservation"}, method=RequestMethod.GET)
	public ModelAndView getReservation(@RequestParam(name="id") Long topoId,
			@SessionAttribute("userEmail") String userEmail) {
		ModelAndView modelView = new ModelAndView();
		
		Topo topo = topoRepository.findByid(topoId);
		Set <User> update = topo.getUsersAskingForReservation();
		update.add(userService.findUserByEmail(userEmail));
		topo.setUsersAskingForReservation(update);
		topoRepository.save(topo);
		modelView.setViewName("redirect:/listetopo");
		/*
		if(!userId.equals(null)) {
			topoService.insertReservation(topoId, userId);
			modelView.setViewName("redirect:/listetopo");
		}		
		else {
			modelView.setViewName("topo/reservation");	
		}
		*/
		return modelView;
	}
	
	
	
	
	
}
