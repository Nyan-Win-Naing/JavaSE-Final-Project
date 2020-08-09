package com.jdc.repo;

import java.util.List;

import com.jdc.entities.Category;
import com.jdc.entities.Item;
import com.jdc.repo.impl.ItemRepoImpl;

public interface ItemRepo {
	
	static ItemRepo getInstance() {
		return ItemRepoImpl.getInstance();
	}

	List<Item> findAll(Category c, String name, boolean enable);
	
	List<Item> find(String id, String name);

	void create(Item item);
	
	void update(Item item);
	
	void delete(Item item);
	
}
