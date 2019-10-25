package com.deroussen.service;

import java.sql.Connection;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import com.deroussen.dao.TopoRepository;
import com.deroussen.entities.Topo;

@Service("topoService")
public class TopoServiceImpl implements TopoService {

	@Autowired
	private TopoRepository topoRepository;
	@Autowired
	private DataSource dataSource;
	private Connection connection;
	private PreparedStatement prepareStatement1;
	private PreparedStatement prepareStatement2;
	private ResultSet  resultSet;
	
	private final String SQL_REQUEST_INSERT = "INSERT INTO topo_user_request VALUES(?,?)";
	private final String SQL_REQUEST_VERIFICATION = "SELECT * FROM topo_user_request WHERE topo_id=? AND user_id=?";
	private final String SQL_REQUEST_CANCEL_BY_OWNER = "DELETE FROM topo_user_request WHERE topo_id=? AND user_id=?";
	
	@Override
	public Topo findByTopoName(String name) {
		return topoRepository.findByName(name);
	}

	@Override
	public Topo findById(Long id) {
		return topoRepository.findByid(id);
	}

	@Override
	public void saveTopo(Topo topo) {
		topoRepository.save(topo);
	}

	@Override
	public void insertReservation(Long topoId, Long userId) {
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			prepareStatement1 = connection.prepareStatement(SQL_REQUEST_VERIFICATION);
			prepareStatement1.setLong(1, topoId);
			prepareStatement1.setLong(2, userId);
			resultSet  = prepareStatement1.executeQuery();
			if(!resultSet.next()) {				
				prepareStatement2 = connection.prepareStatement(SQL_REQUEST_INSERT);		
				prepareStatement2.setLong(1, topoId);
				prepareStatement2.setLong(2, userId);
				prepareStatement2.executeUpdate();
			}
			else {
				System.out.println("error, already exists");
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
				try {
					if(prepareStatement1 != null) {
						prepareStatement1.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				try {
					if(prepareStatement2 != null) {
						prepareStatement2.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				try {
					if(connection != null) {
						connection.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}	
		}
	}
	
	@Override
	public List<Topo> findTopoWithUserId(Long userId) {	
		return topoRepository.findTopoWithUserId(userId);
	}

	public void cancelReservationRequest(Long topoId, Long userId) {
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			prepareStatement1 = connection.prepareStatement(SQL_REQUEST_CANCEL_BY_OWNER);
			prepareStatement1.setLong(1, topoId);
			prepareStatement1.setLong(2, userId);
			prepareStatement1.executeUpdate();	
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				if(prepareStatement1 != null) {
					prepareStatement1.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			try {
				if(connection != null) {
					connection.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
