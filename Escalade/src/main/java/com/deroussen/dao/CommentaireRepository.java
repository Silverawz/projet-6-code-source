package com.deroussen.dao;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.deroussen.entities.Commentaire;



@Repository("commentaireRepository")
public interface CommentaireRepository extends JpaRepository <Commentaire, Long>{
	

	
	@Query("from Commentaire where spot_spot_id=?1")
	List<Commentaire> findBySpot_Id(Long id);

	@Query("from Commentaire where commentaire_id=?1")
	Commentaire findByCommentaireId(Long id);
	

}
