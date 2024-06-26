package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

public class TransactionActivity extends AppCompatActivity {
    EditText editText_Qt;
    AutoCompleteTextView editText_StockSymbol;
    Stock selectedStock;
    ImageView imageView_Logo;
    Timer timer;
    TextView textView_Name, textView_CurrentPrice, textView_Change, textView_PercentChange, textView_Low, textView_High, textView_Open, textView_PreviousClose,
            textView_Total;
    boolean isCompleteLoaded;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

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
        isCompleteLoaded = false;
        
        if (selectedStock == null) {
            selectedStock = new Stock(Controller.lastTransactionSymbol, 0);
        }
        editText_StockSymbol.setText(selectedStock.symbol);

        updateAllInfo();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, Controller.companies.toArray(new String[0]));
        editText_StockSymbol.setAdapter(adapter);
        editText_StockSymbol.setOnItemClickListener((parent, view, position, id) -> {

            selectedStock = new Stock();

            String[] itemClicked = editText_StockSymbol.getText().toString().toUpperCase().split(" ");
            selectedStock.symbol = itemClicked[0];
            Controller.lastTransactionSymbol = selectedStock.symbol;
            updateAllInfo();
            editText_StockSymbol.clearFocus();

            // After choose a stock, hide the keyboard
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            View view1 = this.getCurrentFocus();
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view1 == null) {
                view1 = new View(this);
            }
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);

        });

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
        updateAllInfo();

    }

    private void updateQtd() {

        String quote = String.valueOf(textView_CurrentPrice.getText()).replace("$", "").replace(",", "");
        double c;
        try {
            c = Double.parseDouble(quote);

        } catch (Exception e) {
            c = 1;
        }

        int qt;
        try {
            qt = Integer.parseInt(String.valueOf(editText_Qt.getText()));
            textView_Total.setText(new DecimalFormat("#,##0.00").format(qt * c));
        } catch (Exception e) {
            textView_Total.setText("--");
        }

    }


    private void updateAllInfo() {
        if (selectedStock != null) { // && Controller.companies.contains(selectedStockQuote.symbol)) {
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
        isCompleteLoaded = false;

        String url = "https://finnhub.io/api/v1/quote?symbol=" +
                selectedStock.symbol +
                "&token=" +
                Controller.token;

        @SuppressLint("SetTextI18n") JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                textView_CurrentPrice.setText(new DecimalFormat("#,##0.00").format(response.getDouble("c")));
                textView_Change.setText(new DecimalFormat("#,##0.00").format(response.getDouble("d")));
                if (response.getDouble("dp") < 0) {
                    textView_PercentChange.setText(new DecimalFormat("#,##0.00").format(response.getDouble("dp")) + "%" + "\uF13A");
                    textView_PercentChange.setTextColor(Color.RED);
                } else if (response.getDouble("dp") > 0) {
                    textView_PercentChange.setText(new DecimalFormat("#,##0.00").format(response.getDouble("dp")) + "%" + "\uF139");
                    textView_PercentChange.setTextColor(Color.rgb(0, 128, 0));
                } else {
                    textView_PercentChange.setText(new DecimalFormat("#,##0.00").format(response.getDouble("dp")) + "%" + " - ");
                    textView_PercentChange.setTextColor(Color.BLACK);

                }
                textView_High.setText(new DecimalFormat("#,##0.00").format(response.getDouble("h")));
                textView_Low.setText(new DecimalFormat("#,##0.00").format(response.getDouble("l")));
                textView_Open.setText(new DecimalFormat("#,##0.00").format(response.getDouble("o")));
                textView_PreviousClose.setText(new DecimalFormat("#,##0.00").format(response.getDouble("pc")));

                updateTotal();
                isCompleteLoaded = true;

            } catch (Exception e) {
                Log.wtf("WTF", "updateQuotes: " + e);

            }
        }, error -> Log.wtf("WTF", "updateQuotes: Error response request."));
        Volley.newRequestQueue(this).add(request);

    }

    private void updateTotal() {
        Double total;
        total = Double.parseDouble(textView_CurrentPrice.getText().toString().replace(",", "")) *
                Integer.parseInt(editText_Qt.getText().toString());
        textView_Total.setText(new DecimalFormat("#,##0.00").format(total));
    }

    public void updateDescription() {

        String url = "https://finnhub.io/api/v1/stock/profile2?symbol=" +
                selectedStock.symbol +
                "&token=" +
                Controller.token;


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                selectedStock.name = response.getString("name");
                selectedStock.imageURL = response.getString("logo");


                if (!selectedStock.imageURL.isEmpty()) {

                    Glide.with(this)
                            .load(selectedStock.imageURL)
                            .placeholder(R.drawable.b_investor_logo2)
                            .into(imageView_Logo);
                }
                textView_Name.setText(selectedStock.name);
            } catch (Exception e) {
                Log.wtf("WTF", "updateDescription: " + e);

            }
        }, error -> Log.wtf("WTF", "updateDescription: Error response request."));
        Volley.newRequestQueue(this).add(request);

    }


    public void buy(View view) {

        if (!isCompleteLoaded) {
            Toast.makeText(this, "Wait to load the quote", Toast.LENGTH_SHORT).show();
            return;
        }
        double totalOrder = Double.parseDouble(String.valueOf(textView_Total.getText()).replace("$", "").replace(",", ""));
        int qtdOrder = Integer.parseInt(String.valueOf(editText_Qt.getText()));
        String stockOrder = String.valueOf(selectedStock.symbol);

        if (Controller.getLoggedUser().customerCash.balance >= totalOrder) {
            Controller.getLoggedUser().customerCash.balance -= totalOrder;


            for (Stock stock : Controller.getLoggedUser().stocksInWallet) {
                if (stock.symbol.equals(stockOrder)) {
                    stock.balance += qtdOrder;
                    Controller.updateLoggedUser(getApplicationContext());
                    goPortfolio(view);
                    return;
                }
            }
            Stock myNewStock = new Stock(stockOrder, qtdOrder);
            Controller.getLoggedUser().stocksInWallet.add(myNewStock);

            //Save information to file
            Controller.updateLoggedUser(getApplicationContext());
            goPortfolio(view);
            return;
        }
        Toast.makeText(this, "Insufficient funds", Toast.LENGTH_SHORT).show();
    }

    public void sell(View view) {
        if (!isCompleteLoaded) {
            Toast.makeText(this, "Wait to load the quote", Toast.LENGTH_SHORT).show();
            return;
        }
        double totalOrder = Double.parseDouble(String.valueOf(textView_Total.getText()).replace("$", "").replace(",", ""));
        int qtdOrder = Integer.parseInt(String.valueOf(editText_Qt.getText()));
        String stockOrder = String.valueOf(selectedStock.symbol);

        Stock customerStock = null;

        for (Stock stock : Controller.getLoggedUser().stocksInWallet) {
            if (stock.symbol.equals(stockOrder)) {
                if (stock.balance < qtdOrder) {
                    Toast.makeText(this, "Insufficient stocks", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Controller.getLoggedUser().customerCash.balance += totalOrder;
                    stock.balance -= qtdOrder;
                    customerStock = stock;
                }

                if (stock.balance == 0) {
                    Controller.getLoggedUser().stocksInWallet.remove(stock);
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
        if (timer != null) timer.cancel();
    }

}
