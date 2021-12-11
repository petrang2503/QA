/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.services;

import com.mycompany.pojo.User;
import com.mycompany.configs.JdbcUtils;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 *
 * @author QUOC ANH
 */
public class ResgisterService {
    
    public String validPasword(String password) throws Exception
    {
        if(password.isEmpty()) {

            return "Vui lòng nhập Mật khẩu";
        }
        
        Pattern pattern = Pattern.compile("(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$");
        Matcher matcher = pattern.matcher(password);
        if(!matcher.matches()){
            return "Mật khẩu của bạn không đủ điều kiện. Vui lòng nhập lại";
        }

        return "";
    }

    public String validUsername(String username) throws Exception{
    
        if(username.isEmpty()){
            return "Vui lòng nhập Tên đăng nhập";
        }

        if(isExistUsername(username)){
            return "Tên đăng nhập đã tồn tại";
        }

        return "";
    }

    private boolean isExistUsername(String username) throws SQLException{
        Connection conn = JdbcUtils.getConn();
        String sql = "SELECT id FROM tblUser WHERE username='"+username+"'";
        Statement stament = conn.createStatement();
        ResultSet rs = stament.executeQuery(sql);  

        return rs.next();
    }

    public String validPhone(String phone) throws Exception{
    
        if(!phone.isEmpty()){
           
           Pattern pattern = Pattern.compile("[0-9]{10}");
           Matcher matcher = pattern.matcher(phone);
           if(!matcher.matches()){
            return "Số điện thoại không đúng. Vui lòng nhập lại.";
           }        
        }

        return "";
    }

    public void insertUser(User user) throws SQLException {

        Connection conn = JdbcUtils.getConn();
        String sqlAddUser = "INSERT INTO tblUser(username, password, phone) VALUES(?,?,?)";
        
        conn.setAutoCommit(false);

        PreparedStatement stm = conn.prepareStatement(sqlAddUser);
        stm.setString(1, user.getUsername());
        stm.setString(2, user.getPassword());
        stm.setString(3, user.getPhone());

        stm.executeUpdate();

        conn.commit();
    }
    
}
