package com.deroussen.service;



import java.util.List;

import com.deroussen.entities.Topo;

public interface TopoService {

	public List<Topo> findTopoWithUserId(Long userId);
	public Topo findByTopoName(String nameTopo);
	public Topo findById(Long topoId);
	public void saveTopo(Topo topo);
	void insertReservation(Long topoId, Long userId);
	void cancelReservationRequest(Long topoId, Long userId);
	
}
