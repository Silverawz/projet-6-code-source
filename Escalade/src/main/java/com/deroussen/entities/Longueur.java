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
	
	@Id @GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;
	
	@Column(name="longueurname")
	private String longueurname;

	@Column(name="longueurcotation")
	private String longueurcotation;
	
	@ManyToOne
	private Voie voie;
	
	public Longueur() {
		
	}

	public Longueur(Long id, String longueurname, String longueurcotation, Voie voie) {
		super();
		this.id = id;
		this.longueurname = longueurname;
		this.longueurcotation = longueurcotation;
		this.voie = voie;
	}

	public Longueur(Long id, String longueurname, String longueurcotation) {
		super();
		this.id = id;
		this.longueurname = longueurname;
		this.longueurcotation = longueurcotation;
	}

	public Voie getVoie() {
		return voie;
	}

	public void setVoie(Voie voie) {
		this.voie = voie;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLongueurname() {
		return longueurname;
	}

	public void setLongueurname(String longueurname) {
		this.longueurname = longueurname;
	}

	public String getLongueurcotation() {
		return longueurcotation;
	}

	public void setLongueurcotation(String longueurcotation) {
		this.longueurcotation = longueurcotation;
	}
	
	

}
