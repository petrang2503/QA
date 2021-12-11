package com.mycompany.salesapp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.mycompany.services.ProductsService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import java.sql.SQLException;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableColumn;

import com.mycompany.pojo.Product;

/**
 * FXML Controller class
 *
 * @author QUOC ANH
 */
public class FXMLProductsController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML private TextField txtProductCode;
    @FXML private TextField txtProductName;
    @FXML private TextField txtPurchasePrice;
    @FXML private TextField txtPrice;
    @FXML private TextField txtKeyword;

    @FXML private TableView<Product> tbProduct;
    @FXML private Label lblMessage;
    @FXML private Button btnAddProduct;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try{
            //Load Products
            this.loadProduct();
        } catch(SQLException ex){
            System.err.println(ex.getMessage());
        }

        //select row on Table View
        this.tbProduct.setRowFactory(et -> {
            TableRow row = new TableRow();
            row.setOnMouseClicked(r -> {
                Product product = this.tbProduct.getSelectionModel().getSelectedItem();
                this.txtProductCode.setText(product.getProductCode());
                this.txtProductName.setText(product.getProductName());
                this.txtPurchasePrice.setText(String.valueOf(product.getPurchasePrice()));
                this.txtPrice.setText(String.valueOf(product.getPrice()));
            });
           return row;
        });
    }    
    
    public void AddProduct(ActionEvent Event) throws Exception
    {
       
        Product product = new Product();
        product.setProductCode(this.txtProductCode.getText());
        product.setProductName(this.txtProductName.getText());
        product.setPurchasePrice(this.txtPurchasePrice.getText() == null ? null : Double.parseDouble(this.txtPurchasePrice.getText()));
        product.setPrice(this.txtPurchasePrice.getText() == null ? null : Double.parseDouble(this.txtPrice.getText()));

        String result = ProductsService.validationRequest(product);
        if(!result.isEmpty())
         {
            lblMessage.setText(result);
            return;
        }

        result = ProductsService.insertProduct(product);
        this.tbProduct.getItems().clear();
        this.tbProduct.setItems(FXCollections.observableArrayList(ProductsService.getProducts("")));

        lblMessage.setText(result);
    }

    public void searchProductName(ActionEvent Event) throws Exception
    {
        this.tbProduct.getItems().clear();
        this.tbProduct.setItems(FXCollections.observableArrayList(ProductsService.getProducts(this.txtKeyword.getText())));
    }
    
    public void deleteProduct(ActionEvent Event) throws Exception
    {
        
        Product product = new Product();
        product.setProductCode(this.txtProductCode.getText());
        
        boolean isExist = ProductsService.isExistProductId(product.getProductCode());
        if(!isExist){
            lblMessage.setText("Mã sản phẩm không tồn tại.");
            return ;
        }

        String result = ProductsService.deleteProduct(product.getProductCode());
        
        this.tbProduct.getItems().clear();
        this.tbProduct.setItems(FXCollections.observableArrayList(ProductsService.getProducts("")));
        lblMessage.setText(result);
    }

    public void updateProduct(ActionEvent Event) throws Exception
    {
        
        Product product = new Product();
        product.setProductCode(this.txtProductCode.getText());
        product.setProductName(this.txtProductName.getText());
        product.setPurchasePrice(this.txtPurchasePrice.getText() == null ? null : Double.parseDouble(this.txtPurchasePrice.getText()));
        product.setPrice(this.txtPurchasePrice.getText() == null ? null : Double.parseDouble(this.txtPrice.getText()));
        
        boolean isExist = ProductsService.isExistProductId(product.getProductCode());
        if(!isExist){
            lblMessage.setText("Mã sản phẩm không tồn tại.");
            return ;
        }

        String result = ProductsService.updateProduct(product);
        this.tbProduct.setItems(FXCollections.observableArrayList(ProductsService.getProducts("")));
        lblMessage.setText(result);
    }
    
    private void loadProduct()throws SQLException{
        TableColumn productCode = new TableColumn("Mã sản phẩm");
        productCode.setCellValueFactory(new PropertyValueFactory("productCode"));
        productCode.setPrefWidth(100);

        TableColumn productName = new TableColumn("Tên sản phẩm");
        productName.setCellValueFactory(new PropertyValueFactory("productName"));
        productName.setPrefWidth(200);

        TableColumn purchasePrice = new TableColumn("Giá nhập");
        purchasePrice.setCellValueFactory(new PropertyValueFactory("purchasePrice"));
        purchasePrice.setPrefWidth(100);

        TableColumn price = new TableColumn("Giá bán");
        price.setCellValueFactory(new PropertyValueFactory("price"));
        price.setPrefWidth(100);

        this.tbProduct.getColumns().addAll(productCode, productName, purchasePrice, price);
        this.tbProduct.setItems(FXCollections.observableArrayList(ProductsService.getProducts("")));
    }
    
}
