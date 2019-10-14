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
import javax.validation.constraints.Size;

@Entity
@Table(name="secteur")
public class Secteur {

	@Id @GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long secteur_id;
	
	@Size(min=3, max =30)
	@Column(name="secteur_name")
	private String secteur_name;

	@ManyToOne
	private Spot spot;
	
	@OneToMany(mappedBy="secteur", cascade = CascadeType.ALL)
	private List <Voie> voies;
	
	public Secteur() {
		
	}
	
	public Secteur(String secteur_name) {
		super();
		this.secteur_name = secteur_name;
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

	public Long getSecteur_id() {
		return secteur_id;
	}

	public void setSecteur_id(Long secteur_id) {
		this.secteur_id = secteur_id;
	}

	public String getSecteur_name() {
		return secteur_name;
	}

	public void setSecteur_name(String secteur_name) {
		this.secteur_name = secteur_name;
	}



	
	
}
