/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.pojo;

/**
 *
 * @author QUOC ANH
 */
public class Product {

    private String productCode;
    private String productName;
    private Double purchasePrice;
    private Double price;

    public Product(){}

    /**
    * @return the productCode
    */
    public String getProductCode() {
        return productCode;
    }

    /**
    * @param productCode the productId to set
    */
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    /**
    * @return the productName
    */
    public String getProductName() {
        return productName;
    }

    /**
    * @param productName the productName to set
    */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
    * @return the purchasePrice
    */
    public Double getPurchasePrice() {
        return purchasePrice;
    }

    /**
    * @param purchasePrice the purchasePrice to set
    */
    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    /**
    * @return the price
    */
    public Double getPrice() {
        return price;
    }

    /**
    * @param price the price to set
    */
    public void setPrice(Double price) {
        this.price = price;
    }

    public Product(String productCode, String productName, Double purchasePrice, Double price)
    {
        this.productCode = productCode;
        this.productName = productName;
        this.purchasePrice = purchasePrice;
        this.price = price;
    }
}
