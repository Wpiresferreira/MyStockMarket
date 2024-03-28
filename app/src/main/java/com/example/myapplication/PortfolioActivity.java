package com.example.myapplication;

import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PortfolioActivity extends AppCompatActivity {
    TextView textView_Cash,
            textView_TotalBalance,
    textView_justCounter;
    RecyclerView recycler;
    StockQuoteAdapter adapter;
    List<StockQuote> stockQuoteList;
    Timer timer;
    int justCounter = 0;
    NumberFormat numberFormatCurrency = NumberFormat.getCurrencyInstance();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);
        textView_Cash = findViewById(R.id.textView_Cash);
        textView_TotalBalance = findViewById(R.id.textView_TotalBalance);
        textView_justCounter = findViewById(R.id.justCounter);
        textView_justCounter.setText(String.valueOf(justCounter));
        recycler = findViewById(R.id.recycler);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        if(getActionBar() !=null) getActionBar().hide();

        buildTheScreen();



        loadList();
        setAdapter();
        updateDescription(new View(getApplicationContext()));
        updateTotal();

        long period = (1000 * 10);
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            public void run() {
                try {
                    updateQuotes(new View(getApplicationContext()));
                } catch (Exception e) {
                    Log.wtf("WTF", "run TimerTask: " + e);
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, period);
    }

    private void loadList() {

        stockQuoteList = Controller.loggedUser.stocksInWallet;

        if (stockQuoteList == null) {
            stockQuoteList = new ArrayList<>();
        }
    }

    private void setAdapter() {

        adapter = new StockQuoteAdapter(stockQuoteList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
    }


    private void buildTheScreen() {

        // First, try to load cash balance
        try {
            textView_Cash.setText(numberFormatCurrency.format(Controller.loggedUser.customerCash.balance));
            textView_TotalBalance.setText(numberFormatCurrency.format(Controller.loggedUser.customerCash.balance));
        } catch (Exception e) {
            textView_Cash.setText(R.string.unavailable);
            textView_TotalBalance.setText(R.string.unavailable);
        }

    }

    public void updateQuotes(View view) {

        for (StockQuote s : Controller.loggedUser.stocksInWallet) {
            String url = "https://finnhub.io/api/v1/quote?symbol=" +
                    s.symbol +
                    "&token=" +
                    Controller.token;

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
                try {
                    s.currentPrice = response.getDouble("c");
                    s.change = response.getDouble("d");
                    s.percentChange = response.getDouble("dp");
                    s.highPriceOfTheDay = response.getDouble("h");
                    s.lowPriceOfTheDay = response.getDouble("l");
                    s.openPriceOfTheDay = response.getDouble("o");
                    s.previousClosePrice = response.getDouble("pc");

                    //Save information to file
                    Controller.updateLoggedUser(getApplicationContext());

                    adapter.notifyDataSetChanged();
                    updateTotal();
                    justCounter++;
                    textView_justCounter.setText(String.valueOf(justCounter));

                } catch (Exception e) {
                    Log.wtf("WTF", "updateQuotes: " + e);

                }
            }, error -> Log.wtf("WTF", "updateQuotes: Error response request."));
            Volley.newRequestQueue(this).add(request);
        }

    }

    private void updateTotal(){
        Double total = 0.0;
        total += Controller.loggedUser.customerCash.balance;
        for(StockQuote s : Controller.loggedUser.stocksInWallet){
            total += s.balance* s.currentPrice;
        }

        textView_TotalBalance.setText(new DecimalFormat("#,##0.00").format(total));

    }

    public void updateDescription(View view) {

        for (StockQuote s : Controller.loggedUser.stocksInWallet) {

            if(!s.name.isEmpty()) ;//return;

            String url = "https://finnhub.io/api/v1/stock/profile2?symbol=" +
                    s.symbol +
                    "&token=" +
                    Controller.token;



            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
                try {
                    s.name = response.getString("name");
                    s.imageURL = response.getString("logo");

                    //Save information to file
                    Controller.updateLoggedUser(getApplicationContext());

                    adapter.notifyDataSetChanged();
                    updateTotal();

                } catch (Exception e) {
                    Log.wtf("WTF", "updateDescription: " + e);

                }
            }, error -> Log.wtf("WTF", "updateDescription: Error response request."));
            Volley.newRequestQueue(this).add(request);
        }

    }



    public void goWatchList(View view) {
        timer.cancel();
        Intent intent = new Intent(this, WatchListActivity.class);
        startActivity(intent);
    }

    public void goTransactions(View view) {
        timer.cancel();
        Intent intent = new Intent(this, TransactionActivity.class);
        startActivity(intent);
    }

    public void goProfile(View view) {
        timer.cancel();
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(timer != null) timer.cancel();
    }


}
