package com.example.myapplication;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;


public class Controller {

    static Customer loggedUser;
    static NumberFormat numberFormatCurrency = NumberFormat.getCurrencyInstance();
    static NumberFormat numberFormatInstance = NumberFormat.getNumberInstance();
    static String token = "cmo6he1r01qj3mal97u0cmo6he1r01qj3mal97ug";

    static List<String> companies = Arrays.asList("BTC-USD", "STNE", "MMM", "AOS", "ABT", "ABBV", "ACN", "ADBE", "AMD", "AES", "AFL",
            "A", "APD", "ABNB", "AKAM", "ALB", "ARE", "ALGN", "ALLE", "LNT", "ALL", "GOOGL", "GOOG", "MO", "AMZN",
            "AMCR", "AEE", "AAL", "AEP", "AXP", "AIG", "AMT", "AWK", "AMP", "AME", "AMGN", "APH", "ADI", "ANSS", "AON",
            "APA", "AAPL", "AMAT", "APTV", "ACGL", "ADM", "ANET", "AJG", "AIZ", "T", "ATO", "ADSK", "ADP", "AZO",
            "AVB", "AVY", "AXON", "BKR", "BALL", "BAC", "BK", "BBWI", "BAX", "BDX", "BRK.B", "BBY", "BIO", "TECH",
            "BIIB", "BLK", "BX", "BA", "BKNG", "BWA", "BXP", "BSX", "BMY", "AVGO", "BR", "BRO", "BF.B", "BLDR", "BG",
            "CDNS", "CZR", "CPT", "CPB", "COF", "CAH", "KMX", "CCL", "CARR", "CTLT", "CAT", "CBOE", "CBRE", "CDW",
            "CE", "COR", "CNC", "CNP", "CF", "CHRW", "CRL", "SCHW", "CHTR", "CVX", "CMG", "CB", "CHD", "CI", "CINF",
            "CTAS", "CSCO", "C", "CFG", "CLX", "CME", "CMS", "KO", "CTSH", "CL", "CMCSA", "CMA", "CAG", "COP", "ED",
            "STZ", "CEG", "COO", "CPRT", "GLW", "CTVA", "CSGP", "COST", "CTRA", "CCI", "CSX", "CMI", "CVS", "DHR",
            "DRI", "DVA", "DAY", "DE", "DAL", "XRAY", "DVN", "DXCM", "FANG", "DLR", "DFS", "DG", "DLTR", "D", "DPZ",
            "DOV", "DOW", "DHI", "DTE", "DUK", "DD", "EMN", "ETN", "EBAY", "ECL", "EIX", "EW", "EA", "ELV", "LLY",
            "EMR", "ENPH", "ETR", "EOG", "EPAM", "EQT", "EFX", "EQIX", "EQR", "ESS", "EL", "ETSY", "EG", "EVRG",
            "ES", "EXC", "EXPE", "EXPD", "EXR", "XOM", "FFIV", "FDS", "FICO", "FAST", "FRT", "FDX", "FIS", "FITB",
            "FSLR", "FE", "FI", "FLT", "FMC", "F", "FTNT", "FTV", "FOXA", "FOX", "BEN", "FCX", "GRMN", "IT", "GEHC",
            "GEN", "GNRC", "GD", "GE", "GIS", "GM", "GPC", "GILD", "GPN", "GL", "GS", "HAL", "HIG", "HAS", "HCA",
            "PEAK", "HSIC", "HSY", "HES", "HPE", "HLT", "HOLX", "HD", "HON", "HRL", "HST", "HWM", "HPQ", "HUBB",
            "HUM", "HBAN", "HII", "IBM", "IEX", "IDXX", "ITW", "ILMN", "INCY", "IR", "PODD", "INTC", "ICE", "IFF",
            "IP", "IPG", "INTU", "ISRG", "IVZ", "INVH", "IQV", "IRM", "JBHT", "JBL", "JKHY", "J", "JNJ", "JCI", "JPM",
            "JNPR", "K", "KVUE", "KDP", "KEY", "KEYS", "KMB", "KIM", "KMI", "KLAC", "KHC", "KR", "LHX", "LH", "LRCX",
            "LW", "LVS", "LDOS", "LEN", "LIN", "LYV", "LKQ", "LMT", "L", "LOW", "LULU", "LYB", "MTB", "MRO", "MPC",
            "MKTX", "MAR", "MMC", "MLM", "MAS", "MA", "MTCH", "MKC", "MCD", "MCK", "MDT", "MRK", "META", "MET", "MTD",
            "MGM", "MCHP", "MU", "MSFT", "MAA", "MRNA", "MHK", "MOH", "TAP", "MDLZ", "MPWR", "MNST", "MCO", "MS", "MOS",
            "MSI", "MSCI", "NDAQ", "NTAP", "NFLX", "NEM", "NWSA", "NWS", "NEE", "NKE", "NI", "NDSN", "NSC", "NTRS",
            "NOC", "NCLH", "NRG", "NUE", "NVDA", "NVR", "NXPI", "ORLY", "OXY", "ODFL", "OMC", "ON", "OKE", "ORCL",
            "OTIS", "PCAR", "PKG", "PANW", "PARA", "PH", "PAYX", "PAYC", "PYPL", "PNR", "PEP", "PFE", "PCG", "PM", "PSX",
            "PNW", "PXD", "PNC", "POOL", "PPG", "PPL", "PFG", "PG", "PGR", "PLD", "PRU", "PEG", "PTC", "PSA", "PHM",
            "QRVO", "PWR", "QCOM", "DGX", "RL", "RJF", "RTX", "O", "REG", "REGN", "RF", "RSG", "RMD", "RVTY", "RHI",
            "ROK", "ROL", "ROP", "ROST", "RCL", "SPGI", "CRM", "SBAC", "SLB", "STX", "SRE", "NOW", "SHW", "SPG",
            "SWKS", "SJM", "SNA", "SO", "LUV", "SWK", "SBUX", "STT", "STLD", "STE", "SYK", "SYF", "SNPS", "SYY", "TMUS",
            "TROW", "TTWO", "TPR", "TRGP", "TGT", "TEL", "TDY", "TFX", "TER", "TSLA", "TXN", "TXT", "TMO", "TJX", "TSCO",
            "TT", "TDG", "TRV", "TRMB", "TFC", "TYL", "TSN", "USB", "UBER", "UDR", "ULTA", "UNP", "UAL", "UPS", "URI",
            "UNH", "UHS", "VLO", "VTR", "VLTO", "VRSN", "VRSK", "VZ", "VRTX", "VFC", "VTRS", "VICI", "V", "VMC", "WRB",
            "WAB", "WBA", "WMT", "DIS", "WBD", "WM", "WAT", "WEC", "WFC", "WELL", "WST", "WDC", "WRK", "WY", "WHR",
            "WMB", "WTW", "GWW", "WYNN", "XEL", "XYL", "YUM", "ZBRA", "ZBH", "ZION", "ZTS");


