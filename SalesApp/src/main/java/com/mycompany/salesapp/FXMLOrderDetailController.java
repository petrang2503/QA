/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.salesapp;

import com.mycompany.pojo.OrderDetails;
import com.mycompany.services.OrderDetailService;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author QUOC ANH
 */
public class FXMLOrderDetailController implements Initializable{

    /**
     * Initializes the controller class.
     */
    @FXML
    private TableView<OrderDetails> tblOrderDetail;

    @FXML
    private Label lblAmountTotal;

    @FXML
    private Label lblId;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        //Load Orders       
    }
    private static final DecimalFormat formatPrice = new DecimalFormat("###,##0");

    void setData(String id, String code, Double amountTotal){
        lblId.setText(id + " - " + code);
        lblAmountTotal.setText(formatPrice.format(amountTotal));
        
         try{
            this.viewDetail();
        }catch(SQLException ex){
            System.err.println(ex.getMessage());
        }
    }

    private void viewDetail() throws SQLException{
        TableColumn productName = new TableColumn("Tên sản phẩm");
        productName.setCellValueFactory(new PropertyValueFactory("productName"));
        productName.setPrefWidth(200);

        TableColumn price = new TableColumn("Đơn giá");
        price.setCellValueFactory(new PropertyValueFactory("price"));
        price.setPrefWidth(100);

        TableColumn quantily = new TableColumn("Số lượng");
        quantily.setCellValueFactory(new PropertyValueFactory("quantily"));
        quantily.setPrefWidth(200);

        TableColumn total = new TableColumn("Thành tiền");
        total.setCellValueFactory(new PropertyValueFactory("amountTotal"));
        total.setPrefWidth(200);

        String id = lblId.getText().split(" - ")[ 0 ];
        this.tblOrderDetail.getColumns().addAll(productName, price, quantily, total);
        this.tblOrderDetail.setItems(FXCollections.observableArrayList(OrderDetailService.getOrderDetails(id)));
    }
}
