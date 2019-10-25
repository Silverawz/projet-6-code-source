package com.deroussen.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deroussen.dao.CommentaireRepository;
import com.deroussen.entities.Commentaire;


@Service("commentaireService")
public class CommentaireServiceImpl implements CommentaireService {

	@Autowired
	private CommentaireRepository commentaireRepository;
	
	@Override
	public List<Commentaire> findBySpotId(Long id) {
		return commentaireRepository.findBySpot_Id(id);
	}

	@Override
	public void saveCommentaire(Commentaire commentaire) {
		commentaireRepository.save(commentaire);
	}

	@Override
	public Commentaire findByCommentaireId(Long id) {
		return commentaireRepository.findByCommentaireId(id);
	}


	
}
