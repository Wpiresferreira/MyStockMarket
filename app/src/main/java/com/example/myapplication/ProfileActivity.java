package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    TextView textViewEmailUsername;
    TextView eyeNewPassword, eyeReTypeNewPassword, textView_ErrorMessage;
    EditText editTextNewPasswordInput, editTextRetypeNewPasswordInput;


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
