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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.Dictionary;
import java.util.HashMap;

public class PortfolioActivity extends AppCompatActivity {
    TextView textView_Cash, textView_TotalBalance, textView_StockName, textView_StockCurrent, textView_StockDeltaPercent, textView_StockValue;
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
        buildTheScreen();
    }

    private void buildTheScreen() {

        try {
            textView_Cash.setText(numberFormatCurrency.format(Controller.loggedUser.customerCash.balance));
        }catch (Exception e){
            textView_Cash.setText("unavailable");
        }
        totalBalance =Controller.loggedUser.customerCash.balance;

        for (Stock stock : Controller.loggedUser.customerListStock) {
            StockQuote stockQuote = new StockQuote();
            stockQuote.symbol = stock.symbol;
            stockQuote.shares = stock.balance;

            createBoxes(stockQuote);
            updateQuotes(stockQuote);
        }
    }

    private void updateQuotes(StockQuote stockQuote) {

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
                    try {
                        stockQuote.percentChange = response.getDouble("dp");
                    }catch(Exception e){
                        stockQuote.percentChange = 0;
                    }

                    textView_StockCurrent = findViewById(myTextBoxIds.get("current"+stockQuote.symbol));
                    textView_StockCurrent.setText(String.valueOf(numberFormatCurrency.format(stockQuote.currentPrice)));

                    textView_StockValue = findViewById(myTextBoxIds.get("value"+stockQuote.symbol));
                    textView_StockValue.setText(String.valueOf(numberFormatCurrency.format(stockQuote.currentPrice * stockQuote.shares)));

                    totalBalance += stockQuote.currentPrice * stockQuote.shares;
                    textView_TotalBalance.setText(String.valueOf(numberFormatCurrency.format(totalBalance)));

                    textView_StockDeltaPercent = findViewById((myTextBoxIds.get("change"+stockQuote.symbol)));
                    textView_StockDeltaPercent.setText(String.valueOf(numberFormatInstance.format(stockQuote.percentChange))+"%");

                    if(stockQuote.percentChange>0){
                        profitColor = Color.argb(255,0,128,0);
                    }else if (stockQuote.percentChange <0){
                        profitColor = Color.argb(255,255,0,0);
                    }else{
                        profitColor = Color.BLACK;
                    }

                    textView_StockDeltaPercent.setTextColor(profitColor);

                    //stockQuote.imageURL = response.getString("logo");

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

    private void createBoxes(StockQuote stockQuote) {

        // Create a frame that will be the container of TexViews
        FrameLayout frameLayout = new FrameLayout(this);
        LinearLayout.LayoutParams paramsFrame = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        paramsFrame.setMargins(0,getPx(4),0,0);

        frameLayout.setLayoutParams(paramsFrame);
        frameLayout.setBackground(getResources().getDrawable(box_with_corner));


        // Create TextView for Symbol and fill it
        TextView textViewSymbol = new TextView(this);
        LinearLayout.LayoutParams paramsSymbol = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsSymbol.setMargins( getPx(20),0,0,0);
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
        params0.setMargins( getPx(20),getPx(26),0,0);

        textViewName.setLayoutParams(params0);
        textViewName.setText(stockQuote.name);
        Typeface typeface0 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            typeface0 = getResources().getFont(R.font.poppins_regular);
        }
        textViewName.setTypeface(typeface0);
        textViewName.setTextColor(getResources().getColor(com.google.android.material.R.color.material_dynamic_primary20));
        actualId = View.generateViewId();
        myTextBoxIds.put("name"+ stockQuote.symbol, actualId);
        textViewName.setId(actualId);




        TextView textViewShare = new TextView(this);
        LinearLayout.LayoutParams params5 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params5.setMargins( getPx(170),getPx(26),0,0);

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
        params4.topMargin = getPx(26);
        params4.leftMargin = getPx(280);


        params4.gravity = Gravity.RIGHT;
        textViewChange.setLayoutParams(params4);

        textViewChange.setText(String.valueOf( numberFormatInstance.format(stockQuote.percentChange))+"%");
        actualId = View.generateViewId();
        myTextBoxIds.put("change"+ stockQuote.symbol, actualId);
        textViewChange.setId(actualId);


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


        TextView textViewCurrent = new TextView(this);
        LinearLayout.LayoutParams paramsCurrent = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsCurrent.setMargins( getPx(280   ),0,0,0);

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
        myTextBoxIds.put("current"+ stockQuote.symbol, actualId);
        textViewCurrent.setId(actualId);


        TextView textViewValue = new TextView(this);
        LinearLayout.LayoutParams paramsValue = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsValue.setMargins( getPx(170   ),0,0,0);

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
        myTextBoxIds.put("value"+ stockQuote.symbol, actualId);
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
                    textView_StockName = findViewById(myTextBoxIds.get("name"+stockQuote.symbol));
                    stockQuote.name =  response.getString("name");
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
        Volley.newRequestQueue( this).add(request);

    }

    private void updateScreen2(StockQuote stockQuote){

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
