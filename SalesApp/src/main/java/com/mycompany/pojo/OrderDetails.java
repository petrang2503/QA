/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pojo;

/**
 *
 * @author Trang
 */
public class OrderDetails{

    /**
     * @return the orderId
     */
    public String getOrderId(){
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(String orderId){
        this.orderId = orderId;
    }

    /**
     * @return the productId
     */
    public String getProductId(){
        return productId;
    }

    /**
     * @param productId the productId to set
     */
    public void setProductId(String productId){
        this.productId = productId;
    }

    /**
     * @return the productCode
     */
    public String getProductCode(){
        return productCode;
    }

    /**
     * @param productCode the productCode to set
     */
    public void setProductCode(String productCode){
        this.productCode = productCode;
    }

    /**
     * @return the productName
     */
    public String getProductName(){
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName){
        this.productName = productName;
    }

    /**
     * @return the price
     */
    public String getPrice(){
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(String price){
        this.price = price;
    }

    /**
     * @return the quantily
     */
    public String getQuantily(){
        return quantily;
    }

    /**
     * @param quantily the quantily to set
     */
    public void setQuantily(String quantily){
        this.quantily = quantily;
    }

    /**
     * @return the amountTotal
     */
    public String getAmountTotal(){
        return amountTotal;
    }

    /**
     * @param amountTotal the amountTotal to set
     */
    public void setAmountTotal(String amountTotal){
        this.amountTotal = amountTotal;
    }
   
    private String orderId;
    private String productId;
    private String productCode;
    private String productName;
    private String price;
    private String quantily;
    private String amountTotal;
}
