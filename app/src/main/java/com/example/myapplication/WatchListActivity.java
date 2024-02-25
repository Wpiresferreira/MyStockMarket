package com.example.myapplication;

import static com.example.myapplication.R.drawable.box_with_corner;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Objects;

public class WatchListActivity extends AppCompatActivity {

    NumberFormat numberFormatCurrency = NumberFormat.getCurrencyInstance();
    NumberFormat numberFormatInstance = NumberFormat.getNumberInstance();
    LinearLayout containerWatchList;
    int actualId;
    TextView textView_StockName, textView_StockCurrent, textView_StockDeltaPercent;
    EditText editText_StockToAdd;
    HashMap<String, Integer> myTextBoxIds = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchlist);
        containerWatchList = findViewById(R.id.containerWatchList);
        editText_StockToAdd = findViewById(R.id.editText_StockToAdd);
        buildScreen();
    }

    private void buildScreen() {
        for (Stock stock : Controller.loggedUser.customerStockView) {
            StockQuote stockQuote = new StockQuote();
            stockQuote.symbol = stock.symbol;

            createBoxes(stockQuote);
            updateQuotes(stockQuote);
        }
    }

    public void addStock(View view){

        String textInserted = editText_StockToAdd.getText().toString().toUpperCase();
        //Check if the stock is in the S&P500
        if (!Controller.companies.contains(textInserted)){
            Toast.makeText(this, "Symbol not found", Toast.LENGTH_SHORT).show();
            return;
        }

        //Check if the stock already exist in the list
        for(Stock stock: Controller.loggedUser.customerStockView){
            if (Objects.equals(stock.symbol, textInserted)){
                Toast.makeText(this, "Symbol is already listed", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Check the size of the list
        if(Controller.loggedUser.customerStockView.size() >=10){
            Toast.makeText(this, "Max size Watchlist is 10 stocks", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert the stock to list and create a box, and update the screen
        Stock newStock = new Stock();
        newStock.symbol = textInserted;
        Controller.loggedUser.customerStockView.add(newStock);

        StockQuote newStockQuote = new StockQuote();
        newStockQuote.symbol = textInserted;
        createBoxes(newStockQuote);
        updateQuotes(newStockQuote);


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
                    stockQuote.percentChange = response.getDouble("dp");


                    textView_StockCurrent = findViewById(myTextBoxIds.get("current"+stockQuote.symbol));
                    textView_StockCurrent.setText(String.valueOf(numberFormatCurrency.format(stockQuote.currentPrice)));


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

        Typeface typeface = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            typeface = getResources().getFont(R.font.poppins_bold);
        }
        Typeface typefaceRegular = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            typefaceRegular = getResources().getFont(R.font.poppins_regular);
        }
        Typeface typefaceAwesome = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            typefaceAwesome = getResources().getFont(R.font.fontawesome);
        }


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

        textViewSymbol.setText(stockQuote.symbol);

        textViewSymbol.setTypeface(typeface);
        textViewSymbol.setTextSize(18);
        textViewSymbol.setTextColor(getResources().getColor(com.google.android.material.R.color.material_dynamic_primary20));

        // Create TextView for Description of Stock (name)
        TextView textViewName = new TextView(this);
        LinearLayout.LayoutParams paramsName = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsName.setMargins( getPx(20),getPx(26),0,0);

        textViewName.setLayoutParams(paramsName);
        textViewName.setText(stockQuote.name);
        textViewName.setTypeface(typefaceRegular);
        textViewName.setTextColor(getResources().getColor(com.google.android.material.R.color.material_dynamic_primary20));
        actualId = View.generateViewId();
        myTextBoxIds.put("name"+ stockQuote.symbol, actualId);
        textViewName.setId(actualId);



        // Create a TextView for Change
        TextView textViewChange = new TextView(this);
        LinearLayout.LayoutParams paramsChange = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsChange.setMargins(getPx(260), getPx(13),getPx(20),0);
        paramsChange.gravity = Gravity.END;
        textViewChange.setLayoutParams(paramsChange);
        textViewChange.setText(String.valueOf( numberFormatInstance.format(stockQuote.percentChange))+"%");
        actualId = View.generateViewId();
        myTextBoxIds.put("change"+ stockQuote.symbol, actualId);
        textViewChange.setId(actualId);
        textViewChange.setTypeface(typefaceRegular);
        textViewChange.setTextColor(getResources().getColor(com.google.android.material.R.color.material_dynamic_primary20));
        if(stockQuote.percentChange<0){
            textViewChange.setTextColor(Color.RED);
        }else if(stockQuote.percentChange>0){
            textViewChange.setTextColor(Color.argb(255,0,128,0));
        }

        // Create a TextView for current price
        TextView textViewCurrent = new TextView(this);
        LinearLayout.LayoutParams paramsCurrent = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsCurrent.setMargins( getPx(150  ),13,0,0);
        textViewCurrent.setLayoutParams(paramsCurrent);
        textViewCurrent.setText(String.valueOf(numberFormatCurrency.format(stockQuote.currentPrice)));
        textViewCurrent.setTypeface(typeface);
        textViewCurrent.setTextSize(18);
        textViewCurrent.setTextColor(getResources().getColor(com.google.android.material.R.color.material_dynamic_primary20));
        actualId = View.generateViewId();
        myTextBoxIds.put("current"+ stockQuote.symbol, actualId);
        textViewCurrent.setId(actualId);

        //Create a button to remove a Stock
        TextView buttonRemove = new TextView(this);
        LinearLayout.LayoutParams paramsButton = new LinearLayout.LayoutParams(getPx(20),
                getPx(20));
        paramsButton.setMargins(getPx(340), getPx(13),getPx(20),0);
        buttonRemove.setLayoutParams(paramsButton);
        buttonRemove.setPadding(0,0,0,0);
        buttonRemove.setBackgroundColor(Color.TRANSPARENT);

        buttonRemove.setText(R.string.icon_trash);

        buttonRemove.setTextColor(Color.RED);
        buttonRemove.setTypeface(typefaceAwesome);


        actualId = View.generateViewId();
        myTextBoxIds.put("button"+ stockQuote.symbol, actualId);
        buttonRemove.setId(actualId);

        frameLayout.addView(textViewSymbol);
        frameLayout.addView(textViewCurrent);
        frameLayout.addView(textViewChange);
        frameLayout.addView(textViewName);
        frameLayout.addView(buttonRemove);
        containerWatchList.addView(frameLayout);


        getDescription(stockQuote);

    }

    private void getDescription(@NonNull StockQuote stockQuote) {


        /// GET PROFILE
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

    public int getPx (int dp){
        Resources r = this.getResources();

        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
    }
}
