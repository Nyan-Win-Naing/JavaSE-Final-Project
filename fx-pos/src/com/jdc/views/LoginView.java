package com.jdc.views;

import java.net.URL;
import java.util.ResourceBundle;

import com.jdc.security.POSEncryptor;
import com.jdc.util.LoginSetting;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;

public class LoginView implements Initializable{

    @FXML
    private TextField txtLoginId;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label message;
        
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	message.setText("");
    	
    	Platform.runLater(() -> {
    		message.getScene().getAccelerators().put(
    				new KeyCodeCombination(KeyCode.ESCAPE), this::close);
    		
    		message.getScene().getAccelerators().put(
    				new KeyCodeCombination(KeyCode.ENTER), this::login);
    	});
    }
    
    @FXML
    void close() {
    	Platform.exit();
    }

    @FXML
    void login() {
    	try {
    		LoginSetting.login(txtLoginId.getText(), 
    				POSEncryptor.encrypt(txtPassword.getText()));
			
    		MainView.show();
    		txtLoginId.getScene().getWindow().hide();
    		
		} catch (Exception e) {
			message.setText(e.getMessage());
		}
    }
}
