package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {
    TextView eyeNewPassword, eyeReTypeNewPassword;
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
    public void goSignIn(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);    }
}
