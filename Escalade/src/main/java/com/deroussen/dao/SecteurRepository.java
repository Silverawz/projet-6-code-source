package com.deroussen.dao;



import java.util.List;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.deroussen.entities.Secteur;


public interface SecteurRepository extends JpaRepository <Secteur, Long>{
	
	Secteur findBySecteurname(String name);
	Secteur findByid(Long id);
	
	Page <Secteur> findBySecteurnameContains(String mc, Pageable page);
	
	@Query("from Secteur where spot_id=?1")
	List<Secteur> findBySpotId(Long id);
	
	
	/*
	@Query(value="SELECT * FROM SECTEUR WHERE SPOT_ID = ?")
	List <Secteur> findBySpotID(String spot_id);
	*/
	
	//  "select * from secteur where spotname=?";

}
