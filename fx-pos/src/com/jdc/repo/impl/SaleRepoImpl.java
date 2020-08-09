package com.jdc.repo.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.jdc.entities.Category;
import com.jdc.entities.Sale;
import com.jdc.entities.SalesDetails;
import com.jdc.repo.SaleRepo;
import com.jdc.util.CheckString;
import com.jdc.util.DBConnection;
import com.jdc.util.MessageHandler;
import com.jdc.util.PosException;

public class SaleRepoImpl implements SaleRepo {

	private static final String INSERT_SALE = "insert into sales(sales_date, sales_time, enable) values (?, ?, ?)";
	private static final String INSERT_DETAIL = "insert into sales_details(item_id, sales_id, unit_price, quantity, tax)"
			+ "values (?, ?, ?, ?, ?)";
	
	private static final String SELECT = "select d.id id, s.sales_date saleDate, s.sales_time saleTime, i.id itemId," + 
			"i.name itemName, d.quantity quantity, d.unit_price unitPrice, " + 
			"d.tax tax, d.sales_id saleId, c.name category from sales_details d " + 
			"join sales s on d.sales_id = s.id " + 
			"join item i on d.item_id = i.id " + 
			"join category c on i.category_id = c.id where 1 = 1";
	
	private static final SaleRepo INSTANCE = new SaleRepoImpl();

	SaleRepoImpl() {}

	public static SaleRepo getInstance() {
		return INSTANCE;
	}

	@Override
	public void create(Sale sale) {
		validate(sale);
		
		
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement prepSale = conn.prepareStatement(INSERT_SALE, 
						Statement.RETURN_GENERATED_KEYS);
				PreparedStatement prepDetail = conn.prepareStatement(INSERT_DETAIL)) {
			
			prepSale.setDate(1, Date.valueOf(LocalDate.now()));
			prepSale.setTime(2, Time.valueOf(LocalTime.now()));
			prepSale.setBoolean(3, true);
			
			int result = prepSale.executeUpdate();
			
			if (result == 1) {
				ResultSet rs = prepSale.getGeneratedKeys();
				while(rs.next()) {
					int saleId = rs.getInt(1);
					
					for (SalesDetails detail : sale.getDetails()) {
						prepDetail.setInt(1, detail.getItemId());
						prepDetail.setInt(2, saleId);
						prepDetail.setInt(3, detail.getUnitPrice());
						prepDetail.setInt(4, detail.getQuantity());
						prepDetail.setInt(5, detail.getTax());
						
						prepDetail.executeUpdate();
					}
					
					break;
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void validate(Sale sale) {
		if (sale.getDetails().isEmpty()) {
			throw new PosException("Please add item into cart !!!");
		}
	}

	@Override
	public List<SalesDetails> search(Category c, String item, LocalDate start, LocalDate end) {
		
		List<SalesDetails> list = new ArrayList<SalesDetails>();
		StringBuilder query = new StringBuilder(SELECT);
		List<Object> params = new ArrayList<Object>();
		
		if (null != c) {
			query.append(" and c.id = ?");
			params.add(c.getId());
		}
		
		if (!CheckString.empty(item)) {
			query.append(" and i.name like ?");
			params.add(item.concat("%"));
		}
		
		if (null != start) {
			query.append(" and s.sales_date >= ?");
			params.add(Date.valueOf(start));
		}
		
		if (null != end) {
			query.append(" and s.sales_date <= ?");
			params.add(Date.valueOf(end));
		}
		
//		System.out.printf("%nQUERY: %s%n", query.toString());
		MessageHandler.show(String.format("%nQUERY: %s%n", query.toString()));
		
	
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement prep = conn.prepareStatement(query.toString())) {
			
			for (int i = 0; i < params.size(); i++) {
				prep.setObject(i + 1, params.get(i));
			}
			
			ResultSet rs = prep.executeQuery();
			
			while(rs.next()) {
				SalesDetails details = new SalesDetails();
				
				details.setId(rs.getInt("id"));
				details.setCategory(rs.getString("category"));
				details.setItemId(rs.getInt("itemId"));
				details.setItemName(rs.getString("itemName"));
				details.setQuantity(rs.getInt("quantity"));
				details.setSaleDate(rs.getDate("saleDate").toLocalDate());
				details.setTime(rs.getTime("saleTime").toLocalTime());
				details.setSaleId(rs.getInt("saleId"));
				details.setTax(rs.getInt("tax"));
				details.setUnitPrice(rs.getInt("unitPrice"));
				
				list.add(details);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}

}
