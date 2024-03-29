package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class WatchListActivity extends AppCompatActivity {

    EditText editText_StockToAdd;
    WatchStockAdapter adapter;
    RecyclerView recycler;
    Timer timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchlist);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        if(getActionBar() !=null) getActionBar().hide();

        editText_StockToAdd = findViewById(R.id.editText_StockToAdd);
        recycler = findViewById(R.id.recycler);



        setAdapter();
        updateDescription();

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
    }

    private void updateQuotes() {
        for (StockQuote s : Controller.loggedUser.stocksInWatchlist) {
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

                    //Toast.makeText(this, "Index of" + Controller.loggedUser.stocksInWallet.indexOf(s), Toast.LENGTH_SHORT).show();

                    adapter.notifyItemChanged(Controller.loggedUser.stocksInWatchlist.indexOf(s));

                } catch (Exception e) {
                    Log.wtf("WTF", "updateQuotes: " + e);

                }
            }, error -> Log.wtf("WTF", "updateQuotes: Error response request."));
            Volley.newRequestQueue(this).add(request);
        }

    }

    private void updateDescription() {

        for (StockQuote s : Controller.loggedUser.stocksInWatchlist) {

            //if(s.name != null) return;

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
                    adapter.notifyItemChanged(Controller.loggedUser.stocksInWatchlist.indexOf(s));

                } catch (Exception e) {
                    Log.wtf("WTF", "updateDescription: " + e);

                }
            }, error -> Log.wtf("WTF", "updateDescription: Error response request."));
            Volley.newRequestQueue(this).add(request);
        }

    }

    private void setAdapter() {

        adapter = new WatchStockAdapter(Controller.loggedUser.stocksInWatchlist);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
    }

    public void addStock(View view) {

        String textInserted = editText_StockToAdd.getText().toString().toUpperCase();
        //Check if the stock is in the S&P500
        if (!Controller.companies.contains(textInserted)) {
            Toast.makeText(this, "Symbol not found", Toast.LENGTH_SHORT).show();
            return;
        }

        //Check if the stock already exist in the list
        for (Stock stock : Controller.loggedUser.stocksInWatchlist) {
            if (Objects.equals(stock.symbol, textInserted)) {
                Toast.makeText(this, "Symbol is already listed", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Check the size of the list
        if (Controller.loggedUser.stocksInWatchlist.size() >= 10) {
            Toast.makeText(this, "Max size Watchlist is 10 stocks", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert the stock to list and create a box, and update the screen
        StockQuote newStock = new StockQuote();
        newStock.symbol = textInserted;
        Controller.loggedUser.stocksInWatchlist.add(newStock);

        updateDescription();

    }

    public void goPortfolio(View view) {
        Intent intent = new Intent(this, PortfolioActivity.class);
        startActivity(intent);
    }

    public void goTransactions(View view) {
        Intent intent = new Intent(this, TransactionActivity.class);
        startActivity(intent);
    }

    public void goProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(timer != null) timer.cancel();
    }
}
