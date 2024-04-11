package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Customer implements Parcelable {

    // This class represents the Customer, their balance, their stocks, and their watchlist

    String username;
    String password;
    Cash customerCash;
    List<Stock> stocksInWallet;
    List<Stock> stocksInWatchlist;

    public Customer(){
        username = "";
        password = "";
        customerCash = new Cash();
        stocksInWallet = new ArrayList<>();
        stocksInWatchlist = new ArrayList<>();
    }

    protected Customer(Parcel in) {

        username = in.readString();
        customerCash = new Cash();
        customerCash.balance = in.readDouble();
        customerCash.currency = in.readString();
        int sizeStocksInWallet = in.readInt();
        for (int i = 0; i<sizeStocksInWallet; i++) {
            stocksInWallet.add(in.readParcelable(Stock.class.getClassLoader()));
        }
        int sizeStocksInWatchlist = in.readInt();
        for (int i = 0; i<sizeStocksInWatchlist; i++) {
            stocksInWatchlist.add(in.readParcelable(Stock.class.getClassLoader()));
        }

        if(stocksInWallet==null){
            stocksInWallet = new ArrayList<>();
        }
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeDouble(customerCash.balance);
        dest.writeString(customerCash.currency);
        dest.writeInt(stocksInWallet.size());
        for (Stock s : stocksInWallet) {
            dest.writeParcelable(s, 0);
        }
        dest.writeInt(stocksInWatchlist.size());
        for (Stock s : stocksInWatchlist) {
            dest.writeParcelable(s, 0);
        }
    }
}
