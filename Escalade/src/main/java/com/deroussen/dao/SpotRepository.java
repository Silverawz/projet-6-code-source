package com.deroussen.dao;

import org.springframework.stereotype.Repository;


import com.deroussen.entities.Spot;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Repository("spotRepository")
public interface SpotRepository extends JpaRepository <Spot, Long> {
	
	
	@Query("from Spot s where s.spot_name=?1")
	Spot findBySpot_name(String name);
	
	
	@Query("from Spot s where s.spot_id=?1")
	Spot findByid(Long id);	
	
	
	
	@Query("from Spot s where s.is_equipped=?1")
	List<Spot> findByEquipped(boolean is_equipped);
	
	@Query("from Spot s where s.is_official=?1")
	List<Spot> findByOfficial(boolean is_official);

}
