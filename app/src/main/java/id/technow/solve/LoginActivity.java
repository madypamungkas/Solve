package id.technow.solve;

import androidx.appcompat.app.AppCompatActivity;
import id.technow.solve.Api.RetrofitClient;
import id.technow.solve.Model.ResponseDetails;
import id.technow.solve.Model.UserModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import id.technow.solve.Model.ResponseLogin;

import id.technow.solve.R;

import id.technow.solve.Storage.SharedPrefManager;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout layoutEmail, layoutPass;
    TextInputEditText inputEmail, inputPass;
    TextView btnForgotPass, signUpLink;
    Context mContext;
    ProgressDialog loading;
    private static final String TAG = "Login";
    MaterialButton btnLogin;
    Context mCtx = LoginActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        layoutEmail = findViewById(R.id.layoutEmail);
        layoutPass = findViewById(R.id.layoutPass);
        inputEmail = findViewById(R.id.inputEmail);
        inputPass = findViewById(R.id.inputPass);
        btnForgotPass = findViewById(R.id.btnForgotPass);
        signUpLink = findViewById(R.id.signUpLink);

        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getActivity(), Main2Activity.class));
                loginUser();
            }
        });

        inputEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String email = inputEmail.getText().toString();

                if (email.length() > 0) {
                    layoutEmail.setError(null);
                }
            }
        });

        inputPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String pass = inputPass.getText().toString();

                if (pass.length() > 0) {
                    layoutPass.setError(null);
                }
            }
        });


    }

    public void onStart() {
        super.onStart();
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(this, Main2Activity.class);
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
        loading = ProgressDialog.show(LoginActivity.this, null, getString(R.string.please_wait), true, false);
        String email = inputEmail.getText().toString().trim();
        String password = inputPass.getText().toString().trim();

        if (email.isEmpty()) {
            loading.dismiss();
            layoutEmail.setError("Email is required");
            inputEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loading.dismiss();
            layoutEmail.setError("Enter a valid Email");
            inputEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            loading.dismiss();
            layoutPass.setError("Password is required");
            inputPass.requestFocus();
            return;
        }

        if (password.length() < 8) {
            loading.dismiss();
            layoutPass.setError("Password should be at least 8 characters long");
            inputPass.requestFocus();
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
                if (response.isSuccessful()) {
                    if (responseLogin.getStatus().equals("success")) {
                        Log.i("debug", "onResponse: SUCCESS");
                        loading.dismiss();
                        SharedPrefManager.getInstance(LoginActivity.this).saveUser(response.body().getUser());
                        //  Toast.makeText(LoginActivity.this, "Login successfully" + " - " + loginResponse.getUser().getToken(), Toast.LENGTH_LONG).show();
                        getDetails();
                       /* Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);*/
                    } else {
                        Log.i("debug", "onResponse: FAILED");
                        loading.dismiss();
                        //   Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    loading.dismiss();
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(LoginActivity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                loading.dismiss();
                Toast.makeText(LoginActivity.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
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
                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                        SharedPreferences.Editor editorList = sharedPrefs.edit();
                        Gson gson = new Gson();

                        String detailUser = gson.toJson(response.body());

                        editorList.putString("DetailUser", detailUser);
                        editorList.commit();
                        SharedPrefManager.getInstance(LoginActivity.this).saveAnswerChance(3);

                        SharedPrefManager.getInstance(LoginActivity.this).saveDetail(response.body().getUser());
                        Intent intent = new Intent(LoginActivity.this, Main2Activity.class);
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
