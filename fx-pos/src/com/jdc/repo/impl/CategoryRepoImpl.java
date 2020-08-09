package com.jdc.repo.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.jdc.entities.Category;
import com.jdc.repo.CategoryRepo;
import com.jdc.util.DBConnection;
import com.jdc.util.MessageHandler;
import com.jdc.util.PosException;

public class CategoryRepoImpl implements CategoryRepo {

	private static final CategoryRepoImpl REPO = new CategoryRepoImpl();
	
	private static final String SELECT_ALL = "select * from category";
	private static final String INSERT = "insert into category (name) values (?)";
	private static final String DELETE = "delete from category where id = ?";
	
	
	private CategoryRepoImpl() {}
	
	public static CategoryRepoImpl getInstance() {
		return REPO;
	}
	
	@Override
	public List<Category> findAll() {
		
		List<Category> list = new ArrayList<Category>();
		
		try (Connection conn = DBConnection.getConnection();
				Statement stmt = conn.createStatement()){
			
			ResultSet rs = stmt.executeQuery(SELECT_ALL);
			
			while(rs.next()) {
				Category c = new Category();
				c.setId(rs.getInt("id"));
				c.setName(rs.getString("name"));
				list.add(c);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void create(String name) {
		
		// validation for empty category name
		if(name.isEmpty() || null == name)
			throw new PosException("Category name must not be empty !!!");
		
		// validation for add existing category
//		TODO: if(false)
//			throw new PosException("Already exists category name !!!");
			
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement prep = conn.prepareStatement(INSERT)) {

			prep.setString(1, name);
			prep.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(int id) {
		
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement prep = conn.prepareStatement(DELETE)) {

			prep.setInt(1, id);
			prep.executeUpdate();

		} catch (SQLException e) {
//			e.printStackTrace();
			MessageHandler.show(e);
		}
	}

}










