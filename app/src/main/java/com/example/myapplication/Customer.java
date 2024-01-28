package com.example.myapplication;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    String name;
    String username;
    String password;

    Cash customerCash;
    List<Stock> customerListStock;
    List<Stock> customerStockView;

    public Customer(String name, String username, String password, Cash customerCash, List<Stock> customerListStock, List<Stock> customerStockView) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.customerCash = customerCash;
        this.customerListStock = customerListStock;
        this.customerStockView = customerStockView;
    }
}
