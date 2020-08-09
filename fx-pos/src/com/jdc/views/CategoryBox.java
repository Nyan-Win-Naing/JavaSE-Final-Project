package com.jdc.views;

import com.jdc.entities.Category;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class CategoryBox extends HBox{

	private int boxId;
	
	public CategoryBox(Category c) {
		
		boxId = c.getId();
		Label id = new Label(String.valueOf(c.getId()));
		Label name = new Label(c.getName());
		
		getChildren().addAll(id, name);
		
		getStyleClass().add("wid-200");
		getStyleClass().add("sp-20");
		getStyleClass().add("pad-20");
		getStyleClass().add("primary");
		getStyleClass().add("al-center-left");
		
		id.getStyleClass().add("white-text");
		name.getStyleClass().add("white-text");
		
		setCursor(Cursor.HAND);
	}

	public int getBoxId() {
		return boxId;
	}
	
}
