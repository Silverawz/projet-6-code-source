package com.deroussen.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.deroussen.entities.Voie;

public interface VoieRepository extends JpaRepository <Voie, Long> {


	@Query("from Voie where voie_id=?1")
	Voie findByid(Long id);
	
	@Query("from Voie where secteur_secteur_id=?1")
	List<Voie> findBySecteurId(Long id);

	@Query("from Voie s where s.voie_name=?1")
	Voie findByVoie_name(String name);

}
