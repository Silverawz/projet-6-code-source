package com.deroussen.controller;

import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import com.deroussen.entities.User;
import com.deroussen.service.UserService;

@Controller
@SessionAttributes("userEmail")
public class UserController {
	
	@Autowired
	private UserService userService;
	
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
	

	
}
