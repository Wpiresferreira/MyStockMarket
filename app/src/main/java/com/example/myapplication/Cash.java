package com.example.myapplication;

public class Cash {
    double balance;
    String currency;
    public Cash(){
        balance = 0;
        currency = "USD";
    }

    public Cash(double cash){
        balance = cash;
        currency = "USD";
    }

    public Cash(double cash, String currency){
        balance = cash;
        this.currency = currency;
    }
}
