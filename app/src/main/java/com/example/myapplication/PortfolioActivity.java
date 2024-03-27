package com.example.myapplication;

import static com.example.myapplication.R.drawable.box_with_corner;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PortfolioActivity extends AppCompatActivity {
    TextView textView_Cash, textView_TotalBalance, textView_StockName, textView_StockCurrent, textView_StockDeltaPercent, textView_StockValue,
    textView_justCounter;
    RecyclerView recycler;
    StockQuoteAdapter adapter;
    List<StockQuote> stockQuoteList;
    int justCounter = 0;
    LinearLayout containerPortfolio;
    int actualId;
    HashMap<String, Integer> myTextBoxIds = new HashMap<>();
    NumberFormat numberFormatCurrency = NumberFormat.getCurrencyInstance();
    NumberFormat numberFormatInstance = NumberFormat.getNumberInstance();
    double totalBalance;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);
        textView_Cash = findViewById(R.id.textView_Cash);
        textView_TotalBalance = findViewById(R.id.textView_TotalBalance);
        containerPortfolio = findViewById(R.id.containerPortfolio);
        textView_justCounter = findViewById(R.id.justCounter);
        textView_justCounter.setText(String.valueOf(justCounter));
        recycler = findViewById(R.id.recycler);
        buildTheScreen();

        loadList();
        setAdapter();
        updateDescription(new View(getApplicationContext()));
        updateTotal();

        long TEMPO = (1000 * 5);
        Timer timer;
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
        timer.scheduleAtFixedRate(timerTask, TEMPO, TEMPO);


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

        // Total balance initializes with cash balance
//        totalBalance = Controller.loggedUser.customerCash.balance;


