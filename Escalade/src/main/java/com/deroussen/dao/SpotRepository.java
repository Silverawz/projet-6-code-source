package com.deroussen.dao;

import org.springframework.stereotype.Repository;


import com.deroussen.entities.Spot;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository("spotRepository")
public interface SpotRepository extends JpaRepository <Spot, Long> {
	Spot findBySpotname(String name);
	public Page <Spot> findBySpotnameContains(String mc, Pageable page);
	Spot findByid(Long id);
}
