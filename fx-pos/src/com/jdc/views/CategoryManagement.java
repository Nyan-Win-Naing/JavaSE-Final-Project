package com.jdc.views;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.jdc.entities.Category;
import com.jdc.repo.CategoryRepo;
import com.jdc.util.MessageHandler;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.TilePane;

public class CategoryManagement implements Initializable{

    @FXML
    private TilePane items;
    
    private CategoryRepo repo;
    
    @FXML
    void addNew() {
    	CategoryEdit.show(this::saveAction);   	
    }

    private void saveAction(String name)  {
    	repo.create(name);
    	loadCategoryBox();
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		repo = CategoryRepo.getInstance();
		loadCategoryBox();
	}
	
	private void delete(Node node) {
		Optional<ButtonType> answer = MessageHandler.confirm(
				"Are you sure want to delete ?");
		
		if(answer.get().equals(ButtonType.OK)) {
			int id = ((CategoryBox)node).getBoxId();
			repo.delete(id);
			loadCategoryBox();
		}
	}

	private void loadCategoryBox() {
		
		items.getChildren().clear();
		
		List<Category> list = repo.findAll();
		List<CategoryBox> boxes = list.stream()
				.map(cat -> new CategoryBox(cat))
				.collect(Collectors.toList());
		
		items.getChildren().addAll(boxes);
		
		// set confirmation alert on CategoryBox
		items.getChildren().forEach(node -> {
			node.setOnMouseClicked(event -> { 
				if(event.getClickCount() == 2) {
					delete(node);
				}
			});
		});
	}

	@SuppressWarnings("all")
	private void showAlert() {
		List<Category> list = repo.findAll();
		String output = String.format("Size: %d%n", list.size());
		
		list.forEach(cat -> {
			String s = String.format("ID: %d%nName: %s%n%n", cat.getId(), cat.getName());
			output.concat(s);
		});
		
		MessageHandler.show(output);
		MessageHandler.bringToFront();
	}
	
}
