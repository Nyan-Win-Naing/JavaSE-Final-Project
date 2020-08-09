package com.jdc.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Sale {

	private int id;
	private LocalDate saleDate;
	private LocalTime saleTime;
	private boolean enable;

	// not in database
	private List<SalesDetails> details;

	public Sale() {
		details = new ArrayList<>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(LocalDate saleDate) {
		this.saleDate = saleDate;
	}

	public LocalTime getSaleTime() {
		return saleTime;
	}

	public void setSaleTime(LocalTime saleTime) {
		this.saleTime = saleTime;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public List<SalesDetails> getDetails() {
		return details;
	}

	public void setDetails(List<SalesDetails> details) {
		this.details = details;
	}

	// custom method
	public int getSubTotal() {
		return details.stream().mapToInt(a -> a.getSubTotal()).sum();
	}
	
	public int getTax() {
		return details.stream().mapToInt(a -> a.getTax()).sum();
	}
	
	public int getTotal() {
		return details.stream().mapToInt(a -> a.getTotal()).sum();
	}
}
