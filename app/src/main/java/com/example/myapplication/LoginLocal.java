package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;

public class LoginLocal extends AppCompatActivity implements LoginInterface{

    //This class is responsible to make a login using a local file to check Username/Password
    static SharedPreferences sharedPref;

    public static Customer login(Context context, String username, String password) {

        sharedPref = context.getSharedPreferences(
                "com.example.myapplication.users", Context.MODE_PRIVATE);

        if (sharedPref.getString(username, "").isEmpty()) {
            return null;
        } else{

            String loadedCustomerJSON = sharedPref.getString(username,"");
            Gson gson = new Gson();
            Customer loadedCustomerObj = gson.fromJson(loadedCustomerJSON, Customer.class);

            if(loadedCustomerObj.username.equals(username) &&
                    loadedCustomerObj.password.equals(password)){
                Controller.logUser(loadedCustomerObj);
                if (Controller.getLoggedUser().stocksInWallet == null){
                    Controller.getLoggedUser().stocksInWallet = new ArrayList<>();
                }
                if (Controller.getLoggedUser().stocksInWatchlist == null){
                    Controller.getLoggedUser().stocksInWatchlist = new ArrayList<>();
                }
                return Controller.getLoggedUser();
            }
        }
        return null;
    }
}
