/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.Products;

import com.mycompany.pojo.Product;
import com.mycompany.services.ProductsService;
import static com.mycompany.services.ProductsService.isExistProductId;
import com.mycompany.services.ResgisterService;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author QUOC ANH
 */
public class ProductsTest {
    
    private static ProductsService s;
    private static Product p;
    
     @Test
    public void testExistProductIdSuccess() throws Exception 
    {
        s= new ProductsService();
        p=new Product();
        String ProductCode="Ma6";
        //p.setProductCode(ProductCode);
        boolean a=s.isExistProductId(ProductCode);
         Assertions.assertTrue(a);
        
    }
    
     @Test
    public void testExistProductIdFail() throws Exception 
    {
        s= new ProductsService();
        p=new Product();
        String ProductCode="Ma1000";
        //p.setProductCode(ProductCode);
        boolean a=s.isExistProductId(ProductCode);
         Assertions.assertFalse(a);
        
    }
    
     @Test
    public void testProductIdisNull() throws Exception ,SQLException
    {
        s= new ProductsService();
        p= new Product();
        String productCode="";
        String productName="QA";
        p.setProductName(productName);
        p.setProductCode(productCode);
        String a= s.validationRequest( p);
        Assertions.assertEquals(a,"Vui lòng nhập Mã sản phẩm");
        
    }
    
    
    
    
    
     
     @Test
    public void testProductNameisNull() throws Exception 
    {
        s= new ProductsService();
        p= new Product();
        String productName="";
        p.setProductName(productName);
       String a= s.validationRequest( p);
         Assertions.assertEquals(a,"Vui lòng nhập Tên sản phẩm");
        
    }
    
      @Test
    public void testPriceProductSuccess() throws Exception 
    {
        s= new ProductsService();
        double GiaNhap=5000;
         double GiaBan=6000;
       String a= s.CheckDonGia(GiaNhap, GiaBan);
         Assertions.assertEquals(a,"");
        
    }
    
      @Test
    public void testPriceProductFail() throws Exception 
    {
        s= new ProductsService();
        double GiaNhap=5000;
         double GiaBan=4000;
       String a= s.CheckDonGia(GiaNhap, GiaBan);
         Assertions.assertEquals(a,"Vui lòng nhập đơn giá bán lớn hơn đơn giá nhập");
        
    }
    
    @Test
    public void testDeleteProductSuccess() throws Exception 
    {
        s= new ProductsService();
         String ProductCode="Ma6";
       String a= s.deleteProduct(ProductCode);
         Assertions.assertEquals(a,"Đã xóa thành công Mã sản phẩm: "+ ProductCode);
        
    }
    @Test
    public void testGetProductIdByInvlid()
    {
        s=new ProductsService();
        
        try {
            p=s.getProductCode("Ma4555");
            Assertions.assertNull(p);
        } catch (SQLException ex) {
            Logger.getLogger(ProductsTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
     @Test
    public void testGetProductIdByValid()
    {
        s=new ProductsService();
        
        try {
            p=s.getProductCode("Ma4");
            Assertions.assertNotNull(p.getProductCode());
        } catch (SQLException ex) {
            Logger.getLogger(ProductsTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
     @Test
    public void testUpdateProductSuccess() throws Exception 
    {
        s= new ProductsService();
        p= new Product();
        p.setProductName("Sản phẩm 44");
        p.setPurchasePrice(Double.parseDouble("44555"));
        p.setPrice(Double.parseDouble("1234556677"));
        p.setProductCode("Ma4");
 
         
       String a= s.updateProduct(p);
         Assertions.assertEquals(a,"Đã cập nhật thành công Mã sản phẩm: "+ p.getProductCode());
        
    }
    
     @Test
    public void testpurchasePriceInvalid() throws Exception ,SQLException
    {
        s= new ProductsService();
        p= new Product();
        String productCode="adsad";
        String productName="QA";
        String PurchasePrice="";
        p.setProductName(productName);
        p.setProductCode(productCode);
        String a= s.validationRequest( p);
        Assertions.assertEquals(a,"Định dạng giá nhập không hợp lệ. Vui lòng nhập Giá nhập.");
        
    }
}
