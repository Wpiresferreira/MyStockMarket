package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

public class SignupLocal extends AppCompatActivity implements SignupInterface {

    //This class is responsible to create a new account, and save information locally.

    static SharedPreferences sharedPref;

    public static boolean signup(Context context, Customer customer) {
        sharedPref = context.getSharedPreferences(
                "com.example.myapplication.users", Context.MODE_PRIVATE);

        String customerJSON = new Gson().toJson(customer);

        try {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(customer.username, customerJSON);
            editor.apply();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
