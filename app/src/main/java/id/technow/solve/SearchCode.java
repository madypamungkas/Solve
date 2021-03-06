package id.technow.solve;

import androidx.appcompat.app.AppCompatActivity;
import id.technow.solve.Api.RetrofitClient;
import id.technow.solve.Model.ResponseTypeList;
import id.technow.solve.Model.UserModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import id.technow.solve.R;

import id.technow.solve.Storage.SharedPrefManager;

import org.json.JSONObject;

public class SearchCode extends AppCompatActivity {
    TextInputLayout ilCode;
    TextInputEditText etCode;
    String code;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_code);
        ilCode = findViewById(R.id.ilCode);
        etCode = findViewById(R.id.etCode);

    }

    public void search(View view) {
        search();
    }

    public void search() {
        loading = ProgressDialog.show(SearchCode.this, null, getString(R.string.please_wait), true, false);
        code = etCode.getText().toString().trim();

        if (code.isEmpty()) {
            loading.dismiss();
            ilCode.setError("Kode Quiz Harus Diisi!");
            etCode.requestFocus();
            return;
        }
        UserModel user = SharedPrefManager.getInstance(this).getUser();
        String token = "Bearer " + user.getToken();

        Call<ResponseTypeList> call = RetrofitClient.getInstance().getApi().code(token, "application/json", code);
        call.enqueue(new Callback<ResponseTypeList>() {
            @Override
            public void onResponse(Call<ResponseTypeList> call, Response<ResponseTypeList> response) {
                ResponseTypeList model = response.body();
                loading.dismiss();
                if (response.isSuccessful()) {
                    if(response.body().getStatus()!="failed"){
                        Intent i = new Intent(SearchCode.this, QuizActivity.class);
                        i.putExtra("idCategory", response.body().getResult().get(0).getId());
                        i.putExtra("Type", response.body().getResult().get(0).getPic_url());
                        startActivity(i);
                    }
                   else{
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(SearchCode.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(SearchCode.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                } else {
              //      Toast.makeText(SearchCode.this, "error "+ response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseTypeList> call, Throwable t) {
                loading.dismiss();

            //    Toast.makeText(SearchCode.this, "error "+ t, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
