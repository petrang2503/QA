/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.services;

import com.mycompany.configs.JdbcUtils;
import com.mycompany.pojo.Customer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author QUOC ANH
 */
public class CustomerService {
      
    public static String validationRequest(Customer customer)throws SQLException, Exception{
    
        if(customer.getCustomerName().isEmpty()){
            return "Vui lòng nhập Tên khách hàng";
        }

        if(customer.getCustomerCode().isEmpty()){
            return "Vui lòng nhập Mã khách hàng";
        }
        
        if(isExistCustomerId(customer.getCustomerCode())){
            return "Mã khách hàng đã tồn tại";
        }

   

        return "";
    }
    
        public static boolean isExistCustomerId(String customerCode) throws SQLException
        {
            Connection conn = JdbcUtils.getConn();
            String sql = "SELECT id FROM tblcustomer WHERE customerCode='"+customerCode+"'";
            Statement stament = conn.createStatement();
            ResultSet rs = stament.executeQuery(sql);  

            return rs.next();
        }
        
        public static String validPhone(String phone) throws Exception{
    
        if(!phone.isEmpty()){
           
           Pattern pattern = Pattern.compile("[0-9]{10}");
           Matcher matcher = pattern.matcher(phone);
           if(!matcher.matches()){
            return "Số điện thoại không đúng. Vui lòng nhập lại.";
           }        
        }

        return "";
    }

        
        public static String insertCustomer(Customer customer) throws SQLException {

        Connection conn = JdbcUtils.getConn();
        String sqlAddProduct = "INSERT INTO tblcustomer(customerCode, customerName, customerAddress, customerPhone) VALUES(?,?,?,?)";
        
        conn.setAutoCommit(false);

        PreparedStatement stm = conn.prepareStatement(sqlAddProduct);
        stm.setString(1, customer.getCustomerCode());
        stm.setString(2, customer.getCustomerName());
        stm.setString(3, customer.getCustomerAddress());
        stm.setString(4, customer.getCustomerPhone());

        stm.executeUpdate();
        conn.commit();

        return "Khách hàng đã thêm thành công.";
    }
        
        public static String deleteProduct(String customerCode) throws SQLException {
        String sqlDelete = "DELETE FROM tblcustomer WHERE customerCode = ?";
        Connection conn = JdbcUtils.getConn();
        conn.setAutoCommit(false);

        PreparedStatement stm = conn.prepareStatement(sqlDelete);
        stm.setString(1, customerCode);
        stm.executeUpdate();

        conn.commit();

        return "Đã xóa thành công Mã khách hàng: "+ customerCode;
    }
        
        public static String updateCustomer(Customer customer) throws SQLException 
        {
            String sqlDelete = "UPDATE tblcustomer SET customerName = ?, customerAddress = ?, customerPhone = ? WHERE customerCode = ?";

            Connection conn = JdbcUtils.getConn();
            conn.setAutoCommit(false);

            PreparedStatement stm = conn.prepareStatement(sqlDelete);
            stm.setString(1, customer.getCustomerName());
            stm.setString(2, customer.getCustomerAddress());
            stm.setString(3, customer.getCustomerPhone());
            stm.setString(4, customer.getCustomerCode());
            stm.executeUpdate();

            conn.commit();

            return "Đã cập nhật thành công Mã khách hàng: "+ customer.getCustomerCode();
    }
        
         public static List<Customer> getCustomers(String keyword) throws SQLException {
        String sqlGetList = "SELECT * FROM tblcustomer";
       
        if(!keyword.isEmpty()){
            sqlGetList += " WHERE customerName LIKE ?";
        }

        Connection conn = JdbcUtils.getConn();
        PreparedStatement stm = conn.prepareStatement(sqlGetList);
        if(!keyword.isEmpty()){
            stm.setString(1, String.format("%%%s%%", keyword));
        }
        
        ResultSet rs = stm.executeQuery();
        List<Customer> customers = new ArrayList<>();
        while(rs.next()){
            Customer customer = new Customer(rs.getString("customerCode"), rs.getString("customerName"),
                                          rs.getString("customerAddress"), rs.getString("customerPhone"));
            customers.add(customer);
        }
        rs.close();

        return customers;
    }
         
         public Customer getCustomerCode(String customerCode) throws SQLException
    {
            Customer p=null;
            String sqlgetProduct = "SELECT * FROM tblcustomer WHERE customerCode = ?";
            Connection conn = JdbcUtils.getConn();
            conn.setAutoCommit(false);
             PreparedStatement stm = conn.prepareCall(sqlgetProduct);
            stm.setString(1,customerCode); 
            
            ResultSet rs=stm.executeQuery();
            while(rs.next())
            {
                p= new Customer(rs.getString("customerCode"),rs.getString("customerName"),rs.getString("customerAddress"),rs.getString("customerPhone") );
                break;
            }
        return p;

    }
}
