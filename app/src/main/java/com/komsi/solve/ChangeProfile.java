package com.komsi.solve;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.komsi.solve.Api.RetrofitClient;
import com.komsi.solve.Model.ResponseDetails;
import com.komsi.solve.Model.ResponseProfile;
import com.komsi.solve.Model.UserModel;
import com.komsi.solve.Storage.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

public class ChangeProfile extends AppCompatActivity {
    private AppCompatEditText etSName, etSEmail ;
    private AppCompatTextView etSUsername ;

    Button btnConfirm;
    UserModel user;
    ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);
        etSName = findViewById(R.id.etSName);
        etSEmail = findViewById(R.id.etSEmail);
        etSUsername = findViewById(R.id.etSUsername);
        user = SharedPrefManager.getInstance(this).getUser();
        btnConfirm = findViewById(R.id.btnConfirm);

        etSName.setText(user.getName());
        etSEmail.setText(user.getEmail());
        etSUsername.setText(user.getUsername());
        //loadData();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfil();
            }
        });

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void updateProfil() {
        loading = ProgressDialog.show(ChangeProfile.this, null, "Please wait...", true, false);

        String token = "Bearer " + user.getToken();
        String name = etSName.getText().toString().trim();
        String email = etSEmail.getText().toString().trim();
        String username = etSUsername.getText().toString().trim();
        retrofit2.Call<ResponseProfile> call = RetrofitClient.getInstance().getApi().updateProfil(token, name, email, username);
        call.enqueue(new Callback<ResponseProfile>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseProfile> call, Response<ResponseProfile> response) {
                ResponseProfile profileResponse = response.body();
                if (response.isSuccessful()) {
                    if (profileResponse.getStatus().equals("success")) {
                        Log.i("debug", "onResponse: SUCCESS");
                        loading.dismiss();
                        loadData();
                        Toast.makeText(ChangeProfile.this, "Setting Success!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ChangeProfile.this, MainActivity.class));
                    } else {
                        Toast.makeText(ChangeProfile.this, response.body().getMessage() + "", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(ChangeProfile.this, errors.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(ChangeProfile.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseProfile> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                loading.dismiss();
                Toast.makeText(ChangeProfile.this, R.string.something_wrong, Toast.LENGTH_LONG).show();
            }
        });


    }

    public void loadData() {
        String token = "Bearer " + user.getToken();
        Call<ResponseDetails> call = RetrofitClient.getInstance().getApi().detail("application/json",token);
        call.enqueue(new Callback<ResponseDetails>() {
            @Override
            public void onResponse(Call<ResponseDetails> call, Response<ResponseDetails> response) {
                if (response.isSuccessful()) {
                    //   SharedPrefManager.getInstance(ProfileActivity.this).saveUser(ResponseBody.getUser());
                    Intent intent = new Intent(ChangeProfile.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ChangeProfile.this, R.string.something_wrong, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDetails> call, Throwable t) {
                Toast.makeText(ChangeProfile.this, R.string.something_wrong, Toast.LENGTH_LONG).show();
            }
        });
    }
}