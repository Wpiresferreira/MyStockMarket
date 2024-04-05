package com.example.myapplication;

public class Cash {

    // This class represents the Cash
    // For default we are using USD Currency, but in the future we can implement more currencies.
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
