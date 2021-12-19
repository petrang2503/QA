/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.salesapp;
import com.mycompany.pojo.Customer;
import com.mycompany.pojo.Order;
import com.mycompany.pojo.OrderDetails;
import com.mycompany.services.SalesOrderService;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author QUOC ANH
 */
public class FXMLSalesOrderController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML private DatePicker createdDate;
    @FXML private TextField txtQuantily;
    
    @FXML private ComboBox<String> cbCustomerUser;
    @FXML private ComboBox<String> cbProductName;
    
    @FXML private Label lblPhone;
    @FXML private Label lblAddress;
    @FXML private Label lblCode;
    @FXML private Label lbPrice;
    @FXML private Label lbTotalBill;
    @FXML private Label lblMessage;
    
    @FXML private TableView<OrderDetails> tblOrderDetail;
    
    private static final DecimalFormat formatPrice = new DecimalFormat("###,##0");
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //Code              
        String code = "DH"+String.valueOf(System.currentTimeMillis());
        lblCode.setText(code);
        
        loadTable();
        
        try{
            //Load List Customer
            ObservableList<String> customers = (SalesOrderService.getListCustomerUserName());
            this.cbCustomerUser.getItems().addAll(customers);
        } catch(SQLException ex){
            System.err.println(ex.getMessage());
        }
        
        try{
            //Load List Product
            ObservableList<String> products = (SalesOrderService.getListProductName());
            this.cbProductName.getItems().addAll(products);
        } catch(SQLException ex){
            System.err.println(ex.getMessage());
        }
        
        createdDate.setValue(LocalDate.now());
    }    
    
    public void changeComboboxCustomer(ActionEvent Event) throws Exception{
        String value = cbCustomerUser.getValue();
        String id = value.split(",")[ 0 ];
        Customer customerInfo = SalesOrderService.getCustomerUserInfo(id);
        this.lblPhone.setText(customerInfo.getCustomerPhone());
        this.lblAddress.setText(customerInfo.getCustomerAddress());
    }
   
    public void createOrder(ActionEvent Event) throws Exception{
        
        String code = lblCode.getText();
                
        Order order = new Order();
        order.setOrderCode(code);
        
        String date = createdDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        order.setCreatedDate(date);
        
        String value = cbCustomerUser.getValue();
        String customerUserId = value.split(",")[ 0 ];
        order.setCustomerUserId(customerUserId);
        
        Long currently = System.currentTimeMillis();
        order.setExpiredDate(currently + 1800000);
        
        //Details
        List<OrderDetails> details = tblOrderDetail.getItems();
        String error = SalesOrderService.CreateOrder(order, details);
        
        if(!error.isEmpty()){
            lblMessage.setText(error);
            return;
        }
        
        goBackOrderList();
    }
    
    private void loadTable(){
        TableColumn productId = new TableColumn("Id sản phẩm");
        productId.setCellValueFactory(new PropertyValueFactory("productId"));
        
        TableColumn productName = new TableColumn("Tên sản phẩm");
        productName.setCellValueFactory(new PropertyValueFactory("productName"));
        
        TableColumn price = new TableColumn("Đơn giá");
        price.setCellValueFactory(new PropertyValueFactory("price"));
        
        TableColumn quantily = new TableColumn("Số lượng");
        quantily.setCellValueFactory(new PropertyValueFactory("quantily"));
        
        TableColumn amountTotal = new TableColumn("Thành tiền");
        amountTotal.setCellValueFactory(new PropertyValueFactory("amountTotal"));
        
        this.tblOrderDetail.getColumns().addAll(productId, productName, price, quantily, amountTotal);
    }
    
    public void changeComboboxProduct(ActionEvent Event) throws Exception{
        String value = cbProductName.getValue();
        String id = value.split(",")[ 0 ];
        
        Double price = SalesOrderService.getProductPrice(id);
        this.lbPrice.setText(formatPrice.format(price));
    }
    
    public void handleAddRowBtn(ActionEvent Event) throws Exception{
        
        String value = cbProductName.getValue();
        if(value.isEmpty()){
            lblMessage.setText("Vui lòng chọn sản phẩm");
            return;
        }
                
        String productName = value.split(",")[1];
        String productId = value.split(",")[0];
        
        String price = this.lbPrice.getText();
        String quantily = this.txtQuantily.getText();
        
        if(price.isEmpty() || quantily.isEmpty()){
            lblMessage.setText("Vui lòng kiểm tra thông tin nhập chi tiết đơn hàng.");
            return;
        }
                        
        OrderDetails detail = new OrderDetails();
        detail.setProductId(productId);
        detail.setProductName(productName);
        detail.setPrice(price);
        detail.setQuantily(quantily);
                
        double total = Double.valueOf(price.replaceAll(",", "")) * Double.valueOf(quantily);
        detail.setAmountTotal(formatPrice.format(total));
        
        tblOrderDetail.getItems().add(detail);
        
        this.cbProductName.setValue("");
        this.txtQuantily.clear();
        this.lbPrice.setText("");
        
        String totalBill = this.lbTotalBill.getText().replaceAll(",", "");
        double toltalBillBefore = totalBill.equals("") ? 0.0d : Double.valueOf(totalBill);
        double currentlyTotal = toltalBillBefore + total;
        
        this.lbTotalBill.setText(formatPrice.format(currentlyTotal));     
        
    }
    
    public void handleRemoveRowBtn(ActionEvent Event) throws Exception{
        lblMessage.setText("");
        OrderDetails detail = tblOrderDetail.getSelectionModel().getSelectedItem();
         
        double toltalBillBefore = Double.valueOf(this.lbTotalBill.getText().replaceAll(",", ""));
        double currentlyTotal = 0.0d;
        if(toltalBillBefore != 0.0d){
            
            currentlyTotal = toltalBillBefore - Double.valueOf(detail.getAmountTotal().replaceAll(",", ""));
        }
       
        this.lbTotalBill.setText(formatPrice.format(currentlyTotal));     
        tblOrderDetail.getItems().removeAll(detail);
    }
        
    public void OrderListButton(ActionEvent event) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("FXMLOrderList.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 768, 541);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Quản lý đơn hàng");
        stage.show();
    }
    
    
    public void goBackOrderList() throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("FXMLOrderList.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 768, 541);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Quản lý đơn hàng");
        stage.show();
    }
}
