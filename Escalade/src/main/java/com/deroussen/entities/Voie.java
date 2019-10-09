package com.deroussen.entities;


import java.util.List;
import java.util.Optional;

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
@Table(name="voie")
public class Voie {

	@Id @GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;
	
	@Column(name="voiename")
	private String voiename;

	@Column(name="voiecotation")
	private String voiecotation;
	
	@ManyToOne
	private Secteur secteur;
	
	@OneToMany(mappedBy="voie", cascade = CascadeType.REMOVE)
	private List <Longueur> longueurs;
	
	public Voie(Long id, String voiename, String voiecotation, String secteurname) {
		super();
		this.id = id;
		this.voiename = voiename;
		this.voiecotation = voiecotation;
	}

	public Voie(Long id, String voiename, String voiecotation, String secteurname, Secteur secteur) {
		super();
		this.id = id;
		this.voiename = voiename;
		this.voiecotation = voiecotation;
		this.secteur = secteur;
	}
	
	public Voie(Long id, String voiename, String voiecotation, Secteur secteur, List<Longueur> longueurs) {
		super();
		this.id = id;
		this.voiename = voiename;
		this.voiecotation = voiecotation;
		this.secteur = secteur;
		this.longueurs = longueurs;
	}

	public Voie() {
		
	}
	
	public List<Longueur> getLongueurs() {
		return longueurs;
	}

	public void setLongueurs(List<Longueur> longueurs) {
		this.longueurs = longueurs;
	}

	public Secteur getSecteur() {
		return secteur;
	}

	public void setSecteur(Secteur secteur) {
		this.secteur = secteur;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVoiename() {
		return voiename;
	}

	public void setVoiename(String voiename) {
		this.voiename = voiename;
	}

	public String getVoiecotation() {
		return voiecotation;
	}

	public void setVoiecotation(String voiecotation) {
		this.voiecotation = voiecotation;
	}

	
	
	
	
}
