package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;

public class LoginLocal extends AppCompatActivity implements LoginInterface{
    static SharedPreferences sharedPref;

    public static Customer login(Context context, String username, String password) {

        sharedPref = context.getSharedPreferences(
                "com.example.myapplication.users", Context.MODE_PRIVATE);

        if (sharedPref.getString(username, "").equals("")) {
            return null;
        } else{

            String loadedCustomerJSON = sharedPref.getString(username,"");
            Gson gson = new Gson();
            Customer loadedCustomerObj = gson.fromJson(loadedCustomerJSON, Customer.class);

            if(loadedCustomerObj.username.equals(username) &&
                    loadedCustomerObj.password.equals(password)){
                Controller.loggedUser = loadedCustomerObj;
                if (Controller.loggedUser.stocksInWallet == null){
                    Controller.loggedUser.stocksInWallet = new ArrayList<>();
                }
                if (Controller.loggedUser.stocksInWatchlist == null){
                    Controller.loggedUser.stocksInWatchlist = new ArrayList<>();
                }
                return Controller.loggedUser;
            }
        }

//
//
//
//
//
//        if (sharedPref.getString("username", "").equals("")) {
//            return null;
//        } else if (username.equalsIgnoreCase(sharedPref.getString("username", "")) &&
//                password.equals(sharedPref.getString("password",""))){
//            return new Customer("",
//                    username,
//                    "",
//                    new Cash(Double.parseDouble(String.valueOf(sharedPref.getFloat("cash", 0)))),
//                    new ArrayList<>(),
//                    new ArrayList<>());
//        }
//
        return null;
    }
}
