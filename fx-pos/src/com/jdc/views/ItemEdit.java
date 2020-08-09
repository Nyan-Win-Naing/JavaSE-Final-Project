package com.jdc.views;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.jdc.entities.Category;
import com.jdc.entities.Item;
import com.jdc.repo.CategoryRepo;
import com.jdc.util.MessageHandler;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ItemEdit implements Initializable {

    @FXML
    private Label message;
    @FXML
    private ComboBox<Category> category;
    @FXML
    private TextField name;
    @FXML
    private TextField price;
    @FXML
    private TextArea remark;
    
    private Consumer<Item> saveHandler;
    private CategoryRepo repo;
    
    private Item item;
    private int i;
    
    @FXML
    void close() {
    	message.getScene().getWindow().hide();
    }

    @FXML
    void save() {
    	try {
        	// get view data
//        	Item item = new Item();
    		
    		if (item == null) {
				item = new Item();
			}
    		
        	item.setName(name.getText());
        	item.setPrice(Integer.parseInt(price.getText()));
        	item.setRemark(remark.getText());
        	item.setCategory(category.getValue().getName());
        	item.setCategoryId(category.getValue().getId());
        	Category c1 = category.getValue();
        	Category c2 = category.getValue();
        	System.out.println(c1 == c2);
        	item.setEnable(true);
        	
        	// save in database
        	saveHandler.accept(item);
        	close();
		} catch (Exception e) {
//			message.setText(e.getMessage());
			e.printStackTrace();
		}
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		repo = CategoryRepo.getInstance();
		category.getItems().addAll(repo.findAll());
	}

	public static void show(Consumer<Item> saveHandler) {
		try {
			FXMLLoader loader = new FXMLLoader(
					ItemEdit.class.getResource("ItemEdit.fxml"));
			
			Parent root = loader.load();
			ItemEdit controller = loader.getController();
			controller.saveHandler = saveHandler;
			
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.setTitle("Add New Item");
			stage.show();
			
		} catch (Exception e) {
			MessageHandler.show(e);
		}
	}
	
	public static void show(Consumer<Item> saveHandler, Item item) {
		try {
			FXMLLoader loader = new FXMLLoader(
					ItemEdit.class.getResource("ItemEdit.fxml"));
			
			Parent root = loader.load();
			
			ItemEdit controller = loader.getController();
			controller.saveHandler = saveHandler;
			
			controller.setItem(item);
			
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.setTitle("Edit Item");
			stage.show();
		} catch (Exception e) {
			MessageHandler.show(e);
		}
	}

	private void setItem(Item item) {
		// set instance state
		this.item = item;
		
		// set data into UI
		category.setValue(getCatInstance(item.getCategoryId()));
		name.setText(item.getName());
		price.setText(String.valueOf(item.getPrice()));
		remark.setText(item.getRemark());
		
	}

	private Category getCatInstance(int categoryId) {
		List<Category> catList = repo.findAll();
		
		for (Category c : catList) {
			if (categoryId == c.getId()) {
				return c;
			}
		}
		return null;
	}

}
