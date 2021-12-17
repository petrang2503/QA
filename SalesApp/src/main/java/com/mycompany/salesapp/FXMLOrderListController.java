/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.salesapp;

import com.mycompany.pojo.Order;
import com.mycompany.pojo.OrderDetails;
import com.mycompany.pojo.SearchBean;
import com.mycompany.services.OrderListService;
import com.mycompany.services.SalesOrderService;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
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
 *
 * @author QuocAnh
 */
public class FXMLOrderListController implements Initializable{

    /**
     * Initializes the controller class.
     */
    @FXML
    private DatePicker dbFromDate;
    @FXML
    private DatePicker dbToDate;

    @FXML
    private TextField txtOrderCode;

    @FXML
    private ComboBox<String> cbCustomerUser;

    @FXML
    private TableView<Order> tblOrderList;

    @FXML
    private Label lblMessage;

    private static final DecimalFormat formatPrice = new DecimalFormat("###,##0");

    @Override
    public void initialize(URL url, ResourceBundle rb){
        try{
            //Load List Customer
            ObservableList<String> customers = (SalesOrderService.getListCustomerUserName());
            this.cbCustomerUser.getItems().addAll(customers);
        }catch(SQLException ex){
            System.err.println(ex.getMessage());
        }

        try{
            //Load Orders
            this.loadOrders();
        }catch(SQLException ex){
            System.err.println(ex.getMessage());
        }

    }

    private void loadOrders() throws SQLException{
        TableColumn id = new TableColumn("ID");
        id.setCellValueFactory(new PropertyValueFactory("id"));
        id.setPrefWidth(30);

        TableColumn date = new TableColumn("Ngày");
        date.setCellValueFactory(new PropertyValueFactory("createdDate"));
        date.setPrefWidth(100);

        TableColumn productCode = new TableColumn("Mã đơn hàng");
        productCode.setCellValueFactory(new PropertyValueFactory("orderCode"));
        productCode.setPrefWidth(200);

        TableColumn customer = new TableColumn("Khách hàng");
        customer.setCellValueFactory(new PropertyValueFactory("customer"));
        customer.setPrefWidth(200);

        TableColumn amountTotal = new TableColumn("Tổng tiền");
        amountTotal.setCellValueFactory(new PropertyValueFactory("totalAmount"));
        amountTotal.setPrefWidth(100);

        this.tblOrderList.getColumns().addAll(id, date, productCode, customer, amountTotal);
        this.tblOrderList.setItems(FXCollections.observableArrayList(OrderListService.getOrders(null)));
    }

    public void deleteOrder(ActionEvent Event) throws Exception{

        Order order = tblOrderList.getSelectionModel().getSelectedItem();
        String id = order.getId();

        String idOrder = OrderListService.isExistOrderAndAllow(id);
        if( idOrder.isEmpty() ){
            lblMessage.setText("Đơn hàng không tồn tại.");
            return;
        }

        if( idOrder.equals("NotAllow") ){
            this.lblMessage.setText("Vượt quá 30 phút để xóa đơn hàng");
            return;
        }

        String result = OrderListService.deleteOrder(id);

        this.tblOrderList.getItems().clear();
        this.tblOrderList.setItems(FXCollections.observableArrayList(OrderListService.getOrders(null)));
        lblMessage.setText(result);
    }

    public void viewDetail(ActionEvent Event) throws Exception{

        Order order = tblOrderList.getSelectionModel().getSelectedItem();
        String id = order.getId();

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("FXMLOrderDetail.fxml"));
        
        Scene scene = new Scene(fxmlLoader.load(), 768, 541);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Chi tiết đơn hàng");
        
        FXMLOrderDetailController detailController = fxmlLoader.getController();
        detailController.setData(id, order.getOrderCode() , order.getTotalAmount());
        stage.show();
    }

    public void SalesOrderButton(ActionEvent event) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("FXMLSalesOrder.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 674, 541);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Trang bán hàng");
        stage.show();
    }
    
    public void SearchHandleBtn(ActionEvent event)throws Exception{
        LocalDate fromDate = this.dbFromDate.getValue();
        LocalDate toDate   = this.dbToDate.getValue();
        String orderCode = this.txtOrderCode.getText();
        String customer = this.cbCustomerUser.getValue();
        
        SearchBean searchBean = new SearchBean();
        if(fromDate != null){
            String fromDateS = fromDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            searchBean.setFromDate(fromDateS);
        }
        
        if(toDate != null){
            String toDateS = toDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            searchBean.setToDate(toDateS);
        }
        
        if(orderCode != null && !orderCode.isEmpty()){
            searchBean.setOrderCode(orderCode);
        }
        
        if(customer != null && !customer.isEmpty()){
            String customerId = customer.split(",")[0];
            searchBean.setCustomerId(customerId);
        }
        
        this.tblOrderList.getItems().clear();
        this.tblOrderList.setItems(FXCollections.observableArrayList(OrderListService.getOrders(searchBean)));
    }
    
    public void ResetSearchBtn(ActionEvent event)throws Exception{
        this.txtOrderCode.setText(null);
        this.cbCustomerUser.setValue(null);
        this.dbFromDate.setValue(null);
        this.dbToDate.setValue(null);
        
         this.tblOrderList.getItems().clear();
        this.tblOrderList.setItems(FXCollections.observableArrayList(OrderListService.getOrders(null)));
    }
}