    private Controller() {

    }

    public static Customer getLoggedUser() {
        return loggedUser;
    }

    public static String getCurrency(Double value) {
        NumberFormat result = NumberFormat.getCurrencyInstance();
        return String.valueOf(result.format(value));
    }

    public static void getQuote(StockQuote stockQuote, Context context) {

        String url = "https://finnhub.io/api/v1/quote?symbol=" +
                stockQuote.symbol +
                "&token=cmo6he1r01qj3mal97u0cmo6he1r01qj3mal97ug";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    stockQuote.currentPrice = (double) response.getDouble("c");
                    stockQuote.change = (double) response.getDouble("d");
                    stockQuote.percentChange = (double) response.getDouble("dp");
                    stockQuote.highPriceOfTheDay = (double) response.getDouble("h");
                    stockQuote.lowPriceOfTheDay = (double) response.getDouble("l");
                    stockQuote.openPriceOfTheDay = (double) response.getDouble("o");
                    stockQuote.previousClosePrice = (double) response.getDouble("pc");

                    TransactionActivity.textView_CurrentPrice.setText(numberFormatCurrency.format(stockQuote.currentPrice));
                    TransactionActivity.textView_Change.setText(numberFormatCurrency.format(stockQuote.change));
                    if (stockQuote.change > 0) {
                        TransactionActivity.textView_Change.setTextColor(Color.GREEN);
                    } else if (stockQuote.change < 0) {
                        TransactionActivity.textView_Change.setTextColor(Color.RED);
                    } else {
                        TransactionActivity.textView_Change.setTextColor(Color.BLACK);
                    }


                    TransactionActivity.textView_PercentChange.setText(numberFormatInstance.format(stockQuote.percentChange));
                    if (stockQuote.percentChange > 0) {
                        TransactionActivity.textView_PercentChange.setTextColor(Color.GREEN);
                    } else if (stockQuote.change < 0) {
                        TransactionActivity.textView_PercentChange.setTextColor(Color.RED);
                    } else {
                        TransactionActivity.textView_PercentChange.setTextColor(Color.BLACK);
                    }
                    TransactionActivity.textView_Low.setText(numberFormatCurrency.format(stockQuote.lowPriceOfTheDay));
                    TransactionActivity.textView_High.setText(numberFormatCurrency.format(stockQuote.highPriceOfTheDay));
                    TransactionActivity.textView_Open.setText(numberFormatCurrency.format(stockQuote.openPriceOfTheDay));
                    TransactionActivity.textView_PreviousClose.setText(numberFormatCurrency.format(stockQuote.previousClosePrice));
                    int qt = Integer.parseInt(TransactionActivity.editText_Qt.getText().toString());
                    TransactionActivity.textView_Total.setText(String.valueOf(stockQuote.currentPrice * qt));

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(MyApplication.getAppContext()).add(request);


    }


    public static void getName(StockQuote stockQuote, Context context) {

        String url = "https://finnhub.io/api/v1/stock/profile2?symbol=" +
                stockQuote.symbol +
                "&token=cmo6he1r01qj3mal97u0cmo6he1r01qj3mal97ug";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    stockQuote.name = response.getString("name");
                    stockQuote.imageURL = response.getString("logo");
                    TransactionActivity.textView_Name.setText(stockQuote.name);

                } catch (JSONException e) {
//                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(context).add(request);
    }


    public static boolean isValidUsername(String username) {

        // Check if username size is less than 8
        if (username.length() < 8) {
            return false;
        }
        // Check if username contains "@"
        if (!username.substring(2).contains("@")) {
            return false;
        }
        // Check if username contains .xx or .xxx
        if (username.charAt(username.length() - 4) != '.' && username.charAt(username.length() - 3) != '.') {
            return false;
        }
        return true;
    }

    public static void updateLoggedUser(@NonNull Context context) {

        SharedPreferences sharedPref;
        sharedPref = context.getSharedPreferences(
                "com.example.myapplication." + Controller.loggedUser.username, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("dataJSON", new Gson().toJson(Controller.loggedUser));
        editor.apply();
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
}