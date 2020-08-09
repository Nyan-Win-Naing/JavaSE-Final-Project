package com.jdc.repo.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jdc.entities.Category;
import com.jdc.entities.Item;
import com.jdc.repo.ItemRepo;
import com.jdc.util.CheckString;
import com.jdc.util.DBConnection;
import com.jdc.util.PosException;

public class ItemRepoImpl implements ItemRepo{
	
	private static final ItemRepo INSTANCE = new ItemRepoImpl();
	
	private ItemRepoImpl() {}
	
	public static ItemRepo getInstance() {
		return INSTANCE;
	}

	private static final String SELECT = "select i.id id, i.name name,"
			+ " i.price price, i.remark remark, i.enable enable, c.id categoryId,"
			+ " c.name category from item i join category c on"
			+ " i.category_id = c.id ";
	
	private static final String INSERT = "insert into item(name, price, remark, enable, category_id) "
			+ "values(?, ?, ?, ?, ?)";
	
	@Override
	public void create(Item item) {
		
    	// TODO: validation
    	validateItem(item);
		
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement prep = conn.prepareStatement(INSERT)) {
			
			prep.setString(1, item.getName());
			prep.setInt(2, item.getPrice());
			prep.setString(3, item.getRemark());
			prep.setBoolean(4, item.isEnable());
			prep.setInt(5, item.getCategoryId());
			
			prep.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void validateItem(Item item) {
	
		if (item.getName().isEmpty()) {
			throw new PosException("Please enter item name!");
		}
		
		if (item.getPrice() == 0) {
			throw new PosException("Please enter unit price!");
		}
		
		if (item.getCategoryId() == 0) {
			throw new PosException("Please select category!");
		}
	}

	@Override
	public List<Item> findAll(Category c, String name, boolean enable) {
		
		// result 
		List<Item> items = new ArrayList<Item>();
		
		// dynamic query
		StringBuilder builder = new StringBuilder(SELECT);
		
		// to set parameters
		List<Object> params = new ArrayList<Object>();
		
		builder.append("where i.enable = ?");
		params.add(enable);
		
		if(null != c) {
			builder.append(" and c.id = ?");
			params.add(c.getId());
		}
		
		if(null != name) {
			builder.append(" and i.name like ?");
			params.add(name.concat("%"));
		}
		
//		MessageHandler.show("Query: " + builder.toString());
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement prep = conn.prepareStatement(builder.toString())) {

			// set parameters
			for(int i = 0; i < params.size(); i++)
				prep.setObject(i+1, params.get(i));
			
			// get results from databases
			ResultSet rs = prep.executeQuery();
			
			while(rs.next()) {
				items.add(getItem(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}

	private Item getItem(ResultSet rs) throws SQLException {
		Item item = new Item();
		
		item.setCategory(rs.getString("category"));
		item.setCategoryId(rs.getInt("categoryId"));
		item.setEnable(rs.getBoolean("enable"));
		item.setId(rs.getInt("id"));
		item.setName(rs.getString("name"));
		item.setPrice(rs.getInt("price"));
		item.setRemark(rs.getString("remark"));
		
		return item;
		
	}

	@Override
	public List<Item> find(String id, String name) {
		List<Item> list = new ArrayList<Item>();
		
		// validation
		if (!CheckString.empty(id) || !CheckString.empty(name)) {
			StringBuilder builder = new StringBuilder(SELECT);
			
			List<Object> params = new ArrayList<Object>();
			
			builder.append("where i.enable = ? ");
			params.add(true);
			
			if (!CheckString.empty(id)) {
				builder.append(" and i.id = ?");
				params.add(Integer.parseInt(id));
			}
			
			if (!CheckString.empty(name)) {
				builder.append(" and i.name like ?");
				params.add(name.concat("%"));
			}
			
			
			try (Connection conn = DBConnection.getConnection();
					PreparedStatement prep = conn.prepareStatement(builder.toString())) {
				
				for (int i = 0; i < params.size(); i++) {
					prep.setObject(i + 1, params.get(i));
				}
				
				ResultSet rs = prep.executeQuery();
				
				while(rs.next()) {
					list.add(getItem(rs));
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		return list;
	}

	@Override
	public void update(Item item) {
		String sql = "update item set name = ?, price = ?, remark = ?, enable = ? , category_id = ? where id = ?";
		
		validateItem(item);
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement prep = conn.prepareStatement(sql)) {
			
			
			prep.setString(1, item.getName());
			prep.setInt(2, item.getPrice());
			prep.setString(3, item.getRemark());
			prep.setBoolean(4, item.isEnable());
			prep.setInt(5, item.getCategoryId());
			prep.setInt(6, item.getId());
			
			prep.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(Item item) {
		String sql = "delete from item where id = ?";
		
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement prep = conn.prepareStatement(sql)) {
			
			
			prep.setInt(1, item.getId());
			prep.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}




















