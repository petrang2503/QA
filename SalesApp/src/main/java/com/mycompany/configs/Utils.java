/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dht.configs;

import javafx.scene.control.Alert;

/**
 *
 * @author duonghuuthanh
 */
public class Utils {
    public static Alert getBox(String content, Alert.AlertType type) {
        Alert a = new Alert(type);
        a.setContentText(content);
        
        return a;
    }
}
