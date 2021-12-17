/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.services;

import com.mycompany.configs.JdbcUtils;
import com.mycompany.pojo.OrderDetails;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Quoc Anh
 */
public class OrderDetailService{

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
