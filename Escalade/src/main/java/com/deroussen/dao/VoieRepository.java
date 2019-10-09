package com.deroussen.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.deroussen.entities.Voie;

public interface VoieRepository extends JpaRepository <Voie, Long> {

	Voie findByVoiename(String name);
	public Page <Voie> findByVoienameContains(String mc, Pageable page);
	Voie findByid(Long id);
	
	@Query("from Voie where secteur_id=?1")
	List<Voie> findBySecteurId(Long id);
}
