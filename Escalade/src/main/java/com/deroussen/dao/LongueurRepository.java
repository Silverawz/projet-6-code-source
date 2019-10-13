package com.deroussen.dao;

import java.util.List;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.deroussen.entities.Longueur;

@Repository("longueurRepository")
public interface LongueurRepository extends JpaRepository <Longueur, Long> {
	
	
	
	@Query("from Longueur where voie_voie_id=?1")
	List<Longueur> findByVoieId(Long id);

	@Query("from Longueur where longueur_id=?1")
	Longueur findByid(Long id);
	
}
