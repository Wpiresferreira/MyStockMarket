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


public class MainActivity extends AppCompatActivity {

    TextView textView_eye_password, textView_goSignup, textViewErrorMessage;
    EditText editTextUsernameInput, editTextPasswordInput;
    CheckBox checkBox_Remember_Me;
    SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
//        outState.putString("username", String.valueOf(editTextUsernameInput.getText()));
//        outState.putString("password", String.valueOf(editTextPasswordInput.getText()));
//        outState.putString("eye_password", String.valueOf(textView_eye_password.getText()));
//        outState.putString("error_message", String.valueOf(textViewErrorMessage.getText()));
//        outState.putInt("error_message_visibility", textViewErrorMessage.getVisibility());
//        outState.putBoolean("check_box_remember_me", checkBox_Remember_Me.isChecked());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
       super.onRestoreInstanceState(savedInstanceState);

//        editTextUsernameInput.setText(savedInstanceState.getString("username", ""));
        editTextPasswordInput.setText("");
        editTextPasswordInput.setText("");
//        textView_eye_password.setText(savedInstanceState.getString("eye_password", ""));
//
//        if (textView_eye_password.getText() == getApplicationContext().getResources().getString(R.string.icon_open_eye)) {
//            editTextPasswordInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//            editTextPasswordInput.setSelection(editTextPasswordInput.getText().length());
//
//        } else {
//            editTextPasswordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
//            editTextPasswordInput.setSelection(editTextPasswordInput.getText().length());
//        }
//        textViewErrorMessage.setText(savedInstanceState.getString("error_message", ""));
//        textViewErrorMessage.setVisibility(savedInstanceState.getInt("error_message_visibility", View.INVISIBLE));
//        checkBox_Remember_Me.setChecked(savedInstanceState.getBoolean("check_box_remember_me", true));

    }
}

