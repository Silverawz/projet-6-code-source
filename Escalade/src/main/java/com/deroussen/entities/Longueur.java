package com.deroussen.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="longueur")
public class Longueur {
	
	@Id @GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long longueur_id;
	
	@Column(name="longueur_name")
	private String longueur_name;

	@Column(name="longueur_cotation")
	private String longueur_cotation;
	
	@ManyToOne
	private Voie voie;
	
	public Longueur() {
		
	}

	public Longueur(String longueur_name, String longueur_cotation) {
		super();
		this.longueur_name = longueur_name;
		this.longueur_cotation = longueur_cotation;
	}

	public Voie getVoie() {
		return voie;
	}

	public void setVoie(Voie voie) {
		this.voie = voie;
	}

	public Long getLongueur_id() {
		return longueur_id;
	}

	public void setLongueur_id(Long longueur_id) {
		this.longueur_id = longueur_id;
	}

	public String getLongueur_name() {
		return longueur_name;
	}

	public void setLongueur_name(String longueur_name) {
		this.longueur_name = longueur_name;
	}

	public String getLongueur_cotation() {
		return longueur_cotation;
	}

	public void setLongueur_cotation(String longueur_cotation) {
		this.longueur_cotation = longueur_cotation;
	}


	
	

}
