package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Stock implements Parcelable {

    String symbol;
    int balance;

    public Stock(){

    }

    public Stock(String newSymbol, int newBalance){
        symbol = newSymbol;
        balance = newBalance;

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
