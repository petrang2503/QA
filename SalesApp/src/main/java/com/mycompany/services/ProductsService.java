/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.services;

import com.mycompany.pojo.Product;
import com.mycompany.configs.JdbcUtils;

import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author QUOC ANH
 */
public class ProductsService {
    
    public static String validationRequest(Product product)throws SQLException, Exception{
    
        if(product.getProductName().isEmpty()){
            return "Vui lòng nhập Tên sản phẩm";
        }

        if(product.getProductCode().isEmpty()){
            return "Vui lòng nhập Mã sản phẩm";
        }
        
        if(isExistProductId(product.getProductCode())){
            return "Mã sản phẩm đã tồn tại";
        }

        if(product.getPurchasePrice() == null || product.getPurchasePrice() < 0.0D){
            return "Định dạng giá nhập không hợp lệ. Vui lòng nhập Giá nhập.";
        }

        if(product.getPrice() == null || product.getPrice() < 0.0D){
            return "Định dạng giá bán không hợp lệ. Vui lòng nhập Giá bán.";
        }

        return CheckDonGia(product.getPurchasePrice(), product.getPrice());
    }
    
     public static boolean isExistProductId(String productCode) throws SQLException{
        Connection conn = JdbcUtils.getConn();
        String sql = "SELECT id FROM tblproduct WHERE productCode='"+productCode+"'";
        Statement stament = conn.createStatement();
        ResultSet rs = stament.executeQuery(sql);  

        return rs.next();
    }
     
    public static String CheckDonGia(Double purchasePrice, Double price) throws Exception{
    
        return (price - purchasePrice <= 0) ? "Vui lòng nhập đơn giá bán lớn hơn đơn giá nhập" : "";
    }
      
    public static String insertProduct(Product product) throws SQLException {

        Connection conn = JdbcUtils.getConn();
        String sqlAddProduct = "INSERT INTO tblproduct(productCode, productName, purchasePrice, price) VALUES(?,?,?,?)";
        
        conn.setAutoCommit(false);

        PreparedStatement stm = conn.prepareStatement(sqlAddProduct);
        stm.setString(1, product.getProductCode());
        stm.setString(2, product.getProductName());
        stm.setDouble(3, product.getPurchasePrice());
        stm.setDouble(4, product.getPrice());

        stm.executeUpdate();
        conn.commit();

        return "Sản phẩm đã thêm thành công.";
    }

    public static String deleteProduct(String productCode) throws SQLException {
        String sqlDelete = "DELETE FROM tblproduct WHERE productCode = ?";
        Connection conn = JdbcUtils.getConn();
        conn.setAutoCommit(false);

        PreparedStatement stm = conn.prepareStatement(sqlDelete);
        stm.setString(1, productCode);
        stm.executeUpdate();

        conn.commit();

        return "Đã xóa thành công Mã sản phẩm: "+ productCode;
    }

    public static String updateProduct(Product product) throws SQLException {
        String sqlDelete = "UPDATE tblproduct SET productName = ?, purchasePrice = ?, price = ? WHERE productCode = ?";

        Connection conn = JdbcUtils.getConn();
        conn.setAutoCommit(false);

        PreparedStatement stm = conn.prepareStatement(sqlDelete);
        stm.setString(1, product.getProductName());
        stm.setDouble(2, product.getPurchasePrice());
        stm.setDouble(3, product.getPrice());
        stm.setString(4, product.getProductCode());
        stm.executeUpdate();

        conn.commit();

        return "Đã cập nhật thành công Mã sản phẩm: "+ product.getProductCode();
    }

    public static List<Product> getProducts(String keyword) throws SQLException {
        String sqlGetList = "SELECT * FROM tblproduct";
       
        if(!keyword.isEmpty()){
            sqlGetList += " WHERE productName LIKE ?";
        }

        Connection conn = JdbcUtils.getConn();
        PreparedStatement stm = conn.prepareStatement(sqlGetList);
        if(!keyword.isEmpty()){
            stm.setString(1, String.format("%%%s%%", keyword));
        }
        
        ResultSet rs = stm.executeQuery();
        List<Product> products = new ArrayList<>();
        while(rs.next()){
            Product product = new Product(rs.getString("productCode"), rs.getString("productName"),
                                          rs.getDouble("purchasePrice"), rs.getDouble("price"));
            products.add(product);
        }
        rs.close();

        return products;
    }
    
    public Product getProductCode(String productCode) throws SQLException
    {
            Product p=null;
            String sqlgetProduct = "SELECT * FROM tblproduct WHERE productCode = ?";
            Connection conn = JdbcUtils.getConn();
            conn.setAutoCommit(false);
             PreparedStatement stm = conn.prepareCall(sqlgetProduct);
            stm.setString(1,productCode); 
            
            ResultSet rs=stm.executeQuery();
            while(rs.next())
            {
                p= new Product(rs.getString("productCode"),rs.getString("productName"),rs.getDouble("purchasePrice"),rs.getDouble("price") );
                break;
            }
        return p;

    }
}
