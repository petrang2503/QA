package com.mycompany.Login;


import com.mycompany.pojo.User;
import com.mycompany.services.LoginService;
import java.sql.SQLException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author QUOC ANH
 */
public class LoginTest {
    
  private static LoginService s;
    User lg;
    
    @Test
    public void testLoginFail() throws SQLException, Exception
    {  
        s=new LoginService();
        lg=new User();
        lg.setUsername("asdsadssad");
        lg.setPassword("Admin1");


         String a=s.checkLogin(lg);


         Assertions.assertEquals(a,"Tên đăng nhập hoặc mật khẩu không đúng.!");
      
    }
    
    
    @Test
    public void testLoginSuccess() throws SQLException, Exception
    {  
        s=new LoginService();
       lg=new User();
        lg.setUsername("quocanh");
        lg.setPassword("Quocanh.95");


         String a=s.checkLogin(lg);


         Assertions.assertEquals("",a);
      
    }
    
    
        @Test
    public void testUserNameorPassWordNull() throws SQLException, Exception
    {  
        s=new LoginService();
       lg=new User();
        lg.setUsername("");
        lg.setPassword("Quocanh.95");


         String a=s.checkLogin(lg);


         Assertions.assertEquals("Vui lòng nhập đầy đủ thông tin đăng nhập.!",a);
      
    }
    
       @Test
    public void testUserNameorPassWordFail() throws SQLException, Exception
    {  
        s=new LoginService();
       lg=new User();
        lg.setUsername("quocanh");
        lg.setPassword("Quocanh.95asdsad");


         String a=s.checkLogin(lg);


         Assertions.assertEquals("Tên đăng nhập hoặc mật khẩu không đúng.!",a);
      
    }
//    
//     @Test
//    public void testExpectedExceptionWithParentType() throws  SQLException{
//        //s=new LoginService();
//                 s=new LoginService();
//        lg=new Login();
//        lg.setUserName("Admin");
//        lg.setPassword("Admin1");
//
//     Exception exception =Assertions.assertThrows(Exception.class, () -> {
//       s.CheckLogin(lg);
//    });
//
//    String actualMessage = exception.getMessage();
//
//    Assertions.assertTrue(actualMessage.contains(actualMessage));
////        
////       Assertions.assertThrows(SQLException.class,()-> s.CheckLogin(lg));
//}
//    
   
}
