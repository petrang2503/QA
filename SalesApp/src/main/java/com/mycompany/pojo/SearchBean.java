/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pojo;

/**
 *
 * @author Quoc Anh
 */
public class SearchBean{

    /**
     * @return the fromDate
     */
    public String getFromDate(){
        return fromDate;
    }

    /**
     * @param fromDate the fromDate to set
     */
    public void setFromDate(String fromDate){
        this.fromDate = fromDate;
    }

    /**
     * @return the toDate
     */
    public String getToDate(){
        return toDate;
    }

    /**
     * @param toDate the toDate to set
     */
    public void setToDate(String toDate){
        this.toDate = toDate;
    }

    /**
     * @return the customerId
     */
    public String getCustomerId(){
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(String customerId){
        this.customerId = customerId;
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
    private String fromDate;
    private String toDate;
    private String customerId;
    private String orderCode;
}
