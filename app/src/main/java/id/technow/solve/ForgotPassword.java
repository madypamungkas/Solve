package id.technow.solve;

import androidx.appcompat.app.AppCompatActivity;
import id.technow.solve.Api.RetrofitClient;
import id.technow.solve.Model.ResponseForgotPassword;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import id.technow.solve.R;

import static id.technow.solve.LoginActivity.isValidEmail;

public class ForgotPassword extends AppCompatActivity {
    TextInputLayout layoutEmail;
    TextInputEditText inputEmail;
    MaterialButton btnReset;
    String email;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        inputEmail = findViewById(R.id.inputEmail);
        layoutEmail = findViewById(R.id.layoutEmail);

        btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading = ProgressDialog.show(ForgotPassword.this, null, getString(R.string.please_wait), true, false);
                resetEmail();
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

                if (email.isEmpty()) {
                    layoutEmail.setError("Email is required");
                } else if (!isValidEmail(email)) {
                    layoutEmail.setError("Enter a valid address");
                } else {
                    layoutEmail.setError(null);
                }
            }
        });
    }

    public void resetEmail() {
        email = inputEmail.getText().toString().trim();

        if (email.isEmpty()) {
            loading.dismiss();
            layoutEmail.setError("Email is required");
            inputEmail.requestFocus();
            return;
        }

        retrofit2.Call<ResponseForgotPassword> call = RetrofitClient.getInstance().getApi().resetEmail("application/json", email);
        call.enqueue(new Callback<ResponseForgotPassword>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseForgotPassword> call, Response<ResponseForgotPassword> response) {

                if (response.code() == 201) {
                    loading.dismiss();
                    Toast.makeText(ForgotPassword.this, "Success", Toast.LENGTH_LONG).show();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword.this);
                    builder.setTitle("Info");
                    builder.setMessage(response.body().getMessage().toString() + "");
                    builder.setCancelable(false);

                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(ForgotPassword.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });
                    builder.show();
                } else {
                    loading.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword.this);
                    builder.setTitle("Warning");
                    builder.setMessage("Reset Link Could Not Be Sent");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseForgotPassword> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(ForgotPassword.this, R.string.something_wrong, Toast.LENGTH_LONG).show();
            }
        });
    }
}
