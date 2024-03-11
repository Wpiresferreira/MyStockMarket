package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class Customer implements Parcelable {


    String name;
    String username;
    String password;
    Cash customerCash;
    List<Stock> stocksInWallet;
    List<Stock> stocksInWatchlist;

    public Customer(){}

    public Customer(String name, String username, String password, Cash customerCash, List<Stock> stocksInWallet, List<Stock> stocksInWatchlist) {
        this.name = name;
        this.password = password;
        this.username = username;
        this.customerCash = customerCash;
        this.stocksInWallet = stocksInWallet;
        this.stocksInWatchlist = stocksInWatchlist;
    }


    protected Customer(Parcel in) {
        name = in.readString();
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
        dest.writeString(name);
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
