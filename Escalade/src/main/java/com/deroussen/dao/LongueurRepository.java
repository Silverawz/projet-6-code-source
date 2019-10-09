package com.deroussen.dao;

import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.deroussen.entities.Longueur;

@Repository("longueurRepository")
public interface LongueurRepository extends JpaRepository <Longueur, Long> {
	
	Longueur findByLongueurname(String longueur);
	public Page <Longueur> findByLongueurnameContains(String mc, Pageable page);
	
	@Query("from Longueur where voie_id=?1")
	List<Longueur> findByVoieId(Long id);
}
