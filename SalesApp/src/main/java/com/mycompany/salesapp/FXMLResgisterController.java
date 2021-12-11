package com.mycompany.salesapp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.fxml.FXMLLoader;

import javafx.stage.Window;
import javafx.stage.Stage;

import com.mycompany.services.ResgisterService;
import com.mycompany.pojo.User;
/**
 * FXML Controller class
 *
 * @author QUOC ANH
 */
public class FXMLResgisterController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML private TextField txtUsername;
    @FXML private PasswordField passwordField;
    @FXML private TextField txtPhone;
    @FXML private Label lblMessage;
    @FXML private Button btnRegister;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public  void ResgisterButton(ActionEvent Event) throws Exception
    {
        User user = new User(this.txtUsername.getText(), this.passwordField.getText(), this.txtPhone.getText());
        ResgisterService regisService = new ResgisterService();
        String result = regisService.validPasword(user.getPassword());
        if(!result.isEmpty()){
            lblMessage.setText(result);
            return;
        }

        result = regisService.validUsername(user.getUsername());
        if(!result.isEmpty()){
            lblMessage.setText(result);
            return;
        }

        result = regisService.validPhone(user.getPhone());
        if(!result.isEmpty()){
            lblMessage.setText(result);
            return;
        }
        
        if(!lblMessage.getText().isEmpty()){
            lblMessage.setText("");
        }

        regisService.insertUser(user);
        
        Stage stage = (Stage) btnRegister.getScene().getWindow();
        stage.close();      
    }
    
   
}
