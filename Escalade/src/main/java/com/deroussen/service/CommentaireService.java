package com.deroussen.service;

import java.util.List;


import com.deroussen.entities.Commentaire;


public interface CommentaireService {
	
	public List<Commentaire> findBySpotId(Long id);
	public void saveCommentaire(Commentaire commentaire);
	public Commentaire findByCommentaireId(Long id);
	
}
