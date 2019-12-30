package com.komsi.solve;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.komsi.solve.Adapter.LeaderboardChooseAdapter;
import com.komsi.solve.Adapter.TypeGameAdapter;
import com.komsi.solve.Api.RetrofitClient;
import com.komsi.solve.Model.CategoryModel;
import com.komsi.solve.Model.MenuHomeModel;
import com.komsi.solve.Model.ResponseCategory;
import com.komsi.solve.Model.ResponseMenuHome;
import com.komsi.solve.Model.UserModel;
import com.komsi.solve.Storage.SharedPrefManager;

import java.util.ArrayList;

public class LeaderboardChoose extends AppCompatActivity {
    private ArrayList<MenuHomeModel> categories;
    private RecyclerView typeRV;
    LeaderboardChooseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard_choose);
        typeRV = findViewById(R.id.typeRV);
        typeRV.setLayoutManager(new LinearLayoutManager(LeaderboardChoose.this));

        getListCategory();

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
        Intent intent = new Intent(LeaderboardChoose.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void getListCategory() {
        UserModel user = SharedPrefManager.getInstance(this).getUser();
        String token = "Bearer " + user.getToken();

        Call<ResponseMenuHome> call = RetrofitClient.getInstance().getApi().category(token, "application/json");
        call.enqueue(new Callback<ResponseMenuHome>() {
            @Override
            public void onResponse(Call<ResponseMenuHome> call, Response<ResponseMenuHome> response) {
                ResponseMenuHome category__response = response.body();
                if (response.isSuccessful()) {
                    int size = category__response.getResult().size();
                    categories = response.body().getResult();
                    adapter = new LeaderboardChooseAdapter(LeaderboardChoose.this, categories);
                    typeRV.setAdapter(adapter);
                } else {
                    Toast.makeText(LeaderboardChoose.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseMenuHome> call, Throwable t) {
                Toast.makeText(LeaderboardChoose.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();

            }
        });
    }
}
