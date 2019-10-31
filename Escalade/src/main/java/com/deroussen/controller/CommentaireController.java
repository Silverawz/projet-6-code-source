package com.deroussen.controller;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.deroussen.dao.CommentaireRepository;
import com.deroussen.entities.Commentaire;
import com.deroussen.entities.Role;
import com.deroussen.entities.User;
import com.deroussen.service.CommentaireService;
import com.deroussen.service.SpotService;
import com.deroussen.service.UserService;

@Controller
@SessionAttributes("userEmail")
public class CommentaireController {
	
	
	@Autowired
	private CommentaireService commentaireService;
	@Autowired
	private CommentaireRepository commentaireRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private SpotService spotService;
	
	@RequestMapping(value={"/listecommentaire"}, method=RequestMethod.GET)
	public ModelAndView formGet(@RequestParam(name="id") Long spotId,
			@SessionAttribute(required=false, name="userEmail") String userEmail,
			@RequestParam(name="error", defaultValue="") String error) {
		ModelAndView modelView = new ModelAndView();	
		if(error.equals("Le commentaire doit faire entre 10 et 255 caractères!")) {
			modelView.addObject("error", "error");
		}
		List<Commentaire> commentaires = commentaireService.findBySpotId(spotId);
		Collections.reverse(commentaires);
		if(userEmail != null) {
			boolean result = checkIfUserIsMember(userService.findUserByEmail(userEmail));
			if(result == true) {
				modelView.addObject("role", "MEMBER");
			}
		}
		modelView.addObject("spot_name", spotService.findById(spotId).getSpot_name());
		modelView.addObject("spotId", spotId);
		modelView.addObject("commentaires", commentaires);
		modelView.setViewName("comments/comments");
		return modelView;
	}

	
	@RequestMapping(value={"/createcommentaire"}, method=RequestMethod.POST)
	public ModelAndView formPost(@RequestParam(name="id") Long spotId,
			@RequestParam(name="description") String commentaireDescription,
			@SessionAttribute("userEmail") String userEmail) {
		ModelAndView modelView = new ModelAndView();
		if(commentaireDescription.length() < 10 || commentaireDescription.length() > 255) {
			modelView.addObject("error", "Le commentaire doit faire entre 10 et 255 caractères!");
			modelView.setViewName("redirect:/listecommentaire?id="+spotId);
			return modelView;	
		}
		Commentaire commentaire = new Commentaire();
		commentaire.setCommentaire_description(commentaireDescription);
		commentaire.setSpot(spotService.findById(spotId));
		commentaire.setUser(userService.findUserByEmail(userEmail));
		commentaireService.saveCommentaire(commentaire);
		modelView.setViewName("redirect:/listecommentaire?id="+spotId);
		return modelView;		
	}
	
	@RequestMapping(value={"/deletecommentaire"}, method=RequestMethod.GET)
	public ModelAndView formGetDelete(@RequestParam(name="id") Long commentaireId,
			@SessionAttribute("userEmail") String userEmail) {
		ModelAndView modelView = new ModelAndView();	
		Commentaire commentaire = commentaireService.findByCommentaireId(commentaireId);
		boolean result = checkIfUserIsMember(userService.findUserByEmail(userEmail));
		if(commentaire.getUser().getEmail().equals(userEmail) || result == true){		
			commentaireRepository.deleteById(commentaireId);
			modelView.setViewName("redirect:/listecommentaire?id="+commentaire.getSpot().getSpot_id());
		}
		else {
			modelView.setViewName("errors/access_denied");
		}
		return modelView;
	}
	
	
	@RequestMapping(value={"/changecomment"}, method=RequestMethod.GET)
	public ModelAndView formGetChange(@RequestParam(name="id") Long commentaireId,
			@SessionAttribute("userEmail") String userEmail,
			@RequestParam(name="error", defaultValue="") String error) {
		ModelAndView modelView = new ModelAndView();			
		if(error.equals("Le commentaire doit faire entre 10 et 255 caractères!")) {
			modelView.addObject("error", "error");
		}
		Commentaire commentaire = commentaireService.findByCommentaireId(commentaireId);
		boolean result = checkIfUserIsMember(userService.findUserByEmail(userEmail));
		if(commentaire.getUser().getEmail().equals(userEmail) || result == true){	
			modelView.addObject("commentaire_id", commentaireId);
			modelView.addObject("madeByUser", commentaire.getUser().getEmail());
			modelView.addObject("description", commentaire.getCommentaire_description());
			modelView.addObject("role", "CanBeMEMBER");
			modelView.setViewName("comments/changecomments");
		}
		else {
			modelView.setViewName("errors/access_denied");
		}		
		return modelView;
	}
	
	
	@RequestMapping(value={"/changecomment"}, method=RequestMethod.POST)
	public ModelAndView formPostChange(@RequestParam(name="id") Long commentaireId,
			@RequestParam(name="description") String commentaireDescription,
			@SessionAttribute("userEmail") String userEmail) {
		ModelAndView modelView = new ModelAndView();
		if(commentaireDescription.length() < 10 || commentaireDescription.length() > 255) {
			modelView.addObject("error", "Le commentaire doit faire entre 10 et 255 caractères!");
			modelView.setViewName("redirect:/changecomment?id="+commentaireId);	
		}
		else {
			Commentaire commentaire = commentaireRepository.findByCommentaireId(commentaireId);
			commentaire.setCommentaire_description(commentaireDescription);
			commentaireService.saveCommentaire(commentaire);	
			modelView.setViewName("redirect:/listecommentaire?id="+commentaire.getSpot().getSpot_id());	
		}	
		return modelView;
	}
	
	
	
	public boolean checkIfUserIsMember(User user) {
		Set<Role> userRoles = user.getRoles();
		boolean isAMember= false;
		C:for (Role role : userRoles) {
			if(role.getRole().equals("MEMBER") || role.getRole().equals("ADMIN")) {
				isAMember = true;
			}
			if(isAMember == true) {
				break C;
			}
		}		
		if(isAMember) {
			return true;
		}
		else {
			return false;
		}
	}

}
