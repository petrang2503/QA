/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.company.Customer;



import com.mycompany.pojo.Customer;
import com.mycompany.services.CustomerService;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author QUOC ANH
 */
public class CustomerTest {
    
     private static CustomerService s;
    private static Customer p;
    
     @Test
    public void testExistCustomerIdSuccess() throws Exception 
    {
        s= new CustomerService();
        p=new Customer();
        String CustomerCode="KH04";
        
        boolean a=s.isExistCustomerId(CustomerCode);
         Assertions.assertTrue(a);
        
    }
    
     @Test
    public void testExistCustomerIdFail() throws Exception 
    {
        s= new CustomerService();
        p=new Customer();
        String CustomerCode="KH5000";
        
        boolean a=s.isExistCustomerId(CustomerCode);
         Assertions.assertFalse(a);
        
    }
    
     @Test
    public void testCustomerIdisNull() throws Exception ,SQLException
    {
        s= new CustomerService();
        p=new Customer();
        String customerCode="";
        String customerName="QA";
        p.setCustomerCode(customerCode);
        p.setCustomerName(customerName);
        String a= s.validationRequest( p);
        Assertions.assertEquals(a,"Vui lòng nhập Mã khách hàng");
    }
    
      @Test
    public void testCustomerNameisNull() throws Exception ,SQLException
    {
        s= new CustomerService();
        p=new Customer();
        String customerCode="QA";
        String customerName="";
        p.setCustomerCode(customerCode);
        p.setCustomerName(customerName);
        String a= s.validationRequest( p);
        Assertions.assertEquals(a,"Vui lòng nhập Tên khách hàng");
    }
    
      @Test
    public void testPhoneSuccess() throws Exception 
    {
         s= new CustomerService();
        p=new Customer();
        String phone="0123456754";
        String a=s.validPhone(phone);
        Assertions.assertEquals("",a);

        
    }
    
     @Test
    public void testPhoneFail() throws Exception 
    {
         s= new CustomerService();
        p=new Customer();
        String phone="01234232156754";
        String a=s.validPhone(phone);
        Assertions.assertEquals("Số điện thoại không đúng. Vui lòng nhập lại.",a);

        
    }
    
     @Test
    public void testGetCustomerIdByInvlid() throws SQLException
    {
        s=new CustomerService();
            p=s.getCustomerCode("KH04444");
            Assertions.assertNull(p);
        
        
    }
    
    @Test
    public void testGetCustomerIdByValid() throws SQLException
    {
        s=new CustomerService();
         p=new Customer();
     
            p=s.getCustomerCode("KH04");
            Assertions.assertNotNull(p);
       
    }
    
    @Test
    public void testUpdateCustomerSuccess() throws SQLException
    {
         s=new CustomerService();
         p=new Customer();
         
         p.setCustomerCode("KH04");
         p.setCustomerName("Quốc Anh");
         
          String a= s.updateCustomer(p);
         Assertions.assertEquals(a,"Đã cập nhật thành công Mã khách hàng: "+ p.getCustomerCode());
        
    }
}

