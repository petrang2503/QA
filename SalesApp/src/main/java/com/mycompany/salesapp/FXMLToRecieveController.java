/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.salesapp;

import com.mycompany.pojo.Product;
import com.mycompany.pojo.ToRecieveBean;
import com.mycompany.services.SalesOrderService;
import com.mycompany.services.ToRecieverService;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author QUOC ANH
 */
public class FXMLToRecieveController implements Initializable{
    
    @FXML private ComboBox<String> cbProductName;
    
    @FXML private TableView<ToRecieveBean> tbProduct;
        
     @FXML private Label lblMessage;
     @FXML private Label lblNumberOfWarehouses;
     @FXML private Label lblIdRow;
     @FXML private TextField txtnumberOfRecieve;
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
               
        try{
            //Load List Product
            ObservableList<String> products = (ToRecieverService.getListProductName());
            this.cbProductName.getItems().addAll(products);
        } catch(SQLException ex){
            System.err.println(ex.getMessage());
        }
        
        try{
            //Load Recieve
            this.loadRecieve();
        } catch(SQLException ex){
            System.err.println(ex.getMessage());
        }
        
        //select row on Table View
        this.tbProduct.setRowFactory(et -> {
            TableRow row = new TableRow();
            row.setOnMouseClicked(r -> {
                ToRecieveBean product = this.tbProduct.getSelectionModel().getSelectedItem();
                this.cbProductName.setValue(product.getProductId()+","+product.getProductName());
                this.txtnumberOfRecieve.setText(String.valueOf(product.getNumberOfRecieve()));
                this.lblIdRow.setText(String.valueOf(product.getId()));
            });
           return row;
        });
    }  
    
    public void loadRecieve() throws SQLException{
        
        TableColumn id = new TableColumn("ID");
        id.setCellValueFactory(new PropertyValueFactory("id"));
        id.setPrefWidth(40);
        
        TableColumn createdDate = new TableColumn("Ngày nhập kho");
        createdDate.setCellValueFactory(new PropertyValueFactory("createdDate"));
        createdDate.setPrefWidth(100);
        
        TableColumn productCode = new TableColumn("Id sản phẩm");
        productCode.setCellValueFactory(new PropertyValueFactory("productId"));
        productCode.setPrefWidth(100);

        TableColumn productName = new TableColumn("Tên sản phẩm");
        productName.setCellValueFactory(new PropertyValueFactory("productName"));
        productName.setPrefWidth(200);

        TableColumn numberToRecieve = new TableColumn("Số lượng nhập");
        numberToRecieve.setCellValueFactory(new PropertyValueFactory("numberOfRecieve"));
        numberToRecieve.setPrefWidth(100);
        
        this.tbProduct.getColumns().addAll(id, createdDate, productCode, productName, numberToRecieve);
    }
    
    public void searchProduct() throws SQLException{
        String cbStr = cbProductName.getValue();
        if(cbStr == null || cbStr.isEmpty()){
            lblMessage.setText("Vui lòng chọn sản phẩm!");
           return;
        }
        
         lblMessage.setText("");
        String productId = cbStr.split(",")[0];
        
        Product pro = ToRecieverService.isExistProductId(productId);
        lblNumberOfWarehouses.setText(String.valueOf(pro.getNumberToRecieve()));
                
        this.tbProduct.getItems().clear();
        this.tbProduct.setItems(FXCollections.observableArrayList(ToRecieverService.getProducts(productId)));
    }
    
    public void createToRecieveForProduct() throws SQLException{
        String cbStr = cbProductName.getValue();
        String numberOfRecieve = txtnumberOfRecieve.getText();
        if(cbStr == null || cbStr.isEmpty()){
            lblMessage.setText("Vui lòng chọn sản phẩm!");
           return;
        }
        
        lblMessage.setText("");
        String productId = cbStr.split(",")[0];
        String result = ToRecieverService.insertRecieveProduct(productId, Long.valueOf(numberOfRecieve));
        lblMessage.setText(result);
        
        //Load
        this.tbProduct.getItems().clear();
        this.tbProduct.setItems(FXCollections.observableArrayList(ToRecieverService.getProducts(productId)));
    }
    
     public void delete(ActionEvent Event) throws Exception
    {
        String cbStr = cbProductName.getValue();
        String id = lblIdRow.getText();
        
        if(cbStr.isEmpty()){
            lblMessage.setText("Vui lòng chọn dòng cần chỉnh sửa!");
           return;
        }
        
        lblMessage.setText("");
        String productId = cbStr.split(",")[0];
        Product product = ToRecieverService.isExistProductId(productId);
        if(product == null){
            lblMessage.setText("Mã sản phẩm không tồn tại.");
            return ;
        }

        String result = ToRecieverService.delete(id, productId);
        
        this.tbProduct.getItems().clear();
        this.tbProduct.setItems(FXCollections.observableArrayList(ToRecieverService.getProducts(productId)));
        lblMessage.setText(result);
    }

    public void update(ActionEvent Event) throws Exception
    {
        String id = lblIdRow.getText();
        String cbStr = cbProductName.getValue();
         if(cbStr.isEmpty()){
            lblMessage.setText("Vui lòng chọn dòng cần chỉnh sửa!");
           return;
        }
        
        lblMessage.setText("");
        String productId = cbStr.split(",")[0];
          
        Product pro = ToRecieverService.isExistProductId(productId);
        if(pro == null){
            lblMessage.setText("Mã sản phẩm không tồn tại.");
            return ;
        }

        String result = ToRecieverService.update(id, productId, Long.valueOf(this.txtnumberOfRecieve.getText()));
        
        this.tbProduct.setItems(FXCollections.observableArrayList(ToRecieverService.getProducts(productId)));
        lblMessage.setText(result);
    }
}
