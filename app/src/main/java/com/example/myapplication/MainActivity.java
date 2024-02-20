package com.example.myapplication;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.text.method.HideReturnsTransformationMethod;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    TextView textView_eye_password, textView_goSignup;
    EditText editTextUsernameInput, editTextPasswordInput;
    Controller controller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView_eye_password = findViewById(R.id.eyePassword);
        editTextUsernameInput = findViewById(R.id.editTextUsernameInput);
        editTextPasswordInput = findViewById(R.id.editTextPasswordInput);
        textView_goSignup = findViewById(R.id.textView_goSignup);

        //controller = new Controller();
    }

    public void toggleVisibilityPassword(View view) {

        if (textView_eye_password.getText() == getApplicationContext().getResources().getString(R.string.icon_open_eye)) {
            textView_eye_password.setText(getApplicationContext().getResources().getString(R.string.icon_closed_eye));
            editTextPasswordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
            editTextPasswordInput.setSelection(editTextPasswordInput.getText().length());
        } else {
            textView_eye_password.setText(getApplicationContext().getResources().getString(R.string.icon_open_eye));
            editTextPasswordInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            editTextPasswordInput.setSelection(editTextPasswordInput.getText().length());
        }
    }

    public void doLogin(View view) {
        // TODO
        // This method will check Username and Password and if they match...
        // Update loggedUser and change the page.
        if(!editTextUsernameInput.getText().toString().equals("w") || !editTextPasswordInput.getText().toString().equals("w")){
            //tv_LoginError.setText("Check Username/Password");
            return;
        }


        Cash myCash = new Cash();
        myCash.balance = 1234.56;

        Stock myStock = new Stock();
        myStock.balance = 25;
        myStock.symbol = "GOOG";
        myStock.description = "Google Inc.";

        Stock myStock1 = new Stock();
        myStock1.balance = 12;
        myStock1.symbol = "MSFT";
        myStock1.description = "Microsoft Inc..";

        Stock myStock2 = new Stock();
        myStock2.balance = 1;
        myStock2.symbol = "BTC-USD";
        myStock2.description = "Bitcoin USD";

        Stock myStock4 = new Stock();
        myStock4.balance = 5;
        myStock4.symbol = "GE";

        Stock myStock3 = new Stock();
        myStock3.symbol = "AAPL";
        myStock3.description = "Apple Inc.";

        List<Stock> myCustomerListStock = new ArrayList<>();
        myCustomerListStock.add(myStock);
        myCustomerListStock.add(myStock1);
        myCustomerListStock.add(myStock2);
        myCustomerListStock.add(myStock4);

        List<Stock> myCustomerListMarket = new ArrayList<>();
        myCustomerListMarket.add(myStock);
        myCustomerListMarket.add(myStock1);
        myCustomerListMarket.add(myStock2);
        myCustomerListMarket.add(myStock3);

        Controller.loggedUser = new Customer("Wagner", "w", "w", myCash, myCustomerListStock, myCustomerListMarket);

        //loadStockSymbols();

        goPortfolio(view);
    }

    public void goPortfolio(View view) {
        Intent intent = new Intent(this, PortfolioActivity.class);
        startActivity(intent);

    }


    public void goSignup(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }




}

