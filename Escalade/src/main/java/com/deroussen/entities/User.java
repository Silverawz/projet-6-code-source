package com.deroussen.entities;

import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;



@Entity
@Table(name="user")
public class User {
		
	@Id @GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;
	
	@Email
	@Column(name="email")
	private String email;
	
	@Size(min=3, max =30)
	@Column(name="firstname")
	private String firstname;
	
	@Size(min=3, max =30)
	@Column(name="lastname")
	private String lastname;
	
	@Size(max =255)
	@Column(name="password")
	private String password;
		
	@Column(name="active")
	private int active;
	
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name="user_role", joinColumns=@JoinColumn(name="user_id"), inverseJoinColumns=@JoinColumn(name="role_id"))
	private Set<Role> roles;

	@OneToMany(mappedBy="user")
	private List <Spot> spots;
	
	@OneToMany(mappedBy="user")
	private List <Commentaire> commentaires;

	
	
	public User() {
		super();
	}
	
	public User(String email) {
		super();
		this.email = email;
	}
	
	public User(Long id, String email, String firstname, String lastname, String password, int active, Set<Role> roles,
			List<Spot> spots) {
		super();
		this.id = id;
		this.email = email;
		this.firstname = firstname;
		this.lastname = lastname;
		this.password = password;
		this.active = active;
		this.roles = roles;
		this.spots = spots;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	
	
}
