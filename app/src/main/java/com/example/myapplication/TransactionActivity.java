package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TransactionActivity extends AppCompatActivity {
    static EditText editText_Qt, editText_StockSymbol;
    ImageView imageView_Logo;
    static TextView textView_Name, textView_CurrentPrice, textView_Change, textView_PercentChange, textView_Low, textView_High, textView_Open, textView_PreviousClose, textView_Total;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        if(getActionBar() !=null) getActionBar().hide();


        // Initialize Variables
        editText_StockSymbol = findViewById(R.id.editText_StockSymbol);
        editText_StockSymbol.setText(Controller.lastTransactionSymbol);
        editText_StockSymbol.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateAllInfo();
                Controller.lastTransactionSymbol = s.toString();
            }
        });
//        editText_StockSymbol.setOnKeyListener((v, keyCode, event) -> {
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
        textView_Name = findViewById(R.id.textView_Name);
        textView_CurrentPrice = findViewById(R.id.textView_CurrentPrice);
        textView_Change = findViewById(R.id.textView_Change);
        textView_PercentChange = findViewById(R.id.textView_PercentChange);
        textView_Low = findViewById(R.id.textView_Low);
        textView_High = findViewById(R.id.textView_High);
        textView_Open = findViewById(R.id.textView_Open);
        textView_PreviousClose = findViewById(R.id.textView_PreviousClose);
        textView_Total = findViewById(R.id.textView_Total);

        updateAllInfo();

    }

    private void updateQtd() {

        String quote = String.valueOf(textView_CurrentPrice.getText()).replace("$", "").replace(",","");

        double c = Double.parseDouble(quote);
        int qt;
        try {
            qt = Integer.parseInt(String.valueOf(editText_Qt.getText()));

        } catch (Exception e) {
            qt = 0;
        }
        textView_Total.setText(Controller.numberFormatCurrency.format(c * qt));
    }


    private void updateAllInfo() {

        String typedText = editText_StockSymbol.getText().toString().toUpperCase();
        if (Controller.companies.contains(typedText)) {

            StockQuote stockQuote = new StockQuote();
            stockQuote.symbol = editText_StockSymbol.getText().toString();

            Context context = this;

            Controller.getName(stockQuote, context);
            Controller.getQuote(stockQuote, context);
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




    public void buy(View view) {
        double totalOrder = Double.parseDouble(String.valueOf(textView_Total.getText()).replace("$","").replace(",",""));
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
        double totalOrder = Double.parseDouble(String.valueOf(textView_Total.getText()).replace("$","").replace(",",""));
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
    }

    public void goPortfolio(View view) {
        Intent intent = new Intent(this, PortfolioActivity.class);
        startActivity(intent);
    }

    public void goProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}
