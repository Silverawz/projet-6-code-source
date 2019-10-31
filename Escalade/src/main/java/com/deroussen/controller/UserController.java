package com.deroussen.controller;



import java.util.ArrayList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

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

import com.deroussen.dao.TopoRepository;
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
	@Autowired
	private TopoRepository topoRepository;
	
	@RequestMapping(value={"/","/login"}, method=RequestMethod.GET)
	public ModelAndView login() {
		ModelAndView modelView = new ModelAndView();
		modelView.setViewName("user/login");
		return modelView;
	}
	
	@RequestMapping(value={"/signup"}, method=RequestMethod.GET)
	public ModelAndView signup() {
		ModelAndView modelView = new ModelAndView();
		User user = new User();
		modelView.addObject("user",  user);
		modelView.setViewName("user/signup");
		return modelView;
	}
	
	@RequestMapping(value={"/signup"}, method=RequestMethod.POST)
	public ModelAndView createUser(@Valid User user, BindingResult bindingResult) {
		ModelAndView modelView = new ModelAndView();
		User userExists = userService.findUserByEmail(user.getEmail());
		int errorDetected = 0;
		if(userExists != null) {
			bindingResult.rejectValue("email", "error.user","This email already exists!");
			errorDetected++;
		}
		if(user.getPassword().length() > 25 || user.getPassword().length() < 5 && errorDetected == 0) {
			bindingResult.rejectValue("password", "error.user", "Password size is incorrect!");
			errorDetected++;
		}
		if(user.getFirstname().length() > 30 || user.getFirstname().length() < 3 && errorDetected == 0) {
			bindingResult.rejectValue("firstname", "error.user", "Firstname size is incorrect!");
			errorDetected++;
		}
		if(user.getLastname().length() > 30 || user.getLastname().length() < 3 && errorDetected == 0) {
			bindingResult.rejectValue("lastname", "error.user", "Firstname size is incorrect!");
			errorDetected++;
		}
		if(user.getEmail().length() > 40 || user.getFirstname().length() < 5 && errorDetected == 0) {
			bindingResult.rejectValue("email", "error.user", "Email size is incorrect!");
			errorDetected++;
		}
		else if(user.getEmail().length() < 40 || user.getFirstname().length() > 5 && errorDetected == 0) {
			if (!rfc2822.matcher(user.getEmail()).matches()) {
				bindingResult.rejectValue("email", "error.user", "Email size is incorrect!");
				errorDetected++;
			}
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
		ModelAndView modelView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		modelView.addObject("userEmail", user.getEmail());
		modelView.setViewName("home/home");
		return modelView;
	}
	
	@RequestMapping(value={"/acces_denied"}, method=RequestMethod.GET)
	public ModelAndView accessdenied() {
		ModelAndView modelView = new ModelAndView();
		modelView.setViewName("errors/access_denied");
		return modelView;
	}
		
	@RequestMapping(value={"/personalpage"}, method=RequestMethod.GET)
	public ModelAndView personalPage(@SessionAttribute("userEmail") String userEmail) {
		ModelAndView modelView = new ModelAndView();
		User user = userService.findUserByEmail(userEmail);
		List<Topo> listTopoOfCurrentUser = topoService.findTopoWithUserId(user.getId());		
		int numberOfRequestReceived = 0;
		for (Topo topo : listTopoOfCurrentUser) {
			int size = topo.getUsersAskingForReservation().size();
			// int size = userService.findReservationUserIdWithTopoId(topo.getTopo_id()).size();
			if(size != 0) {
				for(int i = 0; i < size; i++) {	
					numberOfRequestReceived++;
				}
			}			
		}	
		modelView.addObject("topoPossede", listTopoOfCurrentUser.size());
		modelView.addObject("requeteRecu", numberOfRequestReceived);
		modelView.setViewName("user/personalpage");
		return modelView;
	}
	
	@RequestMapping(value={"/userlistetopo"}, method=RequestMethod.GET)
	public ModelAndView getTopoList(@SessionAttribute("userEmail") String userEmail) {
		ModelAndView modelView = new ModelAndView();	
		List <Topo> topos = topoService.findTopoWithUserId(userService.findUserByEmail(userEmail).getId());
		modelView.addObject("topolist", topos);
		modelView.setViewName("user/userlistetopo");
		return modelView;
	}
	
	@RequestMapping(value={"/userlisterequestreceived"}, method=RequestMethod.GET)
	public ModelAndView userListeRequestReceived(@SessionAttribute("userEmail") String userEmail) {
		ModelAndView modelView = new ModelAndView();
		Long userId = userService.findUserByEmail(userEmail).getId();
		List<Topo> listTopoOfCurrentUser = topoService.findTopoWithUserId(userId);
		
		List<String> emailUserWhoMadeRequest = new ArrayList<>();
		List<String> listToposName = new ArrayList<>();
		
		for (Topo topo : listTopoOfCurrentUser) {
			Set<User> users = topo.getUsersAskingForReservation();
			// int size = userService.findReservationUserIdWithTopoId(topo.getTopo_id()).size();		
			if(users.size() != 0) {		
				List<Long> listReservationUserId = new ArrayList<>();
				for (User user : users) {
					listReservationUserId.add(user.getId());
					User user1 = userService.findByUserId(user.getId());
					emailUserWhoMadeRequest.add(user1.getEmail());
					listToposName.add(topo.getTopo_name());
				}
					/*
				for(int i = 0; i < size; i++) {	
					users = topo.getUsersAskingForReservation();
					//on met dans une liste les id des users
					listReservationUserId = userService.findReservationUserIdWithTopoId(topo.getTopo_id());
					//on creer un user avec l'id 
					User user = userService.findByUserId(listReservationUserId.get(i));
					//on met l'email de l'user dans une liste
					emailUserWhoMadeRequest.add(user.getEmail());	
					listToposName.add(topo.getTopo_name());
				}  */	 
			}	
		}		
		List<List <String>> emailUserWhoMadeRequestAndTopoName = new ArrayList<>();
		for(int i = 0; i < listToposName.size(); i++) {
			List <String> newList = new ArrayList<>();
			newList.add(listToposName.get(i));
			newList.add(emailUserWhoMadeRequest.get(i));
			emailUserWhoMadeRequestAndTopoName.add(newList);
		}
		modelView.addObject("listToposName", listToposName);
		modelView.addObject("emailUserWhoMadeRequest", emailUserWhoMadeRequest);
		modelView.addObject("listofListOfTopoNameWithEmailUserWhoMadeRequest", emailUserWhoMadeRequestAndTopoName);
		modelView.setViewName("user/userlisterequestreceived");
		return modelView;
	}
		
	@RequestMapping(value={"/reservationaccepted"}, method=RequestMethod.POST)
	public ModelAndView reservationAccepted(@SessionAttribute("userEmail") String userEmail,
			@RequestParam(name="topoNameAndEmailUserWhoMadeRequest") List<String> topoNameAndEmailUserWhoMadeRequest
			) {
		ModelAndView modelView = new ModelAndView();
		String topoName = topoNameAndEmailUserWhoMadeRequest.get(0).replaceAll("\\[|\\]", "");
		String userEmailName = topoNameAndEmailUserWhoMadeRequest.get(1).replaceAll("\\[|\\]", "");
		Set <User> users = new HashSet<>();
		topoService.findByTopoName(topoName).setUsersAskingForReservation(users);
		topoRepository.save(topoService.findByTopoName(topoName));
		Topo topo = topoService.findByTopoName(topoName);
		topo.setIs_available(false);
		topo.setUserReservingTheTopo(userService.findUserByEmail(userEmailName));
		topoService.saveTopo(topo);
		modelView.setViewName("redirect:/personalpage");
		return modelView;
	}
	
	@RequestMapping(value={"/reservationdeclined"}, method=RequestMethod.POST)
	public ModelAndView reservationDeclined(@SessionAttribute("userEmail") String userEmail,
			@RequestParam(name="topoNameAndEmailUserWhoMadeRequest") List<String> topoNameAndEmailUserWhoMadeRequest
			) {
		ModelAndView modelView = new ModelAndView();	
		String topoName = topoNameAndEmailUserWhoMadeRequest.get(0).replaceAll("\\[|\\]", "");
		String userEmailName = topoNameAndEmailUserWhoMadeRequest.get(1).replaceAll("\\[|\\]", "");
		Long userId = userService.findUserByEmail(userEmailName).getId();	
		Set<User> users = topoService.findByTopoName(topoName).getUsersAskingForReservation();
		C:for (User user : users) {
			if(user.getId() == userId) {
				users.remove(user);
				break C;
			}
		}
		topoRepository.save(topoService.findByTopoName(topoName));
		modelView.setViewName("redirect:/personalpage");
		return modelView;
	}
	
	private static final Pattern rfc2822 = Pattern.compile(
	        "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$"
	);
}
