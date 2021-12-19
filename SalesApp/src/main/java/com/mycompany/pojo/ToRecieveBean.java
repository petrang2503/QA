/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pojo;

/**
 *
 * @author QuocAnh
 */
public class ToRecieveBean{

    /**
     * @return the id
     */
    public String getId(){
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id){
        this.id = id;
    }

    /**
     * @return the expiredDate
     */
    public long getExpiredDate(){
        return expiredDate;
    }

    /**
     * @param expiredDate the expiredDate to set
     */
    public void setExpiredDate(long expiredDate){
        this.expiredDate = expiredDate;
    }

    /**
     * @return the createdDate
     */
    public String getCreatedDate(){
        return createdDate;
    }

    /**
     * @param createdDate the createdDate to set
     */
    public void setCreatedDate(String createdDate){
        this.createdDate = createdDate;
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
     * @return the numberOfRecieve
     */
    public String getNumberOfRecieve(){
        return numberOfRecieve;
    }

    /**
     * @param numberOfRecieve the numberOfRecieve to set
     */
    public void setNumberOfRecieve(String numberOfRecieve){
        this.numberOfRecieve = numberOfRecieve;
    }

    private String productId;
    private String productName;
    private String numberOfRecieve;
    private String createdDate;
    private String id;
    private long expiredDate;

}
