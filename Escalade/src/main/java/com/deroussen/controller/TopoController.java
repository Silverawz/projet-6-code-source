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
	public ModelAndView formGetTopoCreate(@RequestParam(name="errorname", defaultValue="") String errorname,
			@RequestParam(name="errorlieu", defaultValue="") String errorlieu,
			@RequestParam(name="errordescription", defaultValue="") String errordescription,
			@RequestParam(name="errordate", defaultValue="") String errordate) {
		ModelAndView modelView = new ModelAndView();
		if(errorname.equals("error_topo_name")) {
			modelView.addObject("error_topo_name", "error");
		}
		if(errorname.equals("error_topo_name_already_taken")) {
			modelView.addObject("error_topo_name_already_taken", "error");
		}
		if(errorlieu.equals("error_topo_lieu")){
			modelView.addObject("error_topo_lieu", "error");
		}
		if(errordescription.equals("error_topo_description")) {
			modelView.addObject("error_topo_description", "error");
		}
		if(errordate.equals("error_topo_date")) {
			modelView.addObject("error_topo_date", "error");
		}	
		modelView.setViewName("topo/createtopo");
		return modelView;
	}
	
	@RequestMapping(value={"/createtopo"}, method=RequestMethod.POST)
	public ModelAndView formPostTopoCreate(@Valid Topo topo, BindingResult bindingResult,
			@SessionAttribute("userEmail") String userEmail) {
		ModelAndView modelView = new ModelAndView();
		Topo topoExists = topoService.findByTopoName(topo.getTopo_name());
		int errorDetected = 0;
		if(topoExists != null) {
			bindingResult.rejectValue("toponame","This toponame already exists!");
			errorDetected++;
		}
		if(errorDetected != 0) {
			modelView.addObject("errorname", "error_topo_name_already_taken");
		}
		if(topo.getTopo_name().length() > 30 || topo.getTopo_name().length() < 3) {
			modelView.addObject("errorname", "error_topo_name");
			errorDetected++;
		}
		if(topo.getTopo_lieu().length() > 30 || topo.getTopo_lieu().length() < 3) {
			modelView.addObject("errorlieu", "error_topo_lieu");
			errorDetected++;
		}
		if(topo.getTopo_description().length() > 255 || topo.getTopo_description().length() < 10) {
			modelView.addObject("errordescription", "error_topo_description");
			errorDetected++;
		}
		if(dateVerification(topo.getTopo_date_parution()) == false) {
			modelView.addObject("errordate", "error_topo_date");
			errorDetected++;
		}
		if(errorDetected != 0) {
			modelView.setViewName("redirect:/createtopo");
		}
		
		if(errorDetected == 0) {
			if(topoExists == null) {
				if(topo.isIs_available() == true) {
					topo.setIs_available(true);
				}
				else {
					topo.setIs_available(false);
				}
			topo.setUserOwnerOfTheTopo(userService.findUserByEmail(userEmail));	
			topoService.saveTopo(topo);	
			modelView.setViewName("redirect:/listetopo");
			}
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
	public ModelAndView changeTopotGet(@RequestParam(name="id") Long topoId,
			@RequestParam(name="errorname", defaultValue="") String errorname,
			@RequestParam(name="errorlieu", defaultValue="") String errorlieu,
			@RequestParam(name="errordescription", defaultValue="") String errordescription,
			@RequestParam(name="errordate", defaultValue="") String errordate
			) {
		ModelAndView modelView = new ModelAndView();
		if(errorname.equals("error_topo_name")) {
			modelView.addObject("error_topo_name", "error");
		}
		if(errorlieu.equals("error_topo_lieu")){
			modelView.addObject("error_topo_lieu", "error");
		}
		if(errordescription.equals("error_topo_description")) {
			modelView.addObject("error_topo_description", "error");
		}
		if(errordate.equals("error_topo_date")) {
			modelView.addObject("error_topo_date", "error");
		}	
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
	public ModelAndView changeTopoPersonaltGet(@RequestParam(name="id") Long topoId,
			@RequestParam(name="errorname", defaultValue="") String errorname,
			@RequestParam(name="errorlieu", defaultValue="") String errorlieu,
			@RequestParam(name="errordescription", defaultValue="") String errordescription,
			@RequestParam(name="errordate", defaultValue="") String errordate
			) {
		ModelAndView modelView = new ModelAndView();
		if(errorname.equals("error_topo_name")) {
			modelView.addObject("error_topo_name", "error");
		}
		if(errorlieu.equals("error_topo_lieu")){
			modelView.addObject("error_topo_lieu", "error");
		}
		if(errordescription.equals("error_topo_description")) {
			modelView.addObject("error_topo_description", "error");
		}
		if(errordate.equals("error_topo_date")) {
			modelView.addObject("error_topo_date", "error");
		}	
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
		int errorDetected = 0;
		if(topo.getTopo_name().length() > 30 || topo.getTopo_name().length() < 3) {
			modelView.addObject("errorname", "error_topo_name");
			errorDetected++;
		}
		if(topo.getTopo_lieu().length() > 30 || topo.getTopo_lieu().length() < 3) {
			modelView.addObject("errorlieu", "error_topo_lieu");
			errorDetected++;
		}
		if(topo.getTopo_description().length() > 255 || topo.getTopo_description().length() < 10) {
			modelView.addObject("errordescription", "error_topo_description");
			errorDetected++;
		}
		if(dateVerification(topo.getTopo_date_parution()) == false) {
			modelView.addObject("errordate", "error_topo_date");
			errorDetected++;
		}	
		if(errorDetected != 0) {
			modelView.setViewName("redirect:/changetopo?id="+topo.getTopo_id());	
		}
		if(errorDetected == 0) {
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
				if(topo.isIs_available() == true) {
					topoUpdate.setUserReservingTheTopo(null);
				}		
				topoRepository.save(topoUpdate);
				modelView.setViewName("redirect:/listetopo");	
			}
			else {
				modelView.setViewName("redirect:/listetopo");	
			}
		}
		return modelView;
	}
	
	//change when using the private list in personal space
	@RequestMapping(value={"/changetopopersonal"}, method=RequestMethod.POST)
	public ModelAndView changeTopoPersonalPost(@Valid Topo topo, BindingResult bindingResult
			) {
		ModelAndView modelView = new ModelAndView();
		int errorDetected = 0;
		if(topo.getTopo_name().length() > 30 || topo.getTopo_name().length() < 3) {
			modelView.addObject("errorname", "error_topo_name");
			errorDetected++;
		}
		if(topo.getTopo_lieu().length() > 30 || topo.getTopo_lieu().length() < 3) {
			modelView.addObject("errorlieu", "error_topo_lieu");
			errorDetected++;
		}
		if(topo.getTopo_description().length() > 255 || topo.getTopo_description().length() < 10) {
			modelView.addObject("errordescription", "error_topo_description");
			errorDetected++;
		}
		if(dateVerification(topo.getTopo_date_parution()) == false) {
			modelView.addObject("errordate", "error_topo_date");
			errorDetected++;
		}	
		if(errorDetected != 0) {
			modelView.setViewName("redirect:/changetopopersonal?id="+topo.getTopo_id());	
		}
		if(errorDetected == 0) {
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
				if(topo.isIs_available() == true) {
					topoUpdate.setUserReservingTheTopo(null);
				}	
				topoRepository.save(topoUpdate);
				modelView.setViewName("redirect:/userlistetopo");	
			}
			else {
				modelView.setViewName("redirect:/userlistetopo");	
			}
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
	
	/*
	(Format: DD.MM.YYYY)
	pattern="(0[1-9]|1[0-9]|2[0-9]|3[01]).(0[1-9]|1[012]).[0-9]{4}" 
	*/
	public boolean dateVerification(String dateInput) {
		
		if(dateInput.length() != 10) {
			return false;
		}
		else {
			//day check
			if(dateInput.charAt(0) == '0' || dateInput.charAt(0) == '1' || dateInput.charAt(0) == '2' || dateInput.charAt(0) == '3') {
				if(dateInput.charAt(0) == '0' || dateInput.charAt(0) == '1' || dateInput.charAt(0) == '2') {
					if(dateInput.charAt(1) == '0' || dateInput.charAt(1) == '1' || dateInput.charAt(1) == '2' || dateInput.charAt(1) == '3' || dateInput.charAt(1) == '4' ||
							dateInput.charAt(1) == '5' || dateInput.charAt(1) == '6' || dateInput.charAt(1) == '7' || dateInput.charAt(1) == '8' || dateInput.charAt(1) == '9') {
								
							}
					else {
						return false;
					}
				}
				else if (dateInput.charAt(0) == '3') {
					if(dateInput.charAt(1) == '0' || dateInput.charAt(1) == '1') {
						
					}
					else {
						return false;
					}
				}
			}	
			else {
				return false;
			}
			//dot position 3 and 6
			if(dateInput.charAt(2) != '.' && dateInput.charAt(5) != '.') {
				return false;
			}
			//month check
			if(dateInput.charAt(3) == '0' || dateInput.charAt(3) == '1') {
				if(dateInput.charAt(3) == '0') {
					if(dateInput.charAt(4) == '1' || dateInput.charAt(4) == '2' || dateInput.charAt(4) == '3' || dateInput.charAt(4) == '4' || dateInput.charAt(4) == '5'
							|| dateInput.charAt(4) == '6' || dateInput.charAt(4) == '7' || dateInput.charAt(4) == '8' || dateInput.charAt(4) == '9') {			
					}
					else {
						return false;
					}
				}
				if(dateInput.charAt(3) == '1') {
					if(dateInput.charAt(4) == '0' || dateInput.charAt(4) == '1' || dateInput.charAt(4) == '2') {
						
					}
					else {
						return false;
					}
				}
				
			}
			else {
				return false;
			}
			//year check
			if(dateInput.charAt(6) == '0' || dateInput.charAt(6) == '1' || dateInput.charAt(6) == '2') {	
				
			}
			else {
				return false;
			}
			if(dateInput.charAt(7) == '0' || dateInput.charAt(7) == '1' || dateInput.charAt(7) == '2' || dateInput.charAt(7) == '3' || dateInput.charAt(7) == '4' ||
			dateInput.charAt(7) == '5' || dateInput.charAt(7) == '6' || dateInput.charAt(7) == '7' || dateInput.charAt(7) == '8' || dateInput.charAt(7) == '9') {

			}
			else {
				return false;
			}
			if(dateInput.charAt(8) == '0' || dateInput.charAt(8) == '1' || dateInput.charAt(8) == '2' || dateInput.charAt(8) == '3' || dateInput.charAt(8) == '4' ||
			dateInput.charAt(8) == '5' || dateInput.charAt(8) == '6' || dateInput.charAt(8) == '7' || dateInput.charAt(8) == '8' || dateInput.charAt(8) == '9') {

			}
			else {
				return false;
			}
			if(dateInput.charAt(9) == '0' || dateInput.charAt(9) == '1' || dateInput.charAt(9) == '2' || dateInput.charAt(9) == '3' || dateInput.charAt(9) == '4' ||
			dateInput.charAt(9) == '5' || dateInput.charAt(9) == '6' || dateInput.charAt(9) == '7' || dateInput.charAt(9) == '8' || dateInput.charAt(9) == '9') {

			}	
			else {
				return false;
			}
		}		
		return true;
	}
	
	
	
}
