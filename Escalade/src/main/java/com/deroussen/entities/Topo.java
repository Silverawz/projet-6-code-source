package com.deroussen.entities;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name="topo")
public class Topo {
	
	@Id @GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long topo_id;
	
	@Size(min=3, max =30)
	@Column(name="topo_name")
	private String topo_name;
	
	@Size(min=10, max =255)
	@Column(name="topo_description")
	private String topo_description;
	
	@Size(min=3, max =30)
	@Column(name="topo_lieu")
	private String topo_lieu;
	
	@Size(min=10, max=10)
	@Column(name="topo_date_parution")
	private String topo_date_parution;
	
	@Column(name="is_available")
	private boolean is_available;
	
	@ManyToOne
	private User userOwnerOfTheTopo;
	
	@ManyToOne
	private User userReservingTheTopo;
	
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name="topo_user_request", joinColumns=@JoinColumn(name="topo_id"), inverseJoinColumns=@JoinColumn(name="user_id"))
	private Set<User> usersAskingForReservation;
	
	public Topo() {
		super();
	}
	
	public Topo(Long topo_id, @Size(min = 3, max = 30) String topo_name,
			@Size(min = 10, max = 255) String topo_description, @Size(min = 3, max = 30) String topo_lieu,
			String topo_date_parution, boolean is_available, User userOwnerOfTheTopo, User userReservingTheTopo) {
		super();
		this.topo_id = topo_id;
		this.topo_name = topo_name;
		this.topo_description = topo_description;
		this.topo_lieu = topo_lieu;
		this.topo_date_parution = topo_date_parution;
		this.is_available = is_available;
		this.userOwnerOfTheTopo = userOwnerOfTheTopo;
		this.userReservingTheTopo = userReservingTheTopo;
	}

	public Long getTopo_id() {
		return topo_id;
	}

	public void setTopo_id(Long topo_id) {
		this.topo_id = topo_id;
	}

	public String getTopo_name() {
		return topo_name;
	}

	public void setTopo_name(String topo_name) {
		this.topo_name = topo_name;
	}

	public String getTopo_description() {
		return topo_description;
	}

	public void setTopo_description(String topo_description) {
		this.topo_description = topo_description;
	}

	public String getTopo_lieu() {
		return topo_lieu;
	}

	public void setTopo_lieu(String topo_lieu) {
		this.topo_lieu = topo_lieu;
	}

	public String getTopo_date_parution() {
		return topo_date_parution;
	}

	public void setTopo_date_parution(String topo_date_parution) {
		this.topo_date_parution = topo_date_parution;
	}

	public boolean isIs_available() {
		return is_available;
	}

	public void setIs_available(boolean is_available) {
		this.is_available = is_available;
	}

	public User getUserOwnerOfTheTopo() {
		return userOwnerOfTheTopo;
	}

	public void setUserOwnerOfTheTopo(User userOwnerOfTheTopo) {
		this.userOwnerOfTheTopo = userOwnerOfTheTopo;
	}

	public User getUserReservingTheTopo() {
		return userReservingTheTopo;
	}

	public void setUserReservingTheTopo(User userReservingTheTopo) {
		this.userReservingTheTopo = userReservingTheTopo;
	}

	public Set<User> getUsersAskingForReservation() {
		return usersAskingForReservation;
	}

	public void setUsersAskingForReservation(Set<User> usersAskingForReservation) {
		this.usersAskingForReservation = usersAskingForReservation;
	}

	
	
	
	
	
	
	
	
	
	
}
