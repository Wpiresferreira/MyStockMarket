package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LoginLocal  extends AppCompatActivity implements LoginInterface{
    static SharedPreferences sharedPref;

    public static Customer login(Context context, String username, String password) {

        sharedPref = context.getSharedPreferences(
                "com.example.myapplication." + username, Context.MODE_PRIVATE);

        if (sharedPref.getString("username", "").equals("")) {
            return null;
        } else if (username.equalsIgnoreCase(sharedPref.getString("username", "")) &&
                password.equals(sharedPref.getString("password",""))){
            return new Customer("",
                    username,
                    "",
                    new Cash(Double.parseDouble(String.valueOf(sharedPref.getFloat("cash", 0)))),
                    new ArrayList<>(),
                    new ArrayList<>());
        }

        return null;
    }
}
