package com.deroussen.dao;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.deroussen.entities.Topo;

@Repository("topoRepository")
public interface TopoRepository extends JpaRepository <Topo, Long> {
 
	@Query("from Topo where topo_id=?1")
	Topo findByid(Long id);
	
	@Query("from Topo where topo_name=?1")
	Topo findByName(String name);
	
	@Query("from Topo where user_owner_of_the_topo_id=?1")
	List<Topo> findTopoWithUserId(Long id);
	
	
	
}
