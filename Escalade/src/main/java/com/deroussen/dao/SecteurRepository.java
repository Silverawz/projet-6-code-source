package com.deroussen.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.deroussen.entities.Secteur;

public interface SecteurRepository extends JpaRepository <Secteur, Long>{
	Secteur findBySecteurname(String name);
}
