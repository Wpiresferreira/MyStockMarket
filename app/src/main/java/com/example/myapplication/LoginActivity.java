package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {

    //This Activity is the first screen when the app is accessed

    TextView textView_eye_password, textView_goSignup, textViewErrorMessage;
    EditText editTextUsernameInput, editTextPasswordInput;
    CheckBox checkBox_Remember_Me;
    SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Remove purple bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        if(getActionBar() !=null) getActionBar().hide();

        initialize();

    }

    private void initialize() {
        textView_eye_password = findViewById(R.id.eyePassword);
        textViewErrorMessage = findViewById(R.id.textView_ErrorMessage);
        textViewErrorMessage.setVisibility(View.INVISIBLE);
        editTextUsernameInput = findViewById(R.id.editTextUsernameInput);
        editTextPasswordInput = findViewById(R.id.editTextPasswordInput);
        textView_goSignup = findViewById(R.id.textView_goSignup);
        checkBox_Remember_Me = findViewById(R.id.checkBox_Remember_Me);
        checkBox_Remember_Me.setChecked(true);
        sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editTextUsernameInput.setText(sharedPref.getString("usernameRemember", ""));

    }

    private void SaveRememberUser() {
        if (checkBox_Remember_Me.isChecked()) {
            String username = String.valueOf(editTextUsernameInput.getText());
            sharedPref = getSharedPreferences(
                    getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("usernameRemember", username);
            editor.apply();
        } else {
            sharedPref = getSharedPreferences(
                    getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("usernameRemember", "");
            editor.apply();

        }
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
        SaveRememberUser(); // to save the username and remember it next time if checkbox is marked

        Controller.logUser(LoginLocal.login( getApplicationContext(),
                editTextUsernameInput.getText().toString(),
                editTextPasswordInput.getText().toString()));

        if (Controller.getLoggedUser() == null) {
            editTextPasswordInput.setText("");
            textViewErrorMessage.setVisibility(View.VISIBLE);
            textViewErrorMessage.setText(getResources().getString(R.string.error_login));
        } else {
            goPortfolio(view);
        }
    }

    public void goPortfolio(View view) {
        Intent intent = new Intent(this, PortfolioActivity.class);
        startActivity(intent);
        finish();

    }


    public void goSignup(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
       super.onRestoreInstanceState(savedInstanceState);
       //to avoid the password previously typed to be exposed.
        editTextPasswordInput.setText("");

    }
}

