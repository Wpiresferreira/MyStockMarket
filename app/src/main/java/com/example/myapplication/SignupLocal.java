package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

public class SignupLocal extends AppCompatActivity implements SignupInterface {

    static SharedPreferences sharedPref;

    public static boolean signup(Context context, Customer customer) {
        sharedPref = context.getSharedPreferences(
                "com.example.myapplication.users", Context.MODE_PRIVATE);

        String customerJSON = new Gson().toJson(customer);
        Toast.makeText(context, customerJSON, Toast.LENGTH_LONG).show();

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
