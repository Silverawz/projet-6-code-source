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
@Table(name="voie")
public class Voie {

	@Id @GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long voie_id;
	
	@Size(min=3, max =30)
	@Column(name="voie_name")
	private String voie_name;

	@Size(min=1, max =3)
	@Column(name="voie_cotation")
	private String voie_cotation;
	
	@ManyToOne
	private Secteur secteur;
	
	@OneToMany(mappedBy="voie", cascade = CascadeType.ALL)
	private List <Longueur> longueurs;
	

	
	public Voie() {
		
	}
	
	public Voie(String voie_name, String voie_cotation) {
		super();
		this.voie_name = voie_name;
		this.voie_cotation = voie_cotation;
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

	public Long getVoie_id() {
		return voie_id;
	}

	public void setVoie_id(Long voie_id) {
		this.voie_id = voie_id;
	}

	public String getVoie_name() {
		return voie_name;
	}

	public void setVoie_name(String voie_name) {
		this.voie_name = voie_name;
	}

	public String getVoie_cotation() {
		return voie_cotation;
	}

	public void setVoie_cotation(String voie_cotation) {
		this.voie_cotation = voie_cotation;
	}
	

	
	
	
	
}
