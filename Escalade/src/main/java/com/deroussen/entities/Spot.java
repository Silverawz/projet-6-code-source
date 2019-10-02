package com.deroussen.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="spot")
public class Spot {
	
	@Id @GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;
	
	@Column(name="spotname")
	private String spotname;
	
	@Column(name="is_equipped")
	private boolean is_equipped;
	
	@Column(name="is_official")
	private boolean is_official;

	@Column(name="madeByUser")
	private String madeByUser;
			
	public Spot() {
		
	}

	public Spot(Long id, String spotname, boolean is_equipped, boolean is_official) {
		super();
		this.id = id;
		this.spotname = spotname;
		this.is_equipped = is_equipped;
		this.is_official = is_official;
	}

	public Spot(Long id, String spotname, boolean is_equipped) {
		super();
		this.id = id;
		this.spotname = spotname;
		this.is_equipped = is_equipped;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSpotname() {
		return spotname;
	}

	public void setSpotname(String spotname) {
		this.spotname = spotname;
	}

	public boolean isIs_equipped() {
		return is_equipped;
	}

	public void setIs_equipped(boolean is_equipped) {
		this.is_equipped = is_equipped;
	}

	public boolean isIs_official() {
		return is_official;
	}

	public void setIs_official(boolean is_official) {
		this.is_official = is_official;
	}

	
	public String getMadeByUser() {
		return madeByUser;
	}

	public void setMadeByUser(String madeByUser) {
		this.madeByUser = madeByUser;
	}

	@Override
	public String toString() {
		return "Spot [id=" + id + ", spotname=" + spotname + ", is_equipped=" + is_equipped + ", is_official="
				+ is_official + ", madeByUser=" + madeByUser + "]";
	}


	
	
	
}
