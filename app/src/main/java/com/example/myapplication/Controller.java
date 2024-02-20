package com.example.myapplication;

import android.content.Context;
import android.icu.text.DecimalFormat;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.io.Serializable;
import java.text.NumberFormat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Controller {

    static Customer loggedUser;


    private Controller() {

    }

    public static Customer getLoggedUser() {
        return loggedUser;
    }

    public static String getCurrency(Double value) {
        NumberFormat result = NumberFormat.getCurrencyInstance();
        return String.valueOf(result.format(value));
    }

    public static StockQuote getQuote(String stockSymbol) {

        StockQuote myStockQuote = new StockQuote();
        myStockQuote.symbol = stockSymbol;

        String url = "https://finnhub.io/api/v1/quote?symbol=" +
                stockSymbol +
                "&token=cmo6he1r01qj3mal97u0cmo6he1r01qj3mal97ug";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    myStockQuote.currentPrice = (double) response.getDouble("c");
                    myStockQuote.percentChange = (double) response.getDouble("dp");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(MyApplication.getAppContext()).add(request);


        return myStockQuote;
    }


}