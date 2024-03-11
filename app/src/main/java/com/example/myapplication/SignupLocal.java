package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class SignupLocal extends AppCompatActivity implements SignupInterface{

    SharedPreferences sharedPref;
    @Override
    public boolean signup(Context context, Customer customer) {
        sharedPref = context.getSharedPreferences(
                "com.example.myapplication." + customer.username, Context.MODE_PRIVATE);



        return false;
    }
}
