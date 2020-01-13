package com.komsi.solve;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.komsi.solve.Api.RetrofitClient;
import com.komsi.solve.Model.ResponseSignUp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

public class RegisterActivity extends AppCompatActivity {
    private EditText etUsername, etEmail, etName, etPassword, etConfirmPassword;
    Context mContext;
    ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etName = findViewById(R.id.etName);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        ImageButton btnClose = findViewById(R.id.buttonClose);
        btnClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //       SignUp.this.finish();
                Intent intent = new Intent(RegisterActivity.this, LoginRegisterActivity.class);
                startActivity(intent);
            }
        });

        mContext = this;

        Button btnSignUp = this.findViewById(R.id.buttonSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Please wait...", true, false);
                registerUser();
            }
        });
    }

    public void registerUser(){
        String username = etUsername.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        final String name = etName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmpassword = etConfirmPassword.getText().toString().trim();
        String accept = "application/json";

        String status = "Active";

        if (username.isEmpty()) {
            loading.dismiss();
            etUsername.setError("Username is required");
            etUsername.requestFocus();
            return;
        }

        if (username.length() < 4) {
            loading.dismiss();
            etUsername.setError("Username should be at least 4 characters long");
            etUsername.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            loading.dismiss();
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }
        if (name.isEmpty()) {
            loading.dismiss();
            etName.setError("Name is required");
            etName.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loading.dismiss();
            etEmail.setError("Enter a valid Email");
            etEmail.requestFocus();
            return;
        }

//        if (phone.isEmpty()) {
//            loading.dismiss();
//            etPhone.setError("Phone Number is required");
//            etPhone.requestFocus();
//            return;
//        }
//
//        if (phone.length() < 10 || phone.length() > 13) {
//            loading.dismiss();
//            etPhone.setError("Phone Number should be at least 10-13 characters long");
//            etPhone.requestFocus();
//            return;
//        }

        if (password.isEmpty()) {
            loading.dismiss();
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 8) {
            loading.dismiss();
            etPassword.setError("Password should be at least 8 characters long");
            etPassword.requestFocus();
            return;
        }

        if (confirmpassword.isEmpty()) {
            loading.dismiss();
            etConfirmPassword.setError("Confirm Password is required");
            etConfirmPassword.requestFocus();
            return;
        }

        if (confirmpassword.length() < 8) {
            loading.dismiss();
            etConfirmPassword.setError("Password should be at least 8 characters long");
            etConfirmPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmpassword)) {
            loading.dismiss();
            etConfirmPassword.setError("Password not matching");
            etConfirmPassword.requestFocus();
            return;
        }
        Call<ResponseSignUp> call = RetrofitClient
                .getInstance()
                .getApi()
                .registerUser(accept, name, email, username, password, password);

        call.enqueue(new Callback<ResponseSignUp>() {
            @Override
            public void onResponse(Call<ResponseSignUp> call, Response<ResponseSignUp> response) {
                ResponseSignUp signUpResponse = response.body();
                if (response.isSuccessful()){
                    if (signUpResponse.getStatus().equals("success")) {
                        Log.i("debug", "onResponse: SUCCESS");
                        loading.dismiss();
                        Toast.makeText(mContext, "Registration Success!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(mContext, LoginRegisterActivity.class));
                    }
                } else {
                    loading.dismiss();
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorBody.trim());
                        jsonObject = jsonObject.getJSONObject("errors");
                        Iterator<String> keys = jsonObject.keys();
                        StringBuilder errors = new StringBuilder();
                        String separator = "";
                        while (keys.hasNext()) {
                            String key = keys.next();
                            JSONArray arr = jsonObject.getJSONArray(key);
                            for (int i = 0; i < arr.length(); i++) {
                                errors.append(separator).append(key).append(" : ").append(arr.getString(i));
                                separator = "\n";
                            }
                        }
                        Toast.makeText(mContext, errors.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseSignUp> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                loading.dismiss();
                Toast.makeText(mContext, R.string.something_wrong, Toast.LENGTH_LONG).show();
            }
        });
    }

}