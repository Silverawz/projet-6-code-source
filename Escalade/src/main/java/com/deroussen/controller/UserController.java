package com.deroussen.controller;

import java.lang.reflect.Array;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.deroussen.entities.Topo;
import com.deroussen.entities.User;
import com.deroussen.service.TopoService;
import com.deroussen.service.UserService;

@Controller
@SessionAttributes("userEmail")
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private TopoService topoService;
	
	@RequestMapping(value={"/","/login"}, method=RequestMethod.GET)
	public ModelAndView login() {
		ModelAndView modelView = new ModelAndView();
		modelView.setViewName("user/login");
		return modelView;
	}
	
	@RequestMapping(value={"/signup"}, method=RequestMethod.GET)
	public ModelAndView signup() {
		ModelAndView model = new ModelAndView();
		User user = new User();
		model.addObject("user",  user);
		model.setViewName("user/signup");
		return model;
	}
	
	@RequestMapping(value={"/signup"}, method=RequestMethod.POST)
	public ModelAndView createUser(@Valid User user, BindingResult bindingResult) {
		ModelAndView modelView = new ModelAndView();
		User userExists = userService.findUserByEmail(user.getEmail());
		
		if(userExists != null) {
			bindingResult.rejectValue("email", "error.user","This email already exists!");
		}
		if(bindingResult.hasErrors()) {
			modelView.setViewName("user/signup");
		}
		else {
			userService.saveUser(user);
			modelView.addObject("msg","User has been registered successfully!");
			modelView.addObject("user",new User());
			modelView.setViewName("user/signup");
		}
		return modelView;
	}
	
	@RequestMapping(value={"/home/home"}, method=RequestMethod.GET)
	public ModelAndView home() {
		ModelAndView model = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());	
		model.addObject("userEmail", user.getEmail());
		model.setViewName("home/home");
		return model;
	}
	
	@RequestMapping(value={"/acces_denied"}, method=RequestMethod.GET)
	public ModelAndView accessdenied() {
		ModelAndView model = new ModelAndView();
		model.setViewName("errors/access_denied");
		return model;
	}
	
	@RequestMapping(value={"/personalpage"}, method=RequestMethod.GET)
	public ModelAndView personalPage(@SessionAttribute("userEmail") String userEmail) {
		ModelAndView modelView = new ModelAndView();
		Long userId = userService.findUserByEmail(userEmail).getId();
		List<Topo> listTopoOfCurrentUser = topoService.findTopoWithUserId(userId);
		List<String> grande_liste_de_tout = new ArrayList<>();
		for (Topo topo : listTopoOfCurrentUser) {
			int size = userService.findReservationUserIdWithTopoId(topo.getTopo_id()).size(); // WORK result excpected = (1,0,0)			
			if(size != 0) {		
				for(int i = 0; i < size; i++) {	
					grande_liste_de_tout.add(userService.findByUserId(userService.findReservationUserIdWithTopoId
					(topo.getTopo_id()).get(i)).getEmail()+" vous demande une réservation pour le topo : "+topo.getTopo_name());	
				}
			}	
		}
		modelView.addObject("grande_liste_de_tout", grande_liste_de_tout);
		modelView.setViewName("user/personalpage");
		return modelView;
	}
	
	
	@RequestMapping(value={"/reservationaccepted"}, method=RequestMethod.POST)
	public ModelAndView reservationAccepted(@SessionAttribute("userEmail") String userEmail,
			@RequestParam(name="e") String informationContainingUserEmailAndTopoName
			) {
		ModelAndView modelView = new ModelAndView();
		String[] split = informationContainingUserEmailAndTopoName.split(" vous demande une réservation pour le topo : ");
		Object userWhoWantReservationEmailObject = Array.get(split,0);	
		String userWhoWantReservationEmail = (String)userWhoWantReservationEmailObject;
		Object topoNameObject = Array.get(split,1);   
		String topoName = (String)topoNameObject;
		// delete the row that matches with topo id and user id
		Long userId = userService.findUserByEmail(userWhoWantReservationEmail).getId();
		deleteReservationRow(topoService.findByTopoName(topoName).getTopo_id(), userId);
		Topo topo = topoService.findByTopoName(topoName);
		topo.setIs_available(false);
		topo.setUserReservingTheTopo(userService.findUserByEmail(userWhoWantReservationEmail));
		topoService.saveTopo(topo);
		modelView.setViewName("redirect:/personalpage");
		return modelView;
	}
	
	
	@RequestMapping(value={"/reservationdeclined"}, method=RequestMethod.POST)
	public ModelAndView reservationDeclined(@SessionAttribute("userEmail") String userEmail,
			@RequestParam(name="e") String informationContainingUserEmailAndTopoName
			) {
		ModelAndView modelView = new ModelAndView();
		String[] split = informationContainingUserEmailAndTopoName.split(" vous demande une réservation pour le topo : ");
		Object userWhoWantReservationEmailObject = Array.get(split,0);	
		String userWhoWantReservationEmail = (String)userWhoWantReservationEmailObject;
		Object topoNameObject = Array.get(split,1);   
		String topoName = (String)topoNameObject;
		// delete the row that matches with topo id and user id
		deleteReservationRow(topoService.findByTopoName(topoName).getTopo_id(), userService.findUserByEmail(userWhoWantReservationEmail).getId());
		modelView.setViewName("redirect:/personalpage");
		return modelView;
	}
	
	
	
	public void deleteReservationRow(Long topoId, Long userId) {
		topoService.cancelReservationRequest(topoId, userId);	
	}
}
