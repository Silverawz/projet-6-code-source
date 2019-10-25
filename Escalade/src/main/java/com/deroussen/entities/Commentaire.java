package com.deroussen.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name="commentaire")
public class Commentaire {

	@Id @GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long commentaire_id;
	
	@Size(min=10, max =255)
	@Column(name="commentaire_description")
	private String commentaire_description;
	
	@ManyToOne
	private Spot spot;
	
	@ManyToOne
	private User user;

	public Commentaire(Long commentaire_id, @Size(min = 10, max = 255) String commentaire_description, Spot spot,
			User user) {
		super();
		this.commentaire_id = commentaire_id;
		this.commentaire_description = commentaire_description;
		this.spot = spot;
		this.user = user;
	}

	public Commentaire() {
		super();
	}

	public Long getCommentaire_id() {
		return commentaire_id;
	}

	public void setCommentaire_id(Long commentaire_id) {
		this.commentaire_id = commentaire_id;
	}

	public String getCommentaire_description() {
		return commentaire_description;
	}

	public void setCommentaire_description(String commentaire_description) {
		this.commentaire_description = commentaire_description;
	}

	public Spot getSpot() {
		return spot;
	}

	public void setSpot(Spot spot) {
		this.spot = spot;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
		
	
}
