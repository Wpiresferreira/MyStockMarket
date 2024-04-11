package com.example.myapplication;

import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class PortfolioActivity extends AppCompatActivity {
    TextView textView_Cash, textView_TotalBalance;
    RecyclerView recycler;
    StockQuoteAdapter adapter;
    Timer timer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);

        if (Controller.getLoggedUser() == null) finish();
        textView_Cash = findViewById(R.id.textView_Cash);
        textView_TotalBalance = findViewById(R.id.textView_TotalBalance);
        recycler = findViewById(R.id.recycler);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        if (getActionBar() != null) getActionBar().hide();


        // Show Cash e Total at the first time
        try {
            textView_Cash.setText(new DecimalFormat("#,##0.00").format(Controller.getLoggedUser().customerCash.balance));
            textView_TotalBalance.setText(new DecimalFormat("#,##0.00").format(Controller.getLoggedUser().customerCash.balance));
        } catch (Exception e) {
            textView_Cash.setText(R.string.unavailable);
            textView_TotalBalance.setText(R.string.unavailable);
        }

        setAdapter(); // Set the RecyclerView adapter
        updateDescription(new View(getApplicationContext()));
        updateTotal();

        // Update the quotes every 10 seconds using a timer
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

    private void setAdapter() {
        adapter = new StockQuoteAdapter(Controller.getLoggedUser().stocksInWallet);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
    }



    public void updateQuotes(View view) {

        for (Stock s : Controller.getLoggedUser().stocksInWallet) {
            String url = "https://finnhub.io/api/v1/quote?symbol=" + s.symbol + "&token=" + Controller.token;

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


                    adapter.notifyItemChanged(Controller.getLoggedUser().stocksInWallet.indexOf(s));
                    updateTotal();

                } catch (Exception e) {
                    Log.wtf("WTF", "updateQuotes: " + e);

                }
            }, error -> Log.wtf("WTF", "updateQuotes: Error response request."));
            Request<JSONObject> myRequest = Volley.newRequestQueue(this).add(request);


        }

    }

    private void updateTotal() {
        Double total = 0.0;
        total += Controller.getLoggedUser().customerCash.balance;
        for (Stock s : Controller.getLoggedUser().stocksInWallet) {
            total += s.balance * s.currentPrice;
        }

        textView_TotalBalance.setText(new DecimalFormat("#,##0.00").format(total));

    }

    public void updateDescription(View view) {

        for (Stock s : Controller.getLoggedUser().stocksInWallet) {

            if (!s.name.isEmpty()) continue;

            String url = "https://finnhub.io/api/v1/stock/profile2?symbol=" +
                    s.symbol + "&token=" +
                    Controller.token;


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
                try {
                    s.name = response.getString("name");
                    s.imageURL = response.getString("logo");

                    //Save information to file
                    Controller.updateLoggedUser(getApplicationContext());

                    adapter.notifyItemChanged(Controller.getLoggedUser().stocksInWallet.indexOf(s));
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
        finish();
        startActivity(intent);
    }

    public void goTransactions(View view) {
        timer.cancel();
        Intent intent = new Intent(this, TransactionActivity.class);
        finish();
        startActivity(intent);
    }

    public void goProfile(View view) {
        timer.cancel();
        Intent intent = new Intent(this, ProfileActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null) timer.cancel();
    }


}
