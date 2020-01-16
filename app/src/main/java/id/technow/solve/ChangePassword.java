package id.technow.solve;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import id.technow.solve.Api.RetrofitClient;
import id.technow.solve.Model.ResponsePassword;
import id.technow.solve.Model.UserModel;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import com.technow.solve.R;

import id.technow.solve.Storage.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

public class ChangePassword extends AppCompatActivity {
    private AppCompatEditText etOldPass, etNewPass, etNewConfirmPass;
    private MaterialButton btnConfirm;
    UserModel user;
    ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        etOldPass = findViewById(R.id.etOldPass);
        etNewPass = findViewById(R.id.etNewPass);
        etNewConfirmPass = findViewById(R.id.etNewConfirmPass);
        btnConfirm = findViewById(R.id.btnConfirm);
        user = SharedPrefManager.getInstance(this).getUser();
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gantiPass();
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

    public void gantiPass() {
        loading = ProgressDialog.show(ChangePassword.this, null, "Please wait...", true, false);

        String token = "Bearer " + user.getToken();
        String oldPass = etOldPass.getText().toString().trim();
        final String password = etNewPass.getText().toString().trim();
        String confirmPass = etNewConfirmPass.getText().toString().trim();


        retrofit2.Call<ResponsePassword> call = RetrofitClient.getInstance().getApi().updatePass(token, oldPass, password, confirmPass);
        call.enqueue(new Callback<ResponsePassword>() {
            @Override
            public void onResponse(retrofit2.Call<ResponsePassword> call, Response<ResponsePassword> response) {
                ResponsePassword passwordResponse = response.body();
                if (response.isSuccessful()) {
                    if (passwordResponse.getStatus().equals("success")) {
                        Log.i("debug", "onResponse: SUCCESS");
                        loading.dismiss();
                        Toast.makeText(ChangePassword.this, "Setting Success!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ChangePassword.this, Main2Activity.class));
                    }
                    else{
                        loading.dismiss();
                        Toast.makeText(ChangePassword.this, response.body().getMessage()+"", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(ChangePassword.this, errors.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(ChangePassword.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponsePassword> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                loading.dismiss();
                Toast.makeText(ChangePassword.this, R.string.something_wrong, Toast.LENGTH_LONG).show();
            }
        });

    }


}
