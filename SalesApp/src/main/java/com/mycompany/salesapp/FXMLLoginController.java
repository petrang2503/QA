/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.salesapp;

import com.mycompany.configs.JdbcUtils;
import com.mycompany.pojo.User;
import com.mycompany.services.LoginService;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author QUOC ANH
 */
public class FXMLLoginController implements Initializable {

    @FXML private TextField txtUsername;
    @FXML private PasswordField passwordField;
    @FXML private Label lblMessage;

    @FXML private Button btnLogin;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public  void LoginButton(ActionEvent Event) throws Exception
    {
        
       User user = new User(this.txtUsername.getText(), this.passwordField.getText());
       LoginService loginService = new LoginService();
       String result = loginService.checkLogin(user);
       if(!result.isEmpty()){
           lblMessage.setText(result);
           return;
       } 
       
       FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("FXMLMain.fxml"));
       Scene scene = new Scene(fxmlLoader.load(),640, 480);
       Stage stageNew = new Stage();
       stageNew.setScene(scene);
       stageNew.setTitle("Trang chủ");
       stageNew.show();

       Stage stage = (Stage) btnLogin.getScene().getWindow();
       stage.close(); 
    }
    
    public  void ResgisterButton(ActionEvent Event) throws Exception
    {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("FXMLResgister.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),640, 480);
        Stage stage= new Stage();
        stage.setScene(scene);
        stage.setTitle("Trang Đăng Ký");
        stage.show();
               
    }
}
