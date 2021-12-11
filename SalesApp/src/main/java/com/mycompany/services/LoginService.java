/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.services;

import com.dht.configs.Utils;
import com.mycompany.configs.JdbcUtils;
import com.mycompany.pojo.User;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.control.Alert;

/**
 *
 * @author QUOC ANH
 */
public class LoginService {
    public String checkLogin(User user) throws Exception
    {
        if(user.getUsername().isEmpty() || user.getPassword().isEmpty() )
        {
            
             return "Vui lòng nhập đầy đủ thông tin đăng nhập.!";
        }

        if(!isUserExist(user)){
            return "Tên đăng nhập hoặc mật khẩu không đúng.!";
        }
             
        return "";
    }

    private boolean isUserExist(User user)throws SQLException{
   
        Connection conn=JdbcUtils.getConn();

        String sql="SELECT id FROM tbluser "
                + "WHERE username='"+user.getUsername()+"' AND password='"+user.getPassword()+"' ";

        Statement stament= conn.createStatement();
        ResultSet rs= stament.executeQuery(sql);

        return rs.next();
    }
}
