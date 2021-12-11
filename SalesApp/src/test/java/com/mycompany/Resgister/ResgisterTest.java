/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.Resgister;

import com.mycompany.services.ResgisterService;
import java.sql.SQLException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;



/**
 *
 * @author QUOC ANH
 */
public class ResgisterTest {
    private static ResgisterService s;
    
     @Test
    public void testExistUsername() throws Exception 
    {
        s=new ResgisterService();
         String userName="quocanh95";
        String a=s.validUsername(userName);
        Assertions.assertEquals(a,"Tên đăng nhập đã tồn tại");
    }
    
      @Test
    public void testUserNameEmty() throws Exception 
    {
        s=new ResgisterService();
         String userName="";
        String a=s.validUsername(userName);
        Assertions.assertEquals(a,"Vui lòng nhập Tên đăng nhập");
    }
    
      @Test
    public void testvalidPhoneFail() throws Exception 
    {
        s=new ResgisterService();
         String number="012321123432212";
        String a=s.validPhone(number);
        Assertions.assertEquals(a,"Số điện thoại không đúng. Vui lòng nhập lại.");
    }
    
       @Test
    public void testvalidPhoneSuccess() throws Exception 
    {
        s=new ResgisterService();
         String number="0123432212";
        String a=s.validPhone(number);
        Assertions.assertEquals(a,"");
    }

    
   
    
}
