package com.deroussen.dao;



import java.util.List;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.deroussen.entities.Secteur;


public interface SecteurRepository extends JpaRepository <Secteur, Long>{
	
	@Query("from Secteur s where s.secteur_name=?1")
	Secteur findBySecteur_name(String name);
	
	@Query("from Secteur s where s.secteur_id=?1")
	Secteur findByid(Long id);
	
	
	@Query("from Secteur where spot_spot_id=?1")
	List<Secteur> findBySpotId(Long id);
	
	// select u.email, r.role from user u inner join user_role ur on (u.id = ur.user_id) inner join role r on (ur.role_id=r.role_id) where u.email=?
	
	//@Query("from Secteur Inner join Spot on Secteur.spot_id = Spot.id")
	/*@Query("Secteur where id=?1")
	Secteur findSpotIdBySecteurId(Long id); where Secteur.id=?1*/
	
	//FONCTIONNE !////@Query("from Secteur e join Spot s where e.id = s.id")
	//FONCTIONNE !////@Query("select e.spot from Secteur e join Spot s where e.id = s.id")
	//FONCTIONNE !////@Query("select e.spot from Secteur e join Spot s where s.id=?1")
	

	
	
	/*
	@Query(value="SELECT * FROM SECTEUR WHERE SPOT_ID = ?")
	List <Secteur> findBySpotID(String spot_id);
	*/
	
	//  "select * from secteur where spotname=?";

}
