package com.example.myapplication;


public class StockQuote extends Stock{

    String name;
    String imageURL;
    double value;
    double currentPrice;
    double change;
    double percentChange;
    double highPriceOfTheDay;
    double lowPriceOfTheDay;
    double openPriceOfTheDay;
    double previousClosePrice;

    public StockQuote(String newSymbol, int newBalance) {
        super(newSymbol, newBalance);
        this.name = "";
        this.imageURL = "";
        this.value = 0;
        this.currentPrice = 0;
        this.change = 0;
        this.percentChange = 0;
        this.highPriceOfTheDay = 0;
        this.lowPriceOfTheDay = 0;
        this.openPriceOfTheDay = 0;
        this.previousClosePrice = 0;
    }


    public StockQuote() {
    }

}
