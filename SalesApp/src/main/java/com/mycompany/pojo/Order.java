/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pojo;

/**
 *
 * @author Quoc Anh
 */
public class Order{

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
     * @return the orderCode
     */
    public String getOrderCode(){
        return orderCode;
    }

    /**
     * @param orderCode the orderCode to set
     */
    public void setOrderCode(String orderCode){
        this.orderCode = orderCode;
    }

    /**
     * @return the customerUserId
     */
    public String getCustomerUserId(){
        return customerUserId;
    }

    /**
     * @param customerUserId the customerUserId to set
     */
    public void setCustomerUserId(String customerUserId){
        this.customerUserId = customerUserId;
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
     * @return the totalAmount
     */
    public Double getTotalAmount(){
        return totalAmount;
    }

    /**
     * @param totalAmount the totalAmount to set
     */
    public void setTotalAmount(Double totalAmount){
        this.totalAmount = totalAmount;
    }

    /**
     * @return the expiredDate
     */
    public Long getExpiredDate(){
        return expiredDate;
    }

    /**
     * @param expiredDate the expiredDate to set
     */
    public void setExpiredDate(Long expiredDate){
        this.expiredDate = expiredDate;
    }

    private String id;
    private String orderCode;
    private String customerUserId;
    private String createdDate;
    private Double totalAmount;
    private Long expiredDate;
}
