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

        try( ResultSet rs = stm.executeQuery() ){
            while(rs.next()){
                String value = rs.getString("id") + "," + rs.getString("customerName");
                customers.add(value);
            }
        }

        return customers;
    }

    public static ObservableList<String> getListProductName() throws SQLException{
        ObservableList<String> products = FXCollections.observableArrayList();

        String sqlGetList = "SELECT * FROM tblproduct";

        Connection conn = JdbcUtils.getConn();
        PreparedStatement stm = conn.prepareStatement(sqlGetList);

        try( ResultSet rs = stm.executeQuery() ){
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

        try( ResultSet rs = stm.executeQuery() ){
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
        try( ResultSet rs = stm.executeQuery() ){
            while(rs.next()){
                customer.setCustomerAddress(rs.getString("customerAddress"));
                customer.setCustomerPhone(rs.getString("customerPhone"));
            }
        }

        return customer;
    }

    public static String CreateOrder(Order order) throws SQLException{
        Connection conn = JdbcUtils.getConn();
        String sql = "INSERT INTO tblorder(orderCode, customerId, createdDate, expiredDate) VALUES(?,?,?,?)";

        conn.setAutoCommit(false);

        PreparedStatement stm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stm.setString(1, order.getOrderCode());
        stm.setString(2, order.getCustomerUserId());
        stm.setString(3, order.getCreatedDate());
        stm.setLong(4, order.getExpiredDate());

        stm.executeUpdate();
        conn.commit();

        ResultSet rs = stm.getGeneratedKeys();

        String idValue = "";
        if( rs.next() ){
            idValue = rs.getString(1);
        }

        return idValue;
    }

    public static Double InsertOrderDetail(OrderDetails detail, String id) throws SQLException{
        Connection conn = JdbcUtils.getConn();

        String sql = "SELECT * FROM tblorderDetails WHERE orderId = ? AND productId = ?";
        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setString(1, id);
        stm.setString(2, detail.getProductId());

        ResultSet rs = stm.executeQuery();
        if( rs.next() ){
            System.out.println("Update tblorderDetails");
            int quantilyBefore = rs.getInt("quantily");
            int quatilyNew = Integer.valueOf(detail.getQuantily());
            double price = rs.getDouble("price");

            int quantilyCurrently = quatilyNew + quantilyBefore;
            double total = price * quantilyCurrently;

            sql = "UPDATE tblorderDetails SET quantily = ?, amountTotal = ? WHERE orderId = ? AND productId = ?";
            conn.setAutoCommit(false);

            stm = conn.prepareStatement(sql);
            stm.setInt(1, quantilyCurrently);
            stm.setDouble(2, total);
            stm.setString(3, id);
            stm.setString(4, detail.getProductId());

            stm.executeUpdate();
            conn.commit();

            conn.close();

            return price * quatilyNew;
        } else {
            sql = "INSERT INTO tblorderDetails(orderId, productId, price, quantily, amountTotal)"
                    + " VALUES(?,?,?,?,?)";

            conn.setAutoCommit(false);

            stm = conn.prepareStatement(sql);
            stm.setString(1, id);
            stm.setString(2, detail.getProductId());
            stm.setDouble(3, Double.valueOf(detail.getPrice().replaceAll(",", "")));
            stm.setInt(4, Integer.valueOf(detail.getQuantily()));

            double total = Double.valueOf(detail.getAmountTotal().replaceAll(",", ""));
            stm.setDouble(5, total);

            stm.executeUpdate();
            conn.commit();

            conn.close();
            return total;
        }
    }

    public static void UpdateTotalBill(double billTotal, String id) throws SQLException{
        Connection conn = JdbcUtils.getConn();
        String sql = "UPDATE tblorder SET amountTotal = ? WHERE id = ?";

        conn.setAutoCommit(false);

        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setDouble(1, billTotal);
        stm.setString(2, id);

        stm.executeUpdate();
        conn.commit();
    }

    public static String isExistOrderAndAllow(String code) throws SQLException{
        Connection conn = JdbcUtils.getConn();
        String sql = "SELECT id, expiredDate FROM tblorder WHERE orderCode = ?";
        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setString(1, code);

        long expiredDate;
        String id = "";
        try( ResultSet rs = stm.executeQuery() ){
            expiredDate = 0l;
            if( rs.next() ){
                expiredDate = rs.getLong("expiredDate");
                id = rs.getString("id");
            }
        }

        long currently = System.currentTimeMillis();
        if( expiredDate != 0l && expiredDate <= currently ){
            return "NotAllow";
        }

        return id;
    }

    public static void deleteOldOrder(String id) throws SQLException{
        try( Connection conn = JdbcUtils.getConn() ){
            String sqlDelete = "DELETE FROM tblorderDetails WHERE orderId = ?";
            conn.setAutoCommit(false);

            PreparedStatement stm = conn.prepareStatement(sqlDelete);
            stm.setString(1, id);
            stm.executeUpdate();

            conn.commit();

            //-----
            sqlDelete = "DELETE FROM tblorder WHERE id = ?";
            conn.setAutoCommit(false);

            stm = conn.prepareStatement(sqlDelete);
            stm.setString(1, id);
            stm.executeUpdate();

            conn.commit();
        }
    }

    public static String CheckInventory(String productId, int quantily, String action) throws SQLException{
        String sql = "SELECT productName, numberOfWarehouses, quantityRemaining FROM tblproduct WHERE id = ?";
        try( Connection conn = JdbcUtils.getConn() ){
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, productId);
            String productName = "";
            int quantityRemaining = 0;
            try( ResultSet rs = stm.executeQuery() ){
                if( rs.next() ){
                    quantityRemaining = rs.getInt("quantityRemaining");
                    productName = rs.getString("productName");
                }
            }
            
            int remaining = 0;
            if(action.equals("xuatkho")){
                if( quantily > quantityRemaining ){
                    conn.close();
                    return "Sản phẩm " + productName + " đã vượt quá số lượng tồn trong kho. Tồn kho hiện tại: " + quantityRemaining;
                }

                remaining = quantityRemaining - quantily;
            } else if(action.equals("nhaplai")){
                remaining = quantityRemaining + quantily;
            }
                        
            sql = "UPDATE tblproduct SET quantityRemaining = ? WHERE id = ?";
            conn.setAutoCommit(false);
            stm = conn.prepareStatement(sql);
            stm.setInt(1, remaining);
            stm.setString(2, productId);
            stm.executeUpdate();
            conn.commit();
            
            conn.close();
        }
        return "";
    }
}
