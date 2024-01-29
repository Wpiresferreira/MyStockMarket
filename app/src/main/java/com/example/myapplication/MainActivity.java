package com.example.myapplication;

import android.app.Activity;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    TextView textView, textView6, textView7, textView17, textView31, textView24, textView26, textView33, textViewOrder;
    Button button1, buttonLogin, buttonCancel;
    List<Stock> allStocks;
    Customer loggedUser;
    StockQuote myQuote = new StockQuote();
    float totalBalance;
    List<TextView> panel;
    List<TextView> panelMarket;

    EditText editTextNumber;
    private CountDownTimer timer;

    int countRefresh;

    int count;

    Activity activityMarket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SimpleDateFormat sdf = new SimpleDateFormat("'Date\n'dd-MM-yyyy '\n\nand\n\nTime\n'HH:mm:ss z");


        //testing();
        //allStocks = new ArrayList<>();
        login();
    }

    private void testing() {
        setContentView(R.layout.activity_testing);

        LinearLayout my_root = findViewById(R.id.linearLayoutMain);

        TextView title = new TextView(this);

        title.setText("My Title");
        title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        my_root.addView(title);

        testingGetQuote("GOOG");

        TextView stockLine = new TextView(this);
        stockLine.setText("GOOG \n Current price" + myQuote.currentPrice + " \n  " + myQuote.percentChange + "%   ");
        stockLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        my_root.addView(stockLine);


    }

    public void testingGetQuote(String symbol) {

        String url = "https://finnhub.io/api/v1/quote?symbol=GOOG&token=cmo6he1r01qj3mal97u0cmo6he1r01qj3mal97ug";


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    myQuote.currentPrice = (float) response.getDouble("c");
                    myQuote.percentChange = (float) response.getDouble("dp");


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(this).add(request);


    }

    public void login() {
        setContentView(R.layout.activity_login);
    }

    public void doLogin(View view) {
        // TODO
        // This method will check Username and Password and if they match...
        // Update loggedUser and change the page.

        Cash myCash = new Cash();
        myCash.balance = 10000;

        Stock myStock = new Stock();
        myStock.balance = 10;
        myStock.symbol = "GOOG";
        myStock.description = "Google Inc.";

        Stock myStock1 = new Stock();
        myStock1.balance = 7;
        myStock1.symbol = "MSFT";
        myStock1.description = "Microsoft Inc..";

        Stock myStock2 = new Stock();
        myStock2.balance = 12;
        myStock2.symbol = "BTC-USD";
        myStock2.description = "Bitcoin USD";

        Stock myStock3 = new Stock();
        myStock3.symbol = "AAPL";
        myStock3.description = "Apple Inc.";

        List<Stock> myCustomerListStock = new ArrayList<>();
//        myCustomerListStock.add(myStock);
        myCustomerListStock.add(myStock1);
        myCustomerListStock.add(myStock2);

        List<Stock> myCustomerListMarket = new ArrayList<>();
        myCustomerListMarket.add(myStock);
        myCustomerListMarket.add(myStock1);
        myCustomerListMarket.add(myStock2);
        myCustomerListMarket.add(myStock3);

        loggedUser = new Customer("Wagner", "w", "w", myCash, myCustomerListStock, myCustomerListMarket);

        //loadStockSymbols();

        goMainScreen(view);


    }

    private void loadStockSymbols() {

        allStocks = new ArrayList<>();

        String url = "https://finnhub.io/api/v1/stock/symbol?exchange=US&token=";
        url += "cmo6he1r01qj3mal97u0cmo6he1r01qj3mal97ug";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        Stock stock = new Stock();
                        JSONObject objectInArray = response.getJSONObject(i);
                        stock.currency = objectInArray.getString("currency");
                        stock.symbol = objectInArray.getString("symbol");
                        stock.description = objectInArray.getString("description");
                        allStocks.add(stock);
                    }


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(this).add(request);


    }

    public void goToMarket(View view) {
        setContentView(R.layout.activity_market);
        activityMarket = this;

        panelMarket = new ArrayList<>();
        panelMarket.add(findViewById(R.id.textView8));
        panelMarket.add(findViewById(R.id.textView9));
        panelMarket.add(findViewById(R.id.textView10));
        panelMarket.add(findViewById(R.id.textView11));
        panelMarket.add(findViewById(R.id.textView12));
        panelMarket.add(findViewById(R.id.textView13));

        generateMarket();
        timer = new CountDownTimer(5000, 20) {

            @Override
            public void onTick(long millisUntilFinished) {
                try {
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            }

            @Override
            public void onFinish() {
                try {
                    generateMarket();
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            }
        }.start();
        //timer.start();


    }

    private void generateMarket() {
        count = 0;

        if (this == activityMarket) {

            for (Stock stock : loggedUser.customerStockView) {

                String url = "https://finnhub.io/api/v1/quote?symbol=" +
                        stock.symbol +
                        "&token=cmo6he1r01qj3mal97u0cmo6he1r01qj3mal97ug";

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Float c = (float) response.getDouble("c");
                            //Float dp = (float) response.getDouble("dp");

                            panelMarket.get(loggedUser.customerStockView.indexOf(stock)).setText(stock.symbol + "  Price : " + c);

                            count++;
//                        if (dp > 0) {
//                            textView.setBackgroundColor(Color.BLUE);
//                            textView6.setBackgroundColor(Color.BLUE);
//                            textView7.setBackgroundColor(Color.BLUE);
//
//                        } else if (dp < 0) {
//                            textView.setBackgroundColor(Color.RED);
//                            textView6.setBackgroundColor(Color.RED);
//                            textView7.setBackgroundColor(Color.RED);
//                        } else {
//                            textView.setBackgroundColor(Color.BLACK);
//                            textView6.setBackgroundColor(Color.BLACK);
//                            textView7.setBackgroundColor(Color.BLACK);
//                        }
//                        textView.setText("BTC-USD");
//                        textView6.setText(c.toString());
//                        textView7.setText(dp.toString() + "%");

                            myQuote.currentPrice = c;
                            //myQuote.percentChange = dp;


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                Volley.newRequestQueue(this).add(request);


            }
        }

    }

    public void goMainScreen(View view) {
        totalBalance = 0;
        setContentView(R.layout.activity_main);
        button1 = findViewById(R.id.button1);
        textView17 = findViewById(R.id.tv_myCash);
        textView31 = findViewById(R.id.tv_myTotalBalance);

        panel = new ArrayList<>();
        panel.add(findViewById(R.id.tv_myStock1));
        panel.add(findViewById(R.id.tv_myStock2));
        panel.add(findViewById(R.id.tv_myStock3));
        panel.add(findViewById(R.id.tv_myStock4));
        panel.add(findViewById(R.id.tv_myStock5));
        panel.add(findViewById(R.id.tv_myStock6));
        panel.add(findViewById(R.id.tv_myStock7));
        panel.add(findViewById(R.id.tv_myStock8));
        panel.add(findViewById(R.id.tv_myStock9));
        panel.add(findViewById(R.id.tv_myStock10));
        generateWallet();
    }

    private void generateWallet() {
        LinearLayout my_root = findViewById(R.id.linearLayoutWallet);

        String cashBalance = new DecimalFormat("US$ #,##0.00").format(loggedUser.customerCash.balance);
        textView17.setText("Cash  " + cashBalance);

        totalBalance += loggedUser.customerCash.balance;

        for (Stock stock : loggedUser.customerListStock) {

            String url = "https://finnhub.io/api/v1/quote?symbol=" +
                    stock.symbol +
                    "&token=cmo6he1r01qj3mal97u0cmo6he1r01qj3mal97ug";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Float c = (float) response.getDouble("c");
                        //Float dp = (float) response.getDouble("dp");

                        String stockQuote = new DecimalFormat("US$ #,##0.00").format(c);
                        String stockQuoteTotal = new DecimalFormat("US$ #,##0.00").format(c * stock.balance);

                        panel.get(loggedUser.customerListStock.indexOf(stock)).setText(stock.balance + " x " + stock.symbol + "  Price : " + stockQuote + " = " + stockQuoteTotal);
                        totalBalance += stock.balance * c;
                        String formatTotalBalance = new DecimalFormat("US$ #,##0.00").format(totalBalance);
                        textView31.setText(formatTotalBalance);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            Volley.newRequestQueue(this).add(request);


        }
    }



    public void ButtonClick(View view) {
        button1.setText("Fui Clicado!");
    }

    public void getQuote(String symbol) {


        String url = "https://finnhub.io/api/v1/quote?symbol=GOOG&token=cmo6he1r01qj3mal97u0cmo6he1r01qj3mal97ug";


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    myQuote.currentPrice = (float) response.getDouble("c");
                    myQuote.change = (float) response.getDouble("d");
                    myQuote.percentChange = (float) response.getDouble("dp");

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(this).add(request);


//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    result.currentPrice = (float) response.getDouble("c");
//                    result.change = (float) response.getDouble("d");
//                    result.percentChange = (float) response.getDouble("dp");
//                    result.highPriceOfTheDay = (float) response.getDouble("h");
//                    result.lowPriceOfTheDay = (float) response.getDouble("l");
//                    result.openPriceOfTheDay = (float) response.getDouble("o");
//                    result.previousClosePrice = (float) response.getDouble("pc");
//
//                } catch (JSONException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        Volley.newRequestQueue(this).add(request);

        //return result;
    }

    public void goOrder(View view) {
        setContentView(R.layout.activity_order);

        TextView textViewClicked = (TextView) view;
        String symbol = textViewClicked.getText().toString();

        Log.d("TAG", symbol);
        textView24 = findViewById(R.id.textView24);
        textView24.setText(symbol);


        String url = "https://finnhub.io/api/v1/quote?symbol=" +
                "GOOG" + //stock.symbol +
                "&token=cmo6he1r01qj3mal97u0cmo6he1r01qj3mal97ug";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Float c = (float) response.getDouble("c");
                    textView26 = findViewById(R.id.textView26);
                    textView26.setText(String.valueOf(c));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(this).add(request);


    }

    public void buy(View view) {
        editTextNumber = findViewById(R.id.editTextNumber);
        Button b = findViewById(R.id.button9);
        TextView textView33 = findViewById(R.id.textView33);


        float totalPrice = Float.parseFloat(textView26.getText().toString()) * Float.parseFloat(editTextNumber.getText().toString());

        if (totalPrice > loggedUser.customerCash.balance) {
            textView33.setText("Insuficient funds");
        } else {
            loggedUser.customerCash.balance -= totalPrice;
            boolean hasThisStock = false;

            for (Stock stock : loggedUser.customerListStock) {
                if (stock.symbol == "GOOG") {
                    hasThisStock = true;
                    break;
                }
            }

            if (hasThisStock) {
                for (Stock stock : loggedUser.customerListStock) {
                    if (stock.symbol == "GOOG") {
                        stock.balance += Integer.parseInt(editTextNumber.getText().toString());
                        break;
                    }
                }
            } else {
                Stock newStock = new Stock();
                newStock.balance = Integer.parseInt(editTextNumber.getText().toString());
                newStock.symbol = "GOOG";
                loggedUser.customerListStock.add(newStock);

            }
            goToMarket(view);
        }
        b.setText(String.valueOf(totalPrice));//Log.d("Just Testing", symbol);


    }

    public void sell(View view) {
        editTextNumber = findViewById(R.id.editTextNumber);
        TextView textView33 = findViewById(R.id.textView33);

        boolean hasEnoughStock;

        for (Stock stock : loggedUser.customerListStock) {
            if (stock.symbol == "GOOG" && stock.balance >= Integer.parseInt(editTextNumber.getText().toString())) {
                loggedUser.customerCash.balance += Integer.parseInt(editTextNumber.getText().toString()) * Float.parseFloat(textView26.getText().toString());
                for (Stock stockToSell : loggedUser.customerListStock) {
                    if (stockToSell.symbol == "GOOG") {
                        stockToSell.balance -= Integer.parseInt(editTextNumber.getText().toString());
                        if (stockToSell.balance == 0) {
                            loggedUser.customerListStock.remove(stockToSell);
                        }

                    }
                }
            }
        }


        goToMarket(view);
    }
    //b.setText(String.valueOf(totalPrice));//Log.d("Just Testing", symbol);


}

