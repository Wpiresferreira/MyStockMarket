package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class TransactionActivity extends AppCompatActivity{
    EditText editText_Qt;
    AutoCompleteTextView editText_StockSymbol;
    StockQuote selectedStockQuote;
    ImageView imageView_Logo;
    Timer timer;
    TextView textView_Name, textView_CurrentPrice, textView_Change, textView_PercentChange, textView_Low, textView_High, textView_Open, textView_PreviousClose, textView_Total;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        if (Controller.loggedUser == null) finish();


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        if (getActionBar() != null) getActionBar().hide();


        // Initialize Variables
        editText_StockSymbol = findViewById(R.id.editText_StockSymbol);
        imageView_Logo = findViewById(R.id.imageView_Logo);
        textView_Name = findViewById(R.id.textView_Name);
        textView_CurrentPrice = findViewById(R.id.textView_CurrentPrice);
        textView_Change = findViewById(R.id.textView_Change);
        textView_PercentChange = findViewById(R.id.textView_PercentChange);
        textView_Low = findViewById(R.id.textView_Low);
        textView_High = findViewById(R.id.textView_High);
        textView_Open = findViewById(R.id.textView_Open);
        textView_PreviousClose = findViewById(R.id.textView_PreviousClose);
        textView_Total = findViewById(R.id.textView_Total);

        if (selectedStockQuote == null) {
            selectedStockQuote = new StockQuote(Controller.lastTransactionSymbol, 0);
        }
        editText_StockSymbol.setText(selectedStockQuote.symbol);

        updateAllInfo();
        //editText_StockSymbol.setText(Controller.lastTransactionSymbol);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, Controller.COMPANIES);
        editText_StockSymbol.setAdapter(adapter);
        editText_StockSymbol.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selectedStockQuote = new StockQuote();
                selectedStockQuote.symbol = editText_StockSymbol.getText().toString().toUpperCase();
                updateAllInfo();
                editText_StockSymbol.clearFocus();

            }
        });
