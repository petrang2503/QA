package com.mycompany.salesapp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author QUOC ANH
 */
public class FXMLMainController implements Initializable{

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        // TODO
    }

    public void NewProductButton(ActionEvent Event) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("FXMLProducts.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Trang Sản Phẩm");
        stage.show();

//               stage = (Stage) btnNewProduct.getScene().getWindow();
//            stage.close();   
    }

    public void NewCustomerButton(ActionEvent Event) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("FXMLCustomer.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Trang Khách hàng");
        stage.show();
//               
//               stage = (Stage) btnNewCustomer.getScene().getWindow();
//            stage.close();   

    }
    
    public void OrderListButton(ActionEvent event) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("FXMLOrderList.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 768, 541);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Quản lý đơn hàng");
        stage.show();
    }
}
