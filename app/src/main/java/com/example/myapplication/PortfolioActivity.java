package com.example.myapplication;

import static com.example.myapplication.R.drawable.box_with_corner;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class PortfolioActivity extends AppCompatActivity {
    TextView textView_Cash, textView_StockSymbol, textView_StockQuote, textView_StockChange, textView_Description;
    LinearLayout containerPortfolio;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);
        textView_Cash = findViewById(R.id.textView_Cash);
        containerPortfolio = findViewById(R.id.containerPortfolio);
        updateScreen();
    }

    private void updateScreen() {
        textView_Cash.setText(Controller.getCurrency(Controller.loggedUser.customerCash.balance));


        for(Stock stock : Controller.loggedUser.customerListStock){
        StockQuote stockQuote = new StockQuote();
        stockQuote.symbol = stock.symbol;
        stockQuote.shares = stock.balance;


        getQuote(stockQuote);

        }
    }

    private void updateScreen2(StockQuote stockQuote){

        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        NumberFormat numberFormat1 = NumberFormat.getNumberInstance();

        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        frameLayout.setBackground(getResources().getDrawable(box_with_corner));







        TextView textViewSymbol = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins( getPx(20),0,0,0);

        textViewSymbol.setLayoutParams(params);
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
        params0.setMargins( getPx(20),getPx(30),0,0);

        textViewName.setLayoutParams(params0);
        textViewName.setText(stockQuote.name);
        Typeface typeface0 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            typeface0 = getResources().getFont(R.font.poppins_regular);
        }
        textViewName.setTypeface(typeface0);
        textViewName.setTextColor(getResources().getColor(com.google.android.material.R.color.material_dynamic_primary20));



        TextView textViewShare = new TextView(this);
        LinearLayout.LayoutParams params5 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params5.setMargins( getPx(180),getPx(30),0,0);

        textViewShare.setLayoutParams(params5);
        textViewShare.setText( String.valueOf(stockQuote.shares));
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
        params4.topMargin = getPx(30);
        params4.leftMargin = getPx(280);


        params4.gravity = Gravity.RIGHT;
        textViewChange.setLayoutParams(params4);

        textViewChange.setText(String.valueOf( numberFormat1.format(stockQuote.percentChange))+"%");
        Typeface typeface4 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            typeface4 = getResources().getFont(R.font.poppins_regular);
        }
        textViewChange.setTypeface(typeface4);
        //textViewChange.setTextSize(14);
        textViewChange.setTextColor(getResources().getColor(com.google.android.material.R.color.material_dynamic_primary20));
        if(stockQuote.percentChange<0){
            textViewChange.setTextColor(Color.RED);
        }else if(stockQuote.percentChange>0){
            textViewChange.setTextColor(Color.argb(255,0,128,0));
        }






        TextView textViewLast = new TextView(this);
        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params3.setMargins( getPx(280   ),0,0,0);

        textViewLast.setLayoutParams(params3);
        textViewLast.setText(String.valueOf(numberFormat.format(stockQuote.currentPrice)));
        Typeface typeface2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            typeface2 = getResources().getFont(R.font.poppins_bold);
        }
        textViewLast.setTypeface(typeface2);
        textViewLast.setTextSize(18);
        textViewLast.setTextColor(getResources().getColor(com.google.android.material.R.color.material_dynamic_primary20));






//        TextView textViewName = new TextView(this);
//        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        params2.setMargins( 20,20,0,0);
//
//        textViewName.setLayoutParams(params2);
//        textViewName.setText(stockQuote.name);
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            typeface = getResources().getFont(R.font.poppins_regular);
//        }
//        textViewName.setTypeface(typeface);


        frameLayout.addView(textViewSymbol);
        frameLayout.addView(textViewLast);
        frameLayout.addView(textViewChange);
        frameLayout.addView(textViewName);
        frameLayout.addView(textViewShare);
//        frameLayout.addView(textViewName);
        containerPortfolio.addView(frameLayout);


    }
    private void getQuote(StockQuote stockQuote){

        /// GET PROFILE
        String url = "https://finnhub.io/api/v1/stock/profile2?symbol=" +
                stockQuote.symbol +
                "&token=cmo6he1r01qj3mal97u0cmo6he1r01qj3mal97ug";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    stockQuote.name =  response.getString("name");
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
        Volley.newRequestQueue( this).add(request);



    }

    private void updateScreen1(StockQuote stockQuote) {

        /// GET QUOTE
        String url = "https://finnhub.io/api/v1/quote?symbol=" +
                stockQuote.symbol+
                "&token=cmo6he1r01qj3mal97u0cmo6he1r01qj3mal97ug";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int profitColor;

                    stockQuote.currentPrice =  response.getDouble("c");
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
        Volley.newRequestQueue( this).add(request);
    }


    public int getPx (int dp){
        Resources r = this.getResources();

        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
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