//        editText_StockSymbol.addTextChangedListener(new TextWatcher() {
//
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                updateAllInfo();
//                Controller.lastTransactionSymbol = s.toString();
//            }
//        });
////        editText_StockSymbol.setOnKeyListener((v, keyCode, event) -> {
//            updateAllInfo();
//            return false;
//        });
        editText_Qt = findViewById(R.id.editText_Qt);
        editText_Qt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateQtd();
            }
        });


        long period = (1000 * 10);
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            public void run() {
                try {
                    updateQuotes();
                } catch (Exception e) {
                    Log.wtf("WTF", "run TimerTask: " + e);
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, period);
//        updateAllInfo();

    }

    private void updateQtd() {

        String quote = String.valueOf(textView_CurrentPrice.getText()).replace("$", "").replace(",", "");

        double c = Double.parseDouble(quote);
        int qt;
        try {
            qt = Integer.parseInt(String.valueOf(editText_Qt.getText()));

        } catch (Exception e) {
            qt = 0;
        }
        textView_Total.setText(new DecimalFormat("#,##0.00").format(qt * c));
    }


    private void updateAllInfo() {
        if (selectedStockQuote != null && Controller.companies.contains(selectedStockQuote.symbol)) {
            updateDescription();
            updateQuotes();
        } else {
            textView_Name.setText("");// = findViewById(R.id.textView_Name);
            textView_CurrentPrice.setText("");// = findViewById(R.id.textView_CurrentPrice);
            textView_Change.setText("");// = findViewById(R.id.textView_Change);
            textView_PercentChange.setText("");// = findViewById(R.id.textView_PercentChange);
            textView_Low.setText("");// = findViewById(R.id.textView_Low);
            textView_High.setText(""); // = findViewById(R.id.textView_High);
            textView_Open.setText(""); // = findViewById(R.id.textView_Open);
            textView_PreviousClose.setText(""); //= findViewById(R.id.textView_PreviousClose);
        }
    }

    public void updateQuotes() {

        String url = "https://finnhub.io/api/v1/quote?symbol=" +
                selectedStockQuote.symbol +
                "&token=" +
                Controller.token;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                textView_CurrentPrice.setText(String.valueOf(response.getDouble("c")));
                textView_Change.setText(String.valueOf(response.getDouble("d")));
                textView_PercentChange.setText(String.valueOf(response.getDouble("dp")));
                textView_High.setText(String.valueOf(response.getDouble("h")));
                textView_Low.setText(String.valueOf(response.getDouble("l")));
                textView_Open.setText(String.valueOf(response.getDouble("o")));
                textView_PreviousClose.setText(String.valueOf(response.getDouble("pc")));

                updateTotal();

            } catch (Exception e) {
                Log.wtf("WTF", "updateQuotes: " + e);

            }
        }, error -> Log.wtf("WTF", "updateQuotes: Error response request."));
        Volley.newRequestQueue(this).add(request);

    }

    private void updateTotal() {
        Double total = 0.0;
        total = Double.parseDouble(textView_CurrentPrice.getText().toString()) *
                Integer.parseInt(editText_Qt.getText().toString());
        textView_Total.setText(new DecimalFormat("#,##0.00").format(total));
    }

    public void updateDescription() {

        String url = "https://finnhub.io/api/v1/stock/profile2?symbol=" +
                selectedStockQuote.symbol +
                "&token=" +
                Controller.token;


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                selectedStockQuote.name = response.getString("name");
                selectedStockQuote.imageURL = response.getString("logo");


                if (selectedStockQuote.imageURL != null) {

                    Glide.with(this)
                            .load(selectedStockQuote.imageURL)
                            .placeholder(R.drawable.b_investor_logo2)
                            .into(imageView_Logo);
                }
                textView_Name.setText(selectedStockQuote.name);
            } catch (Exception e) {
                Log.wtf("WTF", "updateDescription: " + e);

            }
        }, error -> Log.wtf("WTF", "updateDescription: Error response request."));
        Volley.newRequestQueue(this).add(request);

    }


    public void buy(View view) {
        double totalOrder = Double.parseDouble(String.valueOf(textView_Total.getText()).replace("$", "").replace(",", ""));
        int qtdOrder = Integer.parseInt(String.valueOf(editText_Qt.getText()));
        String stockOrder = String.valueOf(editText_StockSymbol.getText()).toUpperCase();

        if (Controller.loggedUser.customerCash.balance >= totalOrder) {
            Controller.loggedUser.customerCash.balance -= totalOrder;


            for (Stock stock : Controller.loggedUser.stocksInWallet) {
                if (stock.symbol.equals(stockOrder)) {
                    stock.balance += qtdOrder;
                    Controller.updateLoggedUser(getApplicationContext());
                    goPortfolio(view);
                    return;
                }
            }
            StockQuote myNewStock = new StockQuote(stockOrder, qtdOrder);
            Controller.loggedUser.stocksInWallet.add(myNewStock);

            //Save information to file
            Controller.updateLoggedUser(getApplicationContext());
            goPortfolio(view);
            return;
        }
        Toast.makeText(this, "Insufficient funds", Toast.LENGTH_SHORT).show();
    }

    public void sell(View view) {
        double totalOrder = Double.parseDouble(String.valueOf(textView_Total.getText()).replace("$", "").replace(",", ""));
        int qtdOrder = Integer.parseInt(String.valueOf(editText_Qt.getText()));
        String stockOrder = String.valueOf(editText_StockSymbol.getText()).toUpperCase();

        Stock customerStock = null;

        for (Stock stock : Controller.loggedUser.stocksInWallet) {
            if (stock.symbol.equals(stockOrder)) {
                if (stock.balance < qtdOrder) {
                    Toast.makeText(this, "Insufficient stocks", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Controller.loggedUser.customerCash.balance += totalOrder;
                    stock.balance -= qtdOrder;
                    customerStock = stock;
                }

                if (stock.balance == 0) {
                    Controller.loggedUser.stocksInWallet.remove(stock);
                }
                Controller.updateLoggedUser(getApplicationContext());
                goPortfolio(view);
                break;
            }
        }

        if (customerStock == null) {
            Toast.makeText(this, "Insufficient stocks", Toast.LENGTH_SHORT).show();
        }
        //Save information to file
        Controller.updateLoggedUser(getApplicationContext());

    }


    public void goWatchList(View view) {
        Intent intent = new Intent(this, WatchListActivity.class);
        startActivity(intent);
        finish();
    }

    public void goPortfolio(View view) {
        Intent intent = new Intent(this, PortfolioActivity.class);
        startActivity(intent);
        finish();
    }

    public void goProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(timer != null) timer.cancel();
    }

}
