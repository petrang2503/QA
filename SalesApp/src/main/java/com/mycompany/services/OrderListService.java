/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.services;

import com.mycompany.configs.JdbcUtils;
import com.mycompany.pojo.Order;
import com.mycompany.pojo.OrderDetails;
import com.mycompany.pojo.SearchBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Quoc Anh
 */
public class OrderListService{

    public static String deleteOrder(String id) throws SQLException{
        
        String sqlDelete = "DELETE FROM tblorder WHERE id = ?";
        Connection conn = JdbcUtils.getConn();
        conn.setAutoCommit(false);

        PreparedStatement stm = conn.prepareStatement(sqlDelete);
        stm.setString(1, id);
        stm.executeUpdate();

        conn.commit();
        conn.close();
        
        return "Đã xóa thành công Đơn hàng - ID: "+ id;
    }
    
    public static String isExistOrderAndAllow(String id) throws SQLException{
        Connection conn = JdbcUtils.getConn();
        String sql = "SELECT id, expiredDate FROM tblorder WHERE id = ?";
        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setString(1, id);

        String idData = "";
        long expiredDate = 0l;
        try( ResultSet rs = stm.executeQuery() ){
            if( rs.next() ){
                expiredDate = rs.getLong("expiredDate");
                idData = rs.getString("id");
            }
        }

        long currently = System.currentTimeMillis();
        if( expiredDate != 0l  && expiredDate <= currently ){
            return "NotAllow";
        }

        return idData;
    }
       
    public static List<Order> getOrders(SearchBean searchBean) throws SQLException{
        Connection conn = JdbcUtils.getConn();
        String sqlGetList = "SELECT * FROM tblorder";

        StringBuilder sql = new StringBuilder();

        if(searchBean != null){
            if( !searchBean.getCustomerId().isEmpty() ){
                sql.append("customerId = ?");
            }

            if( !searchBean.getFromDate().isEmpty() ){
                addAndQuery(sql);
                sql.append("createdDate >= ?");
            }

            if( !searchBean.getToDate().isEmpty() ){
                addAndQuery(sql);
                sql.append("createdDate <= ?");
            }

            if( !searchBean.getOrderCode().isEmpty() ){
                addAndQuery(sql);
                sql.append("orderCode LIKE ?");
            }

            if( sql.length() > 0 ){
                sqlGetList += " WHERE " + sql.toString();
            }
        }
        

        int column = 0;
        PreparedStatement stm = conn.prepareStatement(sqlGetList);
        if( searchBean != null ){
            if( !searchBean.getCustomerId().isEmpty() ){
                column++;
                stm.setString(column, searchBean.getCustomerId());
            }

            if( !searchBean.getFromDate().isEmpty() ){
                column++;
                stm.setString(column, searchBean.getFromDate());
            }

            if( !searchBean.getToDate().isEmpty() ){
                column++;
                stm.setString(column, searchBean.getToDate());
            }

            if( !searchBean.getOrderCode().isEmpty() ){
                column++;
                stm.setString(column, String.format("%%%s%%", searchBean.getOrderCode()));
            }
        }
        
        List<Order> orders = new ArrayList<>();;
        try (ResultSet rs = stm.executeQuery()) {
            
            while(rs.next()){
                Order order = new Order();
                order.setCreatedDate(rs.getString("createdDate"));
                
                String customerInfo = getCustomerInfo(rs.getString("customerId"));
                
                order.setCustomer(customerInfo);
                order.setId(rs.getString("id"));
                order.setOrderCode(rs.getString("orderCode"));
                order.setTotalAmount(rs.getDouble("AmountTotal"));
                orders.add(order);
            }
        }

        return orders;
    }

    public static String getCustomerInfo(String customerId) throws SQLException{
        
        String sql = "SELECT * FROM tblcustomer WHERE id = ?";
        
       Connection conn = JdbcUtils.getConn();
       PreparedStatement stm = conn.prepareStatement(sql);
        stm.setString(1, customerId);
        
        try (ResultSet rs = stm.executeQuery()) {
            
            while(rs.next()){
                String code = rs.getString("customerCode");
                String name = rs.getString("customerName");
                
                return code + " - " + name;
            }
        }
        
       return "";
    }
    
 
    public static void addAndQuery(StringBuilder sql){
        if( sql.length() > 0 ){
            sql.append(" AND ");
        }
    }
    private static final DecimalFormat formatPrice = new DecimalFormat("###,##0");
    
    public static List<OrderDetails> getOrderDetails(String id) throws SQLException{
        List<OrderDetails> details = new ArrayList<>();
        
        String sql = "SELECT * FROM tblorderDetails WHERE orderId = ?";
        
        try (Connection conn = JdbcUtils.getConn()) {
            
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, id);
            try (ResultSet rs = stm.executeQuery()) {
                
                while(rs.next()){
                    
                    OrderDetails detail = new OrderDetails();
                    detail.setProductId(rs.getString("productId"));
                    detail.setProductName(getProductName(rs.getString("productId")));
                    detail.setPrice(formatPrice.format(rs.getDouble("price")));
                    detail.setQuantily(rs.getString("quantily"));
                    detail.setAmountTotal(formatPrice.format(rs.getDouble("amountTotal")));
                    
                    details.add(detail);
                }
            }
        }
        
        return details;
    }
    
    private static String getProductName(String id) throws SQLException{
        
        String sql = "SELECT productName FROM tblproduct WHERE id = ?";
        
        String productName = "";
        try (Connection conn = JdbcUtils.getConn()) {
            
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, id);
            
            try (ResultSet rs = stm.executeQuery()) {
                
                while(rs.next()){
                    
                    productName = rs.getNString("productName");
                }
            }
        }
        
        return productName;
    }
}
