package com.deroussen.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;



@Controller
@SessionAttributes("userEmail")
public class WelcomeController {
	

	
	@RequestMapping(value={"/welcome/welcomepage"}, method=RequestMethod.GET)
	public ModelAndView home() {
		ModelAndView model = new ModelAndView();
		model.setViewName("welcome/welcomepage");
		return model;
	}

	
	
	
}
