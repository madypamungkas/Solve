package com.komsi.solve;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.komsi.solve.Adapter.TypeGameAdapter;
import com.komsi.solve.Api.RetrofitClient;
import com.komsi.solve.Model.CategoryModel;
import com.komsi.solve.Model.ResponseCategory;
import com.komsi.solve.Model.UserModel;
import com.komsi.solve.Storage.SharedPrefManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TypeChooseActivity extends AppCompatActivity {
    LinearLayout btnTebakHurufVokal, btnTebakKonsonan, leaderBoard;
    int idList, idSoal;
    private ArrayList<CategoryModel> categories;
    private RecyclerView typeRV;
    TypeGameAdapter adapter;
    int idCategory;
    Context mCtx = TypeChooseActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_choose);
        idCategory = getIntent().getIntExtra("idCategory", 1);


        load();
        typeRV = findViewById(R.id.typeRV);
        typeRV.setLayoutManager(new LinearLayoutManager(mCtx));
        TextView txtDesc = findViewById(R.id.txtDesc);
        TextView tvQuizType = findViewById(R.id.tvQuizType);
        tvQuizType.setText(getIntent().getStringExtra("Type"));
        txtDesc.setText(R.string.descHangeul);
        leaderBoard = findViewById(R.id.leaderBoard);
        leaderBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(mCtx, NoFragment.class);
                intent.putExtra("idtype", idCategory);
                startActivity(intent);*/
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(mCtx, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }
    public void load() {
        UserModel user = SharedPrefManager.getInstance(this).getUser();
        String token = "Bearer " + user.getToken();
        Call<ResponseCategory> call = RetrofitClient.getInstance().getApi().typeList(token, "application/json", idCategory);
        call.enqueue(new Callback<ResponseCategory>() {
            @Override
            public void onResponse(Call<ResponseCategory> call, Response<ResponseCategory> response) {
                ResponseCategory category__response = response.body();
                if (response.isSuccessful()) {
                    int size = category__response.getResult().size();
                    categories = response.body().getResult();
                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mCtx);
                    SharedPreferences.Editor editorList = sharedPrefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(categories);
                    editorList.putString("Hangeul", json);
                    editorList.commit();
                    adapter = new TypeGameAdapter(mCtx, categories);
                    typeRV.setAdapter(adapter);
                } else {
                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mCtx);
                    Gson gson = new Gson();
                    String json = sharedPrefs.getString("Hangeul", "hangeul");
                    Type type = new TypeToken<List<CategoryModel>>() {
                    }.getType();
                    ArrayList<CategoryModel> categories = gson.fromJson(json, type);
                    if (categories.size() != 0) {
                        adapter = new TypeGameAdapter(mCtx, categories);  typeRV.setAdapter(adapter);
                    } else {
                        Toast.makeText(mCtx, R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseCategory> call, Throwable t) {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mCtx);
                Gson gson = new Gson();
                String json = sharedPrefs.getString("Hangeul", "hangeul");
                Type type = new TypeToken<List<CategoryModel>>() {
                }.getType();
                ArrayList<CategoryModel> categories = gson.fromJson(json, type);
                if (categories.size() != 0) {
                    adapter = new TypeGameAdapter(mCtx, categories);
                    typeRV.setAdapter(adapter);
                } else {
                    Toast.makeText(mCtx, R.string.something_wrong, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}