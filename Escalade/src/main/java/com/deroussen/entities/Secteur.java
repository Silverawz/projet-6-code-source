package com.deroussen.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="secteur")
public class Secteur {

	@Id @GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;
	
	@Column(name="secteurname")
	private String secteurname;

	@Column(name="spotname")
	private String spotname;
	
	public Secteur(Long id, String secteurname) {
		super();
		this.id = id;
		this.secteurname = secteurname;
	}

	public Secteur() {
		
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

	public String getSpotname() {
		return spotname;
	}

	public void setSpotname(String spotname) {
		this.spotname = spotname;
	}

	@Override
	public String toString() {
		return "Secteur [id=" + id + ", secteurname=" + secteurname + ", spotname=" + spotname + "]";
	}

	

	
	
	
}
