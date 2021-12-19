/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.services;

import com.mycompany.pojo.ToRecieveBean;
import com.mycompany.pojo.Product;
import com.mycompany.configs.JdbcUtils;

import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author QUOC ANH
 */
public class ToRecieverService {
    
     public static Product isExistProductId(String id) throws SQLException{
        Connection conn = JdbcUtils.getConn();
        String sql = "SELECT id, productName, purchasePrice, numberOfWarehouses FROM tblproduct WHERE id='"+id+"'";
        Statement stament = conn.createStatement();
        Product pro = null;
        
         try (ResultSet rs = stament.executeQuery(sql)) {
             pro = new Product();
             while(rs.next()){
                 pro.setProductName(rs.getNString("productName"));
                 pro.setPurchasePrice(rs.getDouble("purchasePrice"));
                 pro.setNumberToRecieve(rs.getInt("numberOfWarehouses"));
             }}
        
        return pro;
    }
     
    public static String insertRecieveProduct(String productId, long numberOfRecieve) throws SQLException {
        System.out.println("vao insertRecieveProduct");
        Product pro = isExistProductId(productId);
        if(pro == null){
            return "Sản phẩm không tồn tại";
        }
        
        String createdDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Long currently = System.currentTimeMillis() + 1800000;
                
        Connection conn = JdbcUtils.getConn();
        conn.setAutoCommit(false);
        
        String sql = "INSERT INTO tbltorecieve(productId, productName, numberOfRecieve, createdDate, expiredDate) VALUES(?,?,?,?,?)";
       
        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setString(1, productId);
        stm.setString(2, pro.getProductName());
        stm.setLong(3, numberOfRecieve);
        stm.setString(4, createdDate);
        stm.setLong(5, currently);

        stm.executeUpdate();
                
       String error = updateQuantilyProduct(conn, productId, "insert");
        if(!error.isEmpty()){
            conn.rollback();
            conn.setAutoCommit(true);
            return error;
        }
        conn.commit();
        conn.setAutoCommit(true);
        conn.close();
        
        return "Đã thêm thành công.";
    }
    
    public static Long getNumberOfRecieve(Connection conn, String productId) throws SQLException{
        String sqlAddProduct = "SELECT SUM(numberOfRecieve) AS totalNumber FROM tbltorecieve WHERE productId = '"+productId+"'";      
        Statement stament = conn.createStatement();
        long totalNumber = 0l;
        
        try (ResultSet rs = stament.executeQuery(sqlAddProduct)) {
             while(rs.next()){
                 totalNumber = rs.getLong("totalNumber");
             }}
        
        return totalNumber;
    }

    public static List<ToRecieveBean> getProducts(String productId) throws SQLException {
        String sqlGetList = "SELECT * FROM tbltorecieve WHERE productId = ?";
       
        Connection conn = JdbcUtils.getConn();
        PreparedStatement stm = conn.prepareStatement(sqlGetList);
        stm.setString(1, productId);
        
        List<ToRecieveBean> products = new ArrayList<>();
         try (ResultSet rs = stm.executeQuery()) { 
             while(rs.next()){
                 ToRecieveBean product = new ToRecieveBean();
                 product.setId(rs.getString("id"));
                 product.setProductId(rs.getString("productId"));
                 product.setProductName(rs.getString("productName"));
                 product.setNumberOfRecieve(String.valueOf(rs.getLong("numberOfRecieve")));
                 product.setCreatedDate(rs.getString("createdDate"));
                 products.add(product);
             }}

        return products;
    }
    
    public static String delete(String id, String productId) throws SQLException{
        
        String sql = "DELETE FROM tbltorecieve WHERE id = ?";
        Connection conn = JdbcUtils.getConn();
        conn.setAutoCommit(false);
        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setString(1, id);
        stm.executeUpdate();

        updateQuantilyProduct(conn, productId, "delete");
        
        conn.commit();

        return "Đã xóa thành công";
        
    }
    
    public static String updateQuantilyProduct(Connection conn, String productId, String action) throws SQLException{
    
        long numberTotal = getNumberOfRecieve(conn, productId);
        
        if(action.equals("insert") && numberTotal > 10){
            
            return "Tồn kho lớn hơn 10. Bạn không được nhập thêm.";
        }
        
        String sql = "UPDATE tblProduct set numberOfWarehouses = ? WHERE id = ?";
        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setInt(1, (int)numberTotal);
        stm.setString(2, productId);
        stm.executeUpdate();
        
        return "";
    }
    
    private static ToRecieveBean getToRecieverInfo(String id)throws SQLException{
         Connection conn = JdbcUtils.getConn();
        String sql = "SELECT expiredDate, numberOfRecieve FROM tbltorecieve WHERE id='"+id+"'";
        Statement stament = conn.createStatement();
        ToRecieveBean pro = null;
        
         try (ResultSet rs = stament.executeQuery(sql)) {
             pro = new ToRecieveBean();
             while(rs.next()){
                 pro.setExpiredDate(rs.getLong("expiredDate"));                 
                 pro.setNumberOfRecieve(rs.getString("numberOfRecieve"));
             }}
        
        return pro;
    }
    
    public static String update(String id, String productId, long numberOfRecieve) throws SQLException{
        
        ToRecieveBean pro = getToRecieverInfo(id);
        if(pro == null){
            return "Dữ liệu không tồn tại.";
        }
        
        Long currently = System.currentTimeMillis();
        if( pro.getExpiredDate() != 0l  && pro.getExpiredDate() <= currently ){
            return "Vượt quá 30 phút để xóa đơn hàng";
        }
                        
        Connection conn = JdbcUtils.getConn();
        conn.setAutoCommit(false);
        
        String sql = "UPDATE tbltorecieve SET numberOfRecieve = ? WHERE ID = ?";
       
        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setLong(1, numberOfRecieve);
        stm.setString(2, id);
        
        stm.executeUpdate();
        
        updateQuantilyProduct(conn, productId, "update");
        
        conn.commit();
        conn.setAutoCommit(true);
        conn.close();
        
        return "Đã cập nhật thành công.";
        
    }
    
    public static ObservableList<String> getListProductName() throws SQLException{
        ObservableList<String> products = FXCollections.observableArrayList();

        String sqlGetList = "SELECT * FROM tblproduct";

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
}
