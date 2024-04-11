package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Stock implements Parcelable {

    String symbol;
    int balance;
    String name;
    String imageURL;
    double currentPrice;
    double change;
    double percentChange;
    double highPriceOfTheDay;
    double lowPriceOfTheDay;
    double openPriceOfTheDay;
    double previousClosePrice;

    public Stock(){

    }

    public Stock(String newSymbol, int newBalance){
        symbol = newSymbol;
        balance = newBalance;
        this.name = "";
        this.imageURL = "";
        this.currentPrice = 0;
        this.change = 0;
        this.percentChange = 0;
        this.highPriceOfTheDay = 0;
        this.lowPriceOfTheDay = 0;
        this.openPriceOfTheDay = 0;
        this.previousClosePrice = 0;

    }

    protected Stock(Parcel in) {
        symbol = in.readString();
        balance = in.readInt();
    }

    public static final Creator<Stock> CREATOR = new Creator<Stock>() {
        @Override
        public Stock createFromParcel(Parcel in) {
            return new Stock(in);
        }

        @Override
        public Stock[] newArray(int size) {
            return new Stock[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(symbol);
        dest.writeInt(balance);
    }
}
