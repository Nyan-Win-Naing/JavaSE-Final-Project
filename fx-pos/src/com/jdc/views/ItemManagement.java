package com.jdc.views;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.jdc.entities.Category;
import com.jdc.entities.Item;
import com.jdc.repo.CategoryRepo;
import com.jdc.repo.ItemRepo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ItemManagement implements Initializable{

    @FXML
    private ComboBox<Category> category;

    @FXML
    private TextField name;

    @FXML
    private CheckBox enable;

    @FXML
    private TableView<Item> table;
    
    private CategoryRepo catRepo;
    private ItemRepo itemRepo;

    @FXML
    void addNew() {
    	ItemEdit.show(this::saveAction);
    }
    
    private void saveAction(Item item) {
    	
    	if (item.getId() == 0) {
    		itemRepo.create(item);
		} else {
			itemRepo.update(item);
		}
    	
    	search();
    }

    @FXML
    void search() {
    	table.getItems().clear();
    	List<Item> list = itemRepo.findAll(category.getValue(), name.getText(), enable.isSelected());
    	table.getItems().addAll(list);
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		catRepo = CategoryRepo.getInstance();
		itemRepo = ItemRepo.getInstance();
		
		category.getItems().addAll(catRepo.findAll());
		enable.setSelected(true);	
		createContextMenu();
		search();
	}

	private void createContextMenu() {
		MenuItem edit = new MenuItem("Edit");
		edit.setOnAction(event -> edit());
		
		MenuItem delete = new MenuItem("Delete");
		delete.setOnAction(event -> delete());
		
		ContextMenu ctx = new ContextMenu(edit, delete);
		table.setContextMenu(ctx);
	}

	private void delete() {
		Item item = table.getSelectionModel().getSelectedItem();
		
		itemRepo.delete(item);
		search();
	}

	private void edit() {
		Item item = table.getSelectionModel().getSelectedItem();
		
		ItemEdit.show(this::saveAction, item);		
	}

}
