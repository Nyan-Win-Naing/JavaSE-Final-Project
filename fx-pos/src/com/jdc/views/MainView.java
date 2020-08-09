package com.jdc.views;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainView implements Initializable {

	@FXML
	private HBox toolsBar;
	@FXML
	private StackPane contentView;

	@FXML
	void signOut() {
		Platform.exit();
	}

	
	private void navigate(String fxml) {
		try {
			Parent layout = FXMLLoader.load(getClass().getResource(fxml));
			contentView.getChildren().clear();
			contentView.getChildren().add(layout);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void show() {
		try {
			Parent root = FXMLLoader.load(MainView.class.getResource("MainView.fxml"));
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.getIcons().add(new Image(new FileInputStream("point-of-sale.png")));
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	enum Mapper {
		Pos("Pos.fxml"),
		Reports("SalesReport.fxml"),
		Items("ItemManagement.fxml"),
		Categories("CategoryManagement.fxml");
		
		private String viewName;

		private Mapper(String viewName) {
			this.viewName = viewName;
		}

		public String getViewName() {
			return viewName;
		}

	}

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		toolsBar.getChildren().stream()
			.filter(node -> node instanceof VBox)
			.map(node -> (VBox)node)
			.forEach(vbox -> {
				String id = vbox.getId();
				Mapper mapper = Mapper.valueOf(id);
				
				vbox.setOnMouseEntered(event -> themeDark(vbox));
				vbox.setOnMouseClicked(event -> {
					navigate(mapper.getViewName());
									
					// TODO: add onClick effect
					toolsBar.getChildren().forEach(node -> {
						if (node.getStyleClass().contains("active")) {
							node.getStyleClass().remove("active");
							themeLight((VBox)node);
						}
					});
					
					themeDark(vbox);
					vbox.getStyleClass().add("active");
					
				});
				
				
				vbox.setOnMouseExited(event -> themeLight(vbox));
				
		});
		
		// default view when application start
		navigate(Mapper.Pos.getViewName());
	}
	
	private void themeLight(VBox box) {
		if (!box.getStyleClass().contains("active")) {
			box.getStyleClass().remove("secondary");
			box.getStyleClass().add("acent");
			
			box.getChildren().forEach(node -> {
				node.getStyleClass().remove("white-text");
				node.getStyleClass().add("black-text");
			});
		}
	}
	
	private void themeDark(VBox box) {
		if (!box.getStyleClass().contains("active")) {
			box.getStyleClass().remove("acent");
			box.getStyleClass().add("secondary");
			
			box.getChildren().forEach(node -> {
				node.getStyleClass().remove("black-text");
				node.getStyleClass().add("white-text");
			});
		}
	}

}
