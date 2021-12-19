/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.services;

import com.mycompany.configs.JdbcUtils;
import com.mycompany.pojo.Customer;
import com.mycompany.pojo.Order;
import com.mycompany.pojo.OrderDetails;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Trang
 */
public class SalesOrderService{

    public static ObservableList<String> getListCustomerUserName() throws SQLException{
        ObservableList<String> customers = FXCollections.observableArrayList();

        String sqlGetList = "SELECT * FROM tblcustomer";

        Connection conn = JdbcUtils.getConn();
        PreparedStatement stm = conn.prepareStatement(sqlGetList);

        try(  ResultSet rs = stm.executeQuery() ){
            while(rs.next()){
                String value = rs.getString("id") + "," + rs.getString("customerName");
                customers.add(value);
            }
        }

        return customers;
    }

    public static ObservableList<String> getListProductName() throws SQLException{
        ObservableList<String> products = FXCollections.observableArrayList();

        String sqlGetList = "SELECT * FROM tblproduct WHERE numberOfWarehouses >= 0";

        Connection conn = JdbcUtils.getConn();
        PreparedStatement stm = conn.prepareStatement(sqlGetList);

        try(  ResultSet rs = stm.executeQuery() ){
            while(rs.next()){
                String value = rs.getString("id") + "," + rs.getString("productName");
                products.add(value);
            }
        }

        return products;
    }

    public static Double getProductPrice(String id) throws SQLException{

        String sql = "SELECT price FROM tblproduct WHERE id = ?";

        Connection conn = JdbcUtils.getConn();
        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setString(1, id);

        try(  ResultSet rs = stm.executeQuery() ){
            while(rs.next()){
                return rs.getDouble("price");
            }
        }

        return 0.0D;
    }

    public static Customer getCustomerUserInfo(String id) throws SQLException{

        String sql = "SELECT customerAddress, customerPhone FROM tblcustomer WHERE id = ?";

        Connection conn = JdbcUtils.getConn();
        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setString(1, id);

        Customer customer = new Customer();
        try(  ResultSet rs = stm.executeQuery() ){
            while(rs.next()){
                customer.setCustomerAddress(rs.getString("customerAddress"));
                customer.setCustomerPhone(rs.getString("customerPhone"));
            }
        }

        return customer;
    }

    public static String CreateOrder(Order order, List<OrderDetails> details) throws SQLException{
        double totalBill = 0.0d;

        Connection conn = null;
        try{
            conn = JdbcUtils.getConn();

            //Insert Order
            String sql = "INSERT INTO tblorder(orderCode, customerId, createdDate, expiredDate) VALUES(?,?,?,?)";
            conn.setAutoCommit(false);

            PreparedStatement stm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, order.getOrderCode());
            stm.setString(2, order.getCustomerUserId());
            stm.setString(3, order.getCreatedDate());
            stm.setLong(4, order.getExpiredDate());
            stm.executeUpdate();

            //Generate ID
            ResultSet rs = stm.getGeneratedKeys();
            String idValue = "";
            if( rs.next() ){
                idValue = rs.getString(1);
            }

            //Insert Details
            for(OrderDetails detail : details){
                String productId = detail.getProductId();
                Integer quantily = Integer.valueOf(detail.getQuantily());

                String checkInventory = SalesOrderService.CheckInventory(productId, quantily, "xuatkho", conn);
                if( !checkInventory.isEmpty() ){
                    System.out.println(checkInventory);
                    conn.rollback();
                    conn.setAutoCommit(true);
                    return checkInventory;
                }

                sql = "INSERT INTO tblorderDetails(orderId, productId, price, quantily, amountTotal)"
                        + " VALUES(?,?,?,?,?)";

                stm = conn.prepareStatement(sql);
                stm.setString(1, idValue);
                stm.setString(2, productId);
                stm.setDouble(3, Double.valueOf(detail.getPrice().replaceAll(",", "")));
                stm.setInt(4, quantily);

                double total = Double.valueOf(detail.getAmountTotal().replaceAll(",", ""));
                stm.setDouble(5, total);
                stm.executeUpdate();
                totalBill += total;

                //insert xuatkho
                sql = "INSERT INTO tbltorecieve(productId, numberOfRecieve, createdDate, orderId) VALUES(?,?,?,?)";

                stm = conn.prepareStatement(sql);
                stm.setString(1, productId);
                stm.setDouble(2, quantily*(-1));
                stm.setString(3, order.getCreatedDate());
                stm.setString(4, idValue);

                stm.executeUpdate();
            }

            sql = "UPDATE tblorder SET amountTotal = ? WHERE id = ?";

            stm = conn.prepareStatement(sql);
            stm.setDouble(1, totalBill);
            stm.setString(2, idValue);
            stm.executeUpdate();

            conn.commit();
            conn.setAutoCommit(true);
        }catch(SQLException e){

            System.out.println("Data ban đầu.");
            if( conn != null ){
                conn.rollback();
            }
        }finally {
            if( conn != null ) {
                conn.close();
            }
        }

        return "";
    }

    public static String CheckInventory(String productId, int quantily, String action, Connection conn) throws SQLException{
        String sql = "SELECT productName, numberOfWarehouses, quantityRemaining FROM tblproduct WHERE id = ?";

        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setString(1, productId);

        String productName = "";
        int quantityRemaining = 0;
        try(  ResultSet rs = stm.executeQuery() ){
            if( rs.next() ){
                quantityRemaining = rs.getInt("quantityRemaining");
                productName = rs.getString("productName");
            }
        }

        int remaining = 0;
        if( action.equals("xuatkho") ){
            if( quantily > quantityRemaining ){

                return "Sản phẩm " + productName + " đã vượt quá số lượng tồn trong kho. Tồn kho hiện tại: " + quantityRemaining;
            }

            remaining = quantityRemaining - quantily;
        } else if( action.equals("nhaplai") ){
            remaining = quantityRemaining + quantily;
        }

        sql = "UPDATE tblproduct SET quantityRemaining = ? WHERE id = ?";

        stm = conn.prepareStatement(sql);
        stm.setInt(1, remaining);
        stm.setString(2, productId);
        stm.executeUpdate();

        return "";
    }
}
