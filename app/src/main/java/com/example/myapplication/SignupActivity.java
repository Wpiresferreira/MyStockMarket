package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {
    SharedPreferences sharedPref;
    TextView eyeNewPassword, eyeReTypeNewPassword, textView_ErrorMessage;
    EditText editTextInitialCashInput, editTextEmailUsernameInput,
            editTextNewPasswordInput, editTextRetypeNewPasswordInput;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        eyeNewPassword = findViewById(R.id.eyeNewPassword);
        eyeReTypeNewPassword = findViewById(R.id.eyeReTypeNewPassword);
        editTextNewPasswordInput = findViewById(R.id.editTextNewPasswordInput);
        editTextRetypeNewPasswordInput = findViewById(R.id.editTextRetypeNewPasswordInput);
        editTextEmailUsernameInput = findViewById(R.id.editTextEmailUsernameInput);
        editTextInitialCashInput = findViewById(R.id.editTextInitialCashInput);
        textView_ErrorMessage = findViewById(R.id.textView_ErrorMessage);
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
    public void goSignIn(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);    }

    public void doSignup(View view){
        String username = String.valueOf(editTextEmailUsernameInput.getText());
        String password = String.valueOf(editTextNewPasswordInput.getText());
        String retypePassword = String.valueOf(editTextRetypeNewPasswordInput.getText());
        String initialCashBalance = String.valueOf(editTextInitialCashInput.getText());
        sharedPref = getSharedPreferences(
                "com.example.myapplication."+username, Context.MODE_PRIVATE);

        // Check Password and retype Password
        if(!password.equals(retypePassword)){
            textView_ErrorMessage.setText("Those passwords didn’t match. Try again.");
            return;
        }

        // Check if User is already registered
        if(!sharedPref.getString("username", "").equals("")){
            textView_ErrorMessage.setText("User already registered");
            return;
        }

        if(!Controller.isValidUsername(username)){
            textView_ErrorMessage.setText("Invalid username. Use a valid email");
            return;
        }

        Customer newCustomer = new Customer();
        newCustomer.username = username.toLowerCase();
        newCustomer.password = password;
        newCustomer.customerCash = new Cash(Double.parseDouble(initialCashBalance));

        boolean signupSuccessful =  SignupLocal.signup(getApplicationContext(), newCustomer);

        if (signupSuccessful){
            sharedPref = getSharedPreferences(
                    getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("usernameRemember", newCustomer.username);
            editor.apply();

            goSignIn(view);
        }
        else{
            textView_ErrorMessage.setText(R.string.signup_failed);
        }
    }
}
