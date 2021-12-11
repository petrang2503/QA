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
public class User {

    private String password;
    private String username;
    private String phone;

    public User(){}
    
    /**
    * @return the password
    */
    public String getPassword() {
        return password;
    }

    /**
    * @param password the password to set
    */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
    * @return the username
    */
    public String getUsername() {
        return username;
    }

    /**
    * @param username the username to set
    */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
    * @return the phone
    */
    public String getPhone() {
        return phone;
    }

    /**
    * @param phone the phone to set
    */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User(String username, String pw, String phone)
    {
        this.username = username;
        this.password = pw;
        this.phone = phone;
    }

    public User(String username, String pw)
    {
        this.username = username;
        this.password = pw;
    }
}
