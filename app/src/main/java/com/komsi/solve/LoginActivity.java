package com.komsi.solve;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.komsi.solve.Api.RetrofitClient;
import com.komsi.solve.Model.ResponseDetails;
import com.komsi.solve.Model.ResponseLogin;
import com.komsi.solve.Model.UserModel;
import com.komsi.solve.Storage.SharedPrefManager;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout ilEmail, ilPassword;
    TextInputEditText etEmail, etPassword;
    TextView tvForgotPass;
    Context mContext;
    ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


    ilEmail = findViewById(R.id.ilEmail);
    ilPassword = findViewById(R.id.ilPassword);

    etEmail = findViewById(R.id.etEmail);
    etPassword = findViewById(R.id.etPassword);


        etEmail.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String email = etEmail.getText().toString();

            if (email.isEmpty()) {
                ilEmail.setError("Email is required");
            } else if (!isValidEmail(email)) {
                ilEmail.setError("Enter a valid address");
            } else {
                ilEmail.setError(null);
            }
        }
    });

        etPassword.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String password = etPassword.getText().toString();

            if (password.isEmpty()) {
                ilPassword.setError("Password is required");
            } else if (password.length() < 8) {
                ilPassword.setError("Password should be at least 8 characters long");
            } else {
                ilPassword.setError(null);
            }
        }
    });

    TextView signUpText = findViewById(R.id.signUpLink);
        signUpText.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);
        }
    });

    TextView forgotPassText = findViewById(R.id.tvForgotPass);
        forgotPassText.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent i = new Intent(LoginActivity.this, ForgotPassword.class);
            startActivity(i);
        }
    });

    mContext = this;

    Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
             loginUser();
        }
    });

}

    public void onStart() {
        super.onStart();
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    public final static boolean isValidEmail(String target) {
        if (target == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private void loginUser() {
        loading = ProgressDialog.show(mContext, null, getString(R.string.please_wait), true, false);
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            loading.dismiss();
            ilEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            loading.dismiss();
            ilPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        Call<ResponseLogin> call = RetrofitClient
                .getInstance()
                .getApi()
                .loginUser(email, password);

        call.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                ResponseLogin responseLogin = response.body();
                //   Toast.makeText(mContext, "Login successfully" + " - " + response.code(), Toast.LENGTH_LONG).show();

                if (response.isSuccessful()) {
                    if (responseLogin.getStatus().equals("success")) {
                        Log.i("debug", "onResponse: SUCCESS");
                        loading.dismiss();
                        SharedPrefManager.getInstance(LoginActivity.this).saveUser(response.body().getUser());
                        //  Toast.makeText(mContext, "Login successfully" + " - " + loginResponse.getUser().getToken(), Toast.LENGTH_LONG).show();
                        getDetails();
                       /* Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);*/
                    } else {
                        Log.i("debug", "onResponse: FAILED");
                        loading.dismiss();
                        //   Toast.makeText(mContext, loginResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    loading.dismiss();
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(mContext, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());

                loading.dismiss();
                Toast.makeText(mContext, R.string.something_wrong, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getDetails() {
        UserModel user = SharedPrefManager.getInstance(this).getUser();
        if (user.getToken() != null) {
            String token = "Bearer " + user.getToken();

            retrofit2.Call<ResponseDetails> call = RetrofitClient.getInstance().getApi().detail("application/json", token);
            call.enqueue(new Callback<ResponseDetails>() {
                @Override
                public void onResponse(retrofit2.Call<ResponseDetails> call, Response<ResponseDetails> response) {
                    if (response.isSuccessful()) {
                        SharedPrefManager.getInstance(LoginActivity.this).saveDetail(response.body().getUser());
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, R.string.something_wrong, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<ResponseDetails> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, R.string.something_wrong, Toast.LENGTH_LONG).show();

                }
            });
        }
    }
}
