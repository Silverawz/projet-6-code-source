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
@Table(name="spot")
public class Spot implements java.lang.Comparable<Spot>{
	
	@Id @GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long spot_id;
	
	@Size(min=3, max =30)
	@Column(name="spot_name")
	private String spot_name;
	
	@Size(min=3, max =30)
	@Column(name="spot_lieu")
	private String spot_lieu;
	
	@Column(name="is_equipped")
	private boolean is_equipped;
	
	@Column(name="is_official")
	private boolean is_official;

	@OneToMany(mappedBy="spot",cascade = CascadeType.ALL)
	private List <Secteur> secteurs;
	
	@OneToMany(mappedBy="spot",cascade = CascadeType.ALL)
	private List <Commentaire> commentaires;
	
	@ManyToOne
	private User user;
	
	public Spot() {
		
	}

	
	public Spot(Long spot_id, @Size(min = 3, max = 30) String spot_name, @Size(min = 3, max = 30) String spot_lieu,
			boolean is_equipped, boolean is_official, List<Secteur> secteurs, List<Commentaire> commentaires,
			User user) {
		super();
		this.spot_id = spot_id;
		this.spot_name = spot_name;
		this.spot_lieu = spot_lieu;
		this.is_equipped = is_equipped;
		this.is_official = is_official;
		this.secteurs = secteurs;
		this.commentaires = commentaires;
		this.user = user;
	}


	public Spot(@Size(min = 3, max = 30) String spot_name, @Size(min = 3, max = 30) String spot_lieu,
			boolean is_equipped, boolean is_official, User user) {
		super();
		this.spot_name = spot_name;
		this.spot_lieu = spot_lieu;
		this.is_equipped = is_equipped;
		this.is_official = is_official;
		this.user = user;
	}


	public Spot(String spot_name, boolean is_equipped, boolean is_official) {
		super();
		this.spot_name = spot_name;
		this.is_equipped = is_equipped;
		this.is_official = is_official;
	}

	public String getSpot_lieu() {
		return spot_lieu;
	}

	public void setSpot_lieu(String spot_lieu) {
		this.spot_lieu = spot_lieu;
	}

	public Long getSpot_id() {
		return spot_id;
	}

	public void setSpot_id(Long spot_id) {
		this.spot_id = spot_id;
	}

	public String getSpot_name() {
		return spot_name;
	}

	public void setSpot_name(String spot_name) {
		this.spot_name = spot_name;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public List<Secteur> getSecteurs() {
		return secteurs;
	}

	public void setSecteurs(List<Secteur> secteurs) {
		this.secteurs = secteurs;
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


	@Override
	public int compareTo(Spot o) {
		// TODO Auto-generated method stub
		return 0;
	}

	
}
