package com.example.myapplication;

public class Stock {
    String currency;
    String description;
    String symbol;
    int balance;

    public Stock(){

    }

    public Stock(String newSymbol, int newBalance){
        symbol = newSymbol;
        balance = newBalance;

    }

}
