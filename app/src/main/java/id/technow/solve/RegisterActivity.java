package id.technow.solve;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import id.technow.solve.Api.RetrofitClient;
import id.technow.solve.Model.ResponseSignUp;
import id.technow.solve.Model.SchoolsModel;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import id.technow.solve.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "Register";
    MaterialButton btnRegister;
    SpinnerDialog spinnerDialog;
    ArrayList<SchoolsModel> items = new ArrayList<>();
    public TextView txtSchools, loginLink;
    private EditText etUsername, etEmail, etName, etPassword, etPhone;
    LinearLayout btnSchool;
    Context mContext;
    String idSchool, schoolName;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etName = findViewById(R.id.etName);
        etPassword = findViewById(R.id.etPassword);
        etPhone = findViewById(R.id.etPhone);
        loginLink = findViewById(R.id.loginLink);
        btnSchool = findViewById(R.id.btnSchool);
        btnRegister = findViewById(R.id.btnRegister);
        txtSchools = findViewById(R.id.txtSchools);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        btnSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                SchoolSearchFragment fragment = new SchoolSearchFragment();
                fragment.setArguments(bundle);
                fragment.show(((FragmentActivity) RegisterActivity.this).getSupportFragmentManager(), TAG);
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

    public void setTextSch(String schoolName, String schoolId) {
        txtSchools.setText(schoolName);
        idSchool = schoolId;
    }

    public void register() {
        loading = ProgressDialog.show(RegisterActivity.this, null, "Please wait...", true, false);

        String username = etUsername.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        final String name = etName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        String accept = "application/json";
        String status = "Active";

        if (username.isEmpty()) {
            loading.dismiss();
            etUsername.setError("Username harus diisi");
            etUsername.requestFocus();
            return;
        }

        if (username.length() < 4) {
            loading.dismiss();
            etUsername.setError("Username minimal memiliki 4 panjang karakter");
            etUsername.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            loading.dismiss();
            etEmail.setError("Email harus diisi");
            etEmail.requestFocus();
            return;
        }
        if (name.isEmpty()) {
            loading.dismiss();
            etName.setError("Nama harus diisi");
            etName.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loading.dismiss();
            etEmail.setError("Gunakan email yang valid");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            loading.dismiss();
            etPassword.setError("Password harus diisi");
            etPassword.requestFocus();
            return;
        }

        if (idSchool.isEmpty()) {
            loading.dismiss();
            Toast.makeText(mContext, "Sekolah harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 8) {
            loading.dismiss();
            etPassword.setError("Password minimal memiliki 8 panjang karakter");
            etPassword.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            loading.dismiss();
            etPhone.setError("Nomor handphone harus diisi");
            etPhone.requestFocus();
            return;
        }

        if (phone.length() < 8 || phone.length() > 15) {
            loading.dismiss();
            etPhone.setError("Nomor handphone harus memiliki 8-15 panjang karakter");
            etPhone.requestFocus();
            return;
        }

        int phoneValid = 0;
        if (phone.substring(0, 1).equals("0")) {
            phoneValid = 1;
        } else if (phone.substring(0, 2).equals("62")) {
            phoneValid = 1;
        } else if (phone.substring(0, 3).equals("+62")) {
            phoneValid = 1;
        }

        if (phoneValid != 1) {
            loading.dismiss();
            etPhone.setError("Nomor handphone harus berasal dari Indonesia");
            etPhone.requestFocus();
            return;
        }

        Call<ResponseSignUp> call = RetrofitClient
                .getInstance()
                .getApi()
                .registerUser(accept, name+"", email+"", username+"", password, idSchool+"" , phone);

        call.enqueue(new Callback<ResponseSignUp>() {
            @Override
            public void onResponse(Call<ResponseSignUp> call, Response<ResponseSignUp> response) {
                ResponseSignUp signUpResponse = response.body();
                if (response.isSuccessful()) {
                    if (signUpResponse.getStatus().equals("success")) {
                        Log.i("debug", "onResponse: SUCCESS");
                        loading.dismiss();
                        Toast.makeText(RegisterActivity.this, "Registration Success!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

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
                        Toast.makeText(RegisterActivity.this, errors.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseSignUp> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                loading.dismiss();
                Toast.makeText(RegisterActivity.this, "Something wrong. Please try again later.", Toast.LENGTH_LONG).show();
            }
        });
    }

}