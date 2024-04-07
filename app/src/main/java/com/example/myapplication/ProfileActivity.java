package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class ProfileActivity extends AppCompatActivity {
    TextView textViewEmailUsername;
    TextView eyeNewPassword, eyeReTypeNewPassword, textView_ErrorMessage;
    EditText editTextNewPasswordInput, editTextRetypeNewPasswordInput, editDepositWithdrawCash;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        if (getActionBar() != null) getActionBar().hide();


        textViewEmailUsername = findViewById(R.id.textViewEmailUsername);
        textViewEmailUsername.setText(Controller.getLoggedUser().username);
        eyeNewPassword = findViewById(R.id.eyeNewPassword);
        eyeReTypeNewPassword = findViewById(R.id.eyeReTypeNewPassword);
        editTextNewPasswordInput = findViewById(R.id.editTextNewPasswordInput);
        editTextRetypeNewPasswordInput = findViewById(R.id.editTextRetypeNewPasswordInput);
        textView_ErrorMessage = findViewById(R.id.textView_ErrorMessage);
        editDepositWithdrawCash = findViewById(R.id.editDepositWithdrawCash);
        editDepositWithdrawCash.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                try {
                    String numbersFormatted = String
                            .valueOf(editDepositWithdrawCash.getText())
                            .replace(",", "")
                            .replace("$", "")
                            .replace(" ", "");
                    double numberDouble = Double.parseDouble(numbersFormatted);
                    numbersFormatted = new DecimalFormat("$ #,##0.00").format(numberDouble);
                    if (!numbersFormatted.equals(editDepositWithdrawCash.getText().toString())) {
                        editDepositWithdrawCash.setText(numbersFormatted);
                    }
                } catch (Exception e) {
                    Log.wtf("Format number", "afterTextChanged: " + e);
                }
            }
        });

    }

    public void logout(View view) {
        Controller.disconnectLoggedUser();
        Intent intent = new Intent(this, LoginActivity.class);
        finish();
        startActivity(intent);
    }

    public void changePassword(View view) {

        String password = String.valueOf(editTextNewPasswordInput.getText());
        String retypePassword = String.valueOf(editTextRetypeNewPasswordInput.getText());

        // Check Password and retype Password
        if(!password.equals(retypePassword)){
            textView_ErrorMessage.setText(R.string.password_did_not_match_try_again);
        }else{
            Controller.updatePassword(this, password);
            textView_ErrorMessage.setText("");
            Toast.makeText(this, "Password changed", Toast.LENGTH_SHORT).show();
            editTextNewPasswordInput.setText("");
            editTextRetypeNewPasswordInput.setText("");
        }
    }


    public void toggleVisibilityReTypeNewPassword(View view) {

        if (eyeReTypeNewPassword.getText() == getApplicationContext().getResources().getString(R.string.icon_open_eye)) {
            eyeReTypeNewPassword.setText(getApplicationContext().getResources().getString(R.string.icon_closed_eye));
            editTextRetypeNewPasswordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
            editTextRetypeNewPasswordInput.setSelection(editTextRetypeNewPasswordInput.getText().length());
        } else {
            eyeReTypeNewPassword.setText(getApplicationContext().getResources().getString(R.string.icon_open_eye));
            editTextRetypeNewPasswordInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            editTextRetypeNewPasswordInput.setSelection(editTextRetypeNewPasswordInput.getText().length());
        }
    }

    public void toggleVisibilityTypeNewPassword(View view) {

        if (eyeNewPassword.getText() == getApplicationContext().getResources().getString(R.string.icon_open_eye)) {
            eyeNewPassword.setText(getApplicationContext().getResources().getString(R.string.icon_closed_eye));
            editTextNewPasswordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
            editTextNewPasswordInput.setSelection(editTextNewPasswordInput.getText().length());
        } else {
            eyeNewPassword.setText(getApplicationContext().getResources().getString(R.string.icon_open_eye));
            editTextNewPasswordInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            editTextNewPasswordInput.setSelection(editTextNewPasswordInput.getText().length());
        }
    }

    public void depositCash(View view) {
        if (!Controller.isValidCash(String.valueOf(editDepositWithdrawCash.getText()))) {
            textView_ErrorMessage.setText(R.string.invalid_value);
            editDepositWithdrawCash.requestFocus();
            return;
        }
        double cashToDeposit = Double.parseDouble(String.valueOf(editDepositWithdrawCash.getText())
                .replace("$","")
                .replace(",","")
                .replace(" ", ""));
        if (Controller.getLoggedUser().customerCash.balance + cashToDeposit > 1000000) {
            textView_ErrorMessage.setText(R.string.your_balance_cannot_be_greater_than_1_000_000_00);
            editDepositWithdrawCash.requestFocus();
            return;
        }

        Controller.depositCash(cashToDeposit);
        goPortfolio(view);


    }

    public void withdrawCash(View view){
        if(!Controller.isValidCash(String.valueOf(editDepositWithdrawCash.getText()))){
            textView_ErrorMessage.setText(R.string.invalid_value);
            editDepositWithdrawCash.requestFocus();
            return;
        }
        double cashToWithdraw = Double.parseDouble(String.valueOf(editDepositWithdrawCash.getText())
                .replace("$","")
                .replace(",","")
                .replace(" ", ""));
        if (Controller.getLoggedUser().customerCash.balance - cashToWithdraw < 0) {
            textView_ErrorMessage.setText(R.string.insuficient_balance);
            editDepositWithdrawCash.requestFocus();
            return;
        }

        Controller.withdrawCash(cashToWithdraw);
        goPortfolio(view);


    }


    public void goWatchList(View view) {
        Intent intent = new Intent(this, WatchListActivity.class);
        finish();
        startActivity(intent);
    }

    public void goTransactions(View view) {
        Intent intent = new Intent(this, TransactionActivity.class);
        finish();
        startActivity(intent);
    }

    public void goPortfolio(View view) {
        Intent intent = new Intent(this, PortfolioActivity.class);
        finish();
        startActivity(intent);
    }
}
