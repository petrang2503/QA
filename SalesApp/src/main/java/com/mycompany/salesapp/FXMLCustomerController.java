/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.salesapp;

import com.mycompany.pojo.Customer;

import com.mycompany.services.CustomerService;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author QUOC ANH
 */
public class FXMLCustomerController implements Initializable{

    /**
     * Initializes the controller class.
     */
    @FXML
    private TextField txtCustomerCode;
    @FXML
    private TextField txtCustomerName;
    @FXML
    private TextField txtNumber;
    @FXML
    private TextField txtAddress;
    @FXML
    private TextField txtKeyword;
    @FXML
    private Label lblMessage;
    @FXML
    private Button btnAddCustomer;

    @FXML
    private TableView<Customer> tbCustomer;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        try{
            //Load Products
            this.loadCustomer();
        }catch(SQLException ex){
            System.err.println(ex.getMessage());
        }

        //select row on Table View
        this.tbCustomer.setRowFactory(et->{
            TableRow row = new TableRow();
            row.setOnMouseClicked(r->{
                Customer customer = this.tbCustomer.getSelectionModel().getSelectedItem();
                this.txtCustomerCode.setText(customer.getCustomerCode());
                this.txtCustomerName.setText(customer.getCustomerName());
                this.txtNumber.setText(customer.getCustomerPhone());
                this.txtAddress.setText(customer.getCustomerAddress());
            });
            return row;
        });
    }

    public void AddCustomer(ActionEvent Event) throws Exception{

        Customer customer = new Customer();
        customer.setCustomerCode(this.txtCustomerCode.getText());
        customer.setCustomerName(this.txtCustomerName.getText());
        customer.setCustomerPhone(this.txtNumber.getText());
        customer.setCustomerAddress(this.txtAddress.getText());

        String result = CustomerService.validationRequest(customer);
        if( !result.isEmpty() ){
            lblMessage.setText(result);
            return;
        }

        result = CustomerService.validPhone(customer.getCustomerPhone());
        if( !result.isEmpty() ){
            lblMessage.setText(result);
            return;
        }

        result = CustomerService.insertCustomer(customer);
        this.tbCustomer.getItems().clear();
        this.tbCustomer.setItems(FXCollections.observableArrayList(CustomerService.getCustomers("")));

        lblMessage.setText(result);
    }

    public void searchCustomerName(ActionEvent Event) throws Exception{
        this.tbCustomer.getItems().clear();
        this.tbCustomer.setItems(FXCollections.observableArrayList(CustomerService.getCustomers(this.txtKeyword.getText())));
    }

    public void deleteProduct(ActionEvent Event) throws Exception{

        Customer customer = new Customer();
        customer.setCustomerCode(this.txtCustomerCode.getText());

        boolean isExist = CustomerService.isExistCustomerId(customer.getCustomerCode());
        if( !isExist ){
            lblMessage.setText("Mã khách hàng không tồn tại.");
            return;
        }

        String result = CustomerService.deleteProduct(customer.getCustomerCode());

        this.tbCustomer.getItems().clear();
        this.tbCustomer.setItems(FXCollections.observableArrayList(CustomerService.getCustomers("")));
        lblMessage.setText(result);
    }

    public void updateCustomer(ActionEvent Event) throws Exception{

        Customer customer = new Customer();
        customer.setCustomerCode(this.txtCustomerCode.getText());
        customer.setCustomerName(this.txtCustomerName.getText());
        customer.setCustomerPhone(this.txtNumber.getText());
        customer.setCustomerAddress(this.txtAddress.getText());

        boolean isExist = CustomerService.isExistCustomerId(customer.getCustomerCode());
        if( !isExist ){
            lblMessage.setText("Mã khách hàng không tồn tại.");
            return;
        }

        String checkresult = CustomerService.validPhone(customer.getCustomerPhone());
        if( !checkresult.isEmpty() ){
            lblMessage.setText(checkresult);
            return;
        }

        String result = CustomerService.updateCustomer(customer);
        this.tbCustomer.setItems(FXCollections.observableArrayList(CustomerService.getCustomers("")));
        lblMessage.setText(result);
    }

    private void loadCustomer() throws SQLException{
        TableColumn customerCode = new TableColumn("Mã khách hàng");
        customerCode.setCellValueFactory(new PropertyValueFactory("customerCode"));
        customerCode.setPrefWidth(100);

        TableColumn customerName = new TableColumn("Tên khách hàng");
        customerName.setCellValueFactory(new PropertyValueFactory("customerName"));
        customerName.setPrefWidth(200);

        TableColumn customerNumber = new TableColumn("Số điện thoại");
        customerNumber.setCellValueFactory(new PropertyValueFactory("customerPhone"));
        customerNumber.setPrefWidth(100);
        TableColumn customerAddress = new TableColumn("Địa chỉ");
        customerAddress.setCellValueFactory(new PropertyValueFactory("customerAddress"));
        customerAddress.setPrefWidth(100);

        this.tbCustomer.getColumns().addAll(customerCode, customerName, customerNumber, customerAddress);
        this.tbCustomer.setItems(FXCollections.observableArrayList(CustomerService.getCustomers("")));
    }
}
