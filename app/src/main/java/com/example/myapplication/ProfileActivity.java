package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        if(getActionBar() !=null) getActionBar().hide();

    }

    public void goWatchList(View view) {
        Intent intent = new Intent(this, WatchListActivity.class);
        startActivity(intent);
    }

    public void goTransactions(View view) {
        Intent intent = new Intent(this, TransactionActivity.class);
        startActivity(intent);
    }

    public void goPortfolio(View view) {
        Intent intent = new Intent(this, PortfolioActivity.class);
        startActivity(intent);
    }
}
