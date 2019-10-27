package com.deroussen.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.deroussen.dao.RoleRepository;
import com.deroussen.dao.UserRepository;
import com.deroussen.entities.Role;
import com.deroussen.entities.User;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	private final String SQL_REQUEST_FIND_RESERVATION_USER = "SELECT user_id FROM topo_user_request WHERE topo_id=?";
	@Autowired
	private DataSource dataSource;
	private Connection connection;
	private PreparedStatement prepareStatement;
	private ResultSet resultSet;
	
	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public void saveUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setActive(1);
		Role userRole = roleRepository.findByRole("USER");
		user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		userRepository.save(user);
	}

	@Override
	public User findByUserId(Long id) {
		return userRepository.findByUserId(id);
	}

	@Override
	public List<Long> findReservationUserIdWithTopoId(Long topoId) {
		List<Long> reservationUser = new ArrayList<Long>();
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			prepareStatement = connection.prepareStatement(SQL_REQUEST_FIND_RESERVATION_USER);
			prepareStatement.setLong(1, topoId);
			resultSet = prepareStatement.executeQuery();
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData(); 
			int columnCount = resultSetMetaData.getColumnCount();
			while(resultSet.next()) {
				int i = 1;
				while(i <= columnCount) {	
					reservationUser.add(Long.parseLong(resultSet.getString(i)));
					i++;
				}			
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	finally {
				try {
					if(resultSet != null) {
						resultSet.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				try {
					if(prepareStatement != null) {
						prepareStatement.close();
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
		return reservationUser;
	}


	
}
