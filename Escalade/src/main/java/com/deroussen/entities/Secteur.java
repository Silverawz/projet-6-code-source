package com.deroussen.entities;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="secteur")
public class Secteur {

	@Id @GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;
	
	@Column(name="secteurname")
	private String secteurname;

	@ManyToOne
	private Spot spot;
	
	@OneToMany(mappedBy="secteur", cascade = CascadeType.REMOVE)
	private List <Voie> voies;
	
	public Secteur() {
		
	}
	
	public Secteur(Long id, String secteurname) {
		super();
		this.id = id;
		this.secteurname = secteurname;
	}


	public Secteur(Long id, String secteurname, Spot spot) {
		super();
		this.id = id;
		this.secteurname = secteurname;
		this.spot = spot;
	}

	
	public List<Voie> getVoies() {
		return voies;
	}

	public void setVoies(List<Voie> voies) {
		this.voies = voies;
	}

	public Spot getSpot() {
		return spot;
	}

	public void setSpot(Spot spot) {
		this.spot = spot;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSecteurname() {
		return secteurname;
	}

	public void setSecteurname(String secteurname) {
		this.secteurname = secteurname;
	}


	
}