//        if(Controller.loggedUser.stocksInWallet !=null) {
//            for (Stock stock : Controller.loggedUser.stocksInWallet) {
//                StockQuote stockQuote = new StockQuote();
//                stockQuote.symbol = stock.symbol;
//                stockQuote.shares = stock.balance;
//
//                createBoxes(stockQuote);
//                updateQuotes(stockQuote);
//                //update2s();
//            }
//        }else{
//
//        }
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
                    ;
                }
            }, error -> {
                Log.wtf("WTF", "updateQuotes: Error response request.");
            });
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

            if(!s.name.isEmpty()) return;

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
                    ;
                }
            }, error -> {
                Log.wtf("WTF", "updateDescription: Error response request.");
            });
            Volley.newRequestQueue(this).add(request);
        }

    }


    private void updateQuotes(StockQuote stockQuote) {

        /// GET QUOTE
        String url = "https://finnhub.io/api/v1/quote?symbol=" +
                stockQuote.symbol +
                "&token=cmo6he1r01qj3mal97u0cmo6he1r01qj3mal97ug";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                int profitColor;
                stockQuote.currentPrice = response.getDouble("c");
                try {
                    stockQuote.percentChange = response.getDouble("dp");
                } catch (Exception e) {
                    stockQuote.percentChange = 0;
                }

                textView_StockCurrent = findViewById(myTextBoxIds.get("current" + stockQuote.symbol));
                textView_StockCurrent.setText(String.valueOf(numberFormatCurrency.format(stockQuote.currentPrice)));

                textView_StockValue = findViewById(myTextBoxIds.get("value" + stockQuote.symbol));
                textView_StockValue.setText(String.valueOf(numberFormatCurrency.format(stockQuote.currentPrice * stockQuote.balance)));

                totalBalance += stockQuote.currentPrice * stockQuote.balance;
                textView_TotalBalance.setText(String.valueOf(numberFormatCurrency.format(totalBalance)));

                textView_StockDeltaPercent = findViewById((myTextBoxIds.get("change" + stockQuote.symbol)));
                textView_StockDeltaPercent.setText(numberFormatInstance.format(stockQuote.percentChange) + "%");

                if (stockQuote.percentChange > 0) {
                    profitColor = Color.argb(255, 0, 128, 0);
                } else if (stockQuote.percentChange < 0) {
                    profitColor = Color.argb(255, 255, 0, 0);
                } else {
                    profitColor = Color.BLACK;
                }

                textView_StockDeltaPercent.setTextColor(profitColor);

                //stockQuote.imageURL = response.getString("logo");

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(this).add(request);
    }

    private void createBoxes(StockQuote stockQuote) {

        // Create a frame that will be the container of TexViews
        FrameLayout frameLayout = new FrameLayout(this);
        LinearLayout.LayoutParams paramsFrame = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        paramsFrame.setMargins(0, getPx(4), 0, 0);

        frameLayout.setLayoutParams(paramsFrame);
        frameLayout.setBackground(getResources().getDrawable(box_with_corner));


        // Create TextView for Symbol and fill it
        TextView textViewSymbol = new TextView(this);
        LinearLayout.LayoutParams paramsSymbol = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsSymbol.setMargins(getPx(20), 0, 0, 0);
        textViewSymbol.setLayoutParams(paramsSymbol);

        //textViewSymbol.setId(Integer.parseInt("textView_"+ stockQuote.symbol));
        textViewSymbol.setText(stockQuote.symbol);
        Typeface typeface = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            typeface = getResources().getFont(R.font.poppins_bold);
        }
        textViewSymbol.setTypeface(typeface);
        textViewSymbol.setTextSize(18);
        textViewSymbol.setTextColor(getResources().getColor(com.google.android.material.R.color.material_dynamic_primary20));


        TextView textViewName = new TextView(this);
        LinearLayout.LayoutParams params0 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params0.setMargins(getPx(20), getPx(26), 0, 0);

        textViewName.setLayoutParams(params0);
        textViewName.setText(stockQuote.name);
        Typeface typeface0 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            typeface0 = getResources().getFont(R.font.poppins_regular);
        }
        textViewName.setTypeface(typeface0);
        textViewName.setTextColor(getResources().getColor(com.google.android.material.R.color.material_dynamic_primary20));
        actualId = View.generateViewId();
        myTextBoxIds.put("name" + stockQuote.symbol, actualId);
        textViewName.setId(actualId);


        TextView textViewShare = new TextView(this);
        LinearLayout.LayoutParams params5 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params5.setMargins(getPx(170), getPx(26), 0, 0);

        textViewShare.setLayoutParams(params5);
        textViewShare.setText(String.valueOf(stockQuote.balance));


        Typeface typeface5 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            typeface5 = getResources().getFont(R.font.poppins_regular);
        }
        textViewShare.setTypeface(typeface5);
        textViewShare.setTextColor(getResources().getColor(com.google.android.material.R.color.material_dynamic_primary20));


        TextView textViewChange = new TextView(this);
        LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        //params4.setMargins( 0,getPx(20),getPx(20),0);
        params4.rightMargin = getPx(20);
        params4.topMargin = getPx(26);
        params4.leftMargin = getPx(280);


        params4.gravity = Gravity.RIGHT;
        textViewChange.setLayoutParams(params4);

        textViewChange.setText(numberFormatInstance.format(stockQuote.percentChange) + "%");
        actualId = View.generateViewId();
        myTextBoxIds.put("change" + stockQuote.symbol, actualId);
        textViewChange.setId(actualId);


        Typeface typeface4 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            typeface4 = getResources().getFont(R.font.poppins_regular);
        }
        textViewChange.setTypeface(typeface4);
        //textViewChange.setTextSize(14);
        textViewChange.setTextColor(getResources().getColor(com.google.android.material.R.color.material_dynamic_primary20));
        if (stockQuote.percentChange < 0) {
            textViewChange.setTextColor(Color.RED);
        } else if (stockQuote.percentChange > 0) {
            textViewChange.setTextColor(Color.argb(255, 0, 128, 0));
        }


        TextView textViewCurrent = new TextView(this);
        LinearLayout.LayoutParams paramsCurrent = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsCurrent.setMargins(getPx(280), 0, 0, 0);

        textViewCurrent.setLayoutParams(paramsCurrent);
        textViewCurrent.setText(String.valueOf(numberFormatCurrency.format(stockQuote.currentPrice)));
        Typeface typeface2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            typeface2 = getResources().getFont(R.font.poppins_bold);
        }
        textViewCurrent.setTypeface(typeface2);
        textViewCurrent.setTextSize(18);
        textViewCurrent.setTextColor(getResources().getColor(com.google.android.material.R.color.material_dynamic_primary20));

        actualId = View.generateViewId();
        myTextBoxIds.put("current" + stockQuote.symbol, actualId);
        textViewCurrent.setId(actualId);


        TextView textViewValue = new TextView(this);
        LinearLayout.LayoutParams paramsValue = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsValue.setMargins(getPx(170), 0, 0, 0);

        textViewValue.setLayoutParams(paramsValue);
        textViewValue.setText("");
        Typeface typefaceValue = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            typefaceValue = getResources().getFont(R.font.poppins_bold);
        }
        textViewValue.setTypeface(typefaceValue);
        textViewValue.setTextSize(18);
        textViewValue.setTextColor(getResources().getColor(com.google.android.material.R.color.material_dynamic_primary20));

        actualId = View.generateViewId();
        myTextBoxIds.put("value" + stockQuote.symbol, actualId);
        textViewValue.setId(actualId);


        frameLayout.addView(textViewSymbol);
        frameLayout.addView(textViewCurrent);
        frameLayout.addView(textViewChange);
        frameLayout.addView(textViewName);
        frameLayout.addView(textViewShare);
        frameLayout.addView(textViewValue);
        containerPortfolio.addView(frameLayout);


        getDescription(stockQuote);

    }

    private void getDescription(@NonNull StockQuote stockQuote) {


        /// GET PROFILE  //  Name

        String url = "https://finnhub.io/api/v1/stock/profile2?symbol=" +
                stockQuote.symbol +
                "&token=cmo6he1r01qj3mal97u0cmo6he1r01qj3mal97ug";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    textView_StockName = findViewById(myTextBoxIds.get("name" + stockQuote.symbol));
                    stockQuote.name = response.getString("name");
                    textView_StockName.setText(stockQuote.name);
                    stockQuote.imageURL = response.getString("logo");

                } catch (JSONException e) {
//                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(this).add(request);

    }

    private void updateScreen2(StockQuote stockQuote) {

//        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
//        NumberFormat numberFormat1 = NumberFormat.getNumberInstance();
//
//        FrameLayout frameLayout = new FrameLayout(this);
//        frameLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
//        frameLayout.setBackground(getResources().getDrawable(box_with_corner));
//
//
//
//
//
//
//
//        TextView textViewSymbol = new TextView(this);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        params.setMargins( getPx(20),0,0,0);
//
//        textViewSymbol.setLayoutParams(params);
//        textViewSymbol.setText(stockQuote.symbol);
//        Typeface typeface = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            typeface = getResources().getFont(R.font.poppins_bold);
//        }
//        textViewSymbol.setTypeface(typeface);
//        textViewSymbol.setTextSize(18);
//        textViewSymbol.setTextColor(getResources().getColor(com.google.android.material.R.color.material_dynamic_primary20));
//
//
//
//        TextView textViewName = new TextView(this);
//        LinearLayout.LayoutParams params0 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        params0.setMargins( getPx(20),getPx(30),0,0);
//
//        textViewName.setLayoutParams(params0);
//        textViewName.setText(stockQuote.name);
//        Typeface typeface0 = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            typeface0 = getResources().getFont(R.font.poppins_regular);
//        }
//        textViewName.setTypeface(typeface0);
//        textViewName.setTextColor(getResources().getColor(com.google.android.material.R.color.material_dynamic_primary20));
//
//
//
//        TextView textViewShare = new TextView(this);
//        LinearLayout.LayoutParams params5 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        params5.setMargins( getPx(180),getPx(30),0,0);
//
//        textViewShare.setLayoutParams(params5);
//        textViewShare.setText( String.valueOf(stockQuote.shares));
//        Typeface typeface5 = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            typeface5 = getResources().getFont(R.font.poppins_regular);
//        }
//        textViewShare.setTypeface(typeface5);
//        textViewShare.setTextColor(getResources().getColor(com.google.android.material.R.color.material_dynamic_primary20));
//
//
//
//        TextView textViewChange = new TextView(this);
//        LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        //params4.setMargins( 0,getPx(20),getPx(20),0);
//        params4.rightMargin = getPx(20);
//        params4.topMargin = getPx(30);
//        params4.leftMargin = getPx(280);
//
//
//        params4.gravity = Gravity.RIGHT;
//        textViewChange.setLayoutParams(params4);
//
//        textViewChange.setText(String.valueOf( numberFormat1.format(stockQuote.percentChange))+"%");
//        Typeface typeface4 = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            typeface4 = getResources().getFont(R.font.poppins_regular);
//        }
//        textViewChange.setTypeface(typeface4);
//        //textViewChange.setTextSize(14);
//        textViewChange.setTextColor(getResources().getColor(com.google.android.material.R.color.material_dynamic_primary20));
//        if(stockQuote.percentChange<0){
//            textViewChange.setTextColor(Color.RED);
//        }else if(stockQuote.percentChange>0){
//            textViewChange.setTextColor(Color.argb(255,0,128,0));
//        }
//
//
//
//
//
//
//        TextView textViewLast = new TextView(this);
//        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        params3.setMargins( getPx(280   ),0,0,0);
//
//        textViewLast.setLayoutParams(params3);
//        textViewLast.setText(String.valueOf(numberFormat.format(stockQuote.currentPrice)));
//        Typeface typeface2 = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            typeface2 = getResources().getFont(R.font.poppins_bold);
//        }
//        textViewLast.setTypeface(typeface2);
//        textViewLast.setTextSize(18);
//        textViewLast.setTextColor(getResources().getColor(com.google.android.material.R.color.material_dynamic_primary20));
//
//
//
//
//
//
////        TextView textViewName = new TextView(this);
////        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
////                LinearLayout.LayoutParams.WRAP_CONTENT);
////        params2.setMargins( 20,20,0,0);
////
////        textViewName.setLayoutParams(params2);
////        textViewName.setText(stockQuote.name);
////        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
////            typeface = getResources().getFont(R.font.poppins_regular);
////        }
////        textViewName.setTypeface(typeface);
//
//
//        frameLayout.addView(textViewSymbol);
//        frameLayout.addView(textViewLast);
//        frameLayout.addView(textViewChange);
//        frameLayout.addView(textViewName);
//        frameLayout.addView(textViewShare);
////        frameLayout.addView(textViewName);
//        containerPortfolio.addView(frameLayout);
//
//
    }

    private void getQuote(StockQuote stockQuote) {

        /// GET PROFILE
        String url = "https://finnhub.io/api/v1/stock/profile2?symbol=" +
                stockQuote.symbol +
                "&token=cmo6he1r01qj3mal97u0cmo6he1r01qj3mal97ug";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    stockQuote.name = response.getString("name");
                    stockQuote.imageURL = response.getString("logo");
                    updateScreen1(stockQuote);
                    //                    textView_Description.setText(myStockQuote.name);

                } catch (JSONException e) {
//                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(this).add(request);


    }

    private void updateScreen1(StockQuote stockQuote) {

        /// GET QUOTE
        String url = "https://finnhub.io/api/v1/quote?symbol=" +
                stockQuote.symbol +
                "&token=cmo6he1r01qj3mal97u0cmo6he1r01qj3mal97ug";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int profitColor;

                    stockQuote.currentPrice = response.getDouble("c");
                    stockQuote.percentChange = response.getDouble("dp");
                    updateScreen2(stockQuote);

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


    public int getPx(int dp) {
        Resources r = this.getResources();

        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
    }

    private void update2s() {

        CountDownTimer timer = new CountDownTimer(100000, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {

                for (Stock stock : Controller.loggedUser.stocksInWallet) {

                    String url = "https://finnhub.io/api/v1/quote?symbol=" +
                            stock.symbol +
                            "&token=" +
                            Controller.token;

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                textView_StockCurrent = findViewById(myTextBoxIds.get("current" + stock.symbol));
                                textView_StockCurrent.setText(numberFormatCurrency.format(response.getDouble("c")));

                                textView_StockValue = findViewById(myTextBoxIds.get("value" + stock.symbol));
                                //textView_StockValue.setText(String.valueOf(numberFormatCurrency.format(stock.currentPrice * stockQuote.shares)));

                                //totalBalance += stockQuote.currentPrice * stockQuote.shares;
                                //textView_TotalBalance.setText(String.valueOf(numberFormatCurrency.format(totalBalance)));

                                double percentChange = response.getDouble("dp");
                                int profitColor;

                                if (percentChange > 0) {
                                    profitColor = Color.argb(255, 0, 128, 0);
                                } else if (percentChange < 0) {
                                    profitColor = Color.argb(255, 255, 0, 0);
                                } else {
                                    profitColor = Color.BLACK;
                                }


                                textView_StockDeltaPercent = findViewById(myTextBoxIds.get("change" + stock.symbol));
                                textView_StockDeltaPercent.setText(numberFormatInstance.format(percentChange) + "!!!%");
                                textView_StockDeltaPercent.setTextColor(profitColor);

                            } catch (JSONException e) {
//                                    throw new RuntimeException(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    Volley.newRequestQueue(MyApplication.getAppContext()).add(request);


                }

            }

            @Override
            public void onFinish() {

            }
        }.start();

    }


    public void goWatchList(View view) {
        Intent intent = new Intent(this, WatchListActivity.class);
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
}
