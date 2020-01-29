package id.technow.solve;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import id.technow.solve.Api.RetrofitClient;
import id.technow.solve.Model.ResponseTypeList;
import id.technow.solve.Model.TypeListModel;
import id.technow.solve.Model.UserModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import id.technow.solve.Adapter.LeaderboardChooseAdapter;

import id.technow.solve.R;

import id.technow.solve.Storage.SharedPrefManager;

import java.util.ArrayList;

public class LeaderboardChoose extends AppCompatActivity {
    private ArrayList<TypeListModel> categories;
    private RecyclerView typeRV;
    LeaderboardChooseAdapter adapter;
    int idType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard_choose);
        typeRV = findViewById(R.id.typeRV);
        typeRV.setLayoutManager(new LinearLayoutManager(LeaderboardChoose.this));
        idType = getIntent().getIntExtra("idType", 2);
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
        super.onBackPressed();
    }


    public void getListCategory() {
        UserModel user = SharedPrefManager.getInstance(this).getUser();
        String token = "Bearer " + user.getToken();

        Call<ResponseTypeList> call = RetrofitClient.getInstance().getApi().quiz(token, "application/json", idType);
        call.enqueue(new Callback<ResponseTypeList>() {
            @Override
            public void onResponse(Call<ResponseTypeList> call, Response<ResponseTypeList> response) {
               if (response.isSuccessful()) {
                     categories = response.body().getResult();
                    if (categories.isEmpty()) {
                        Toast.makeText(LeaderboardChoose.this, "Bidang Tidak Tersedia", Toast.LENGTH_SHORT).show();

                    } else {
                        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
                        adapter = new LeaderboardChooseAdapter(LeaderboardChoose.this, categories);
                        typeRV.setLayoutManager(new LinearLayoutManager(LeaderboardChoose.this));
                        typeRV.setLayoutManager(staggeredGridLayoutManager);
                        typeRV.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(LeaderboardChoose.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseTypeList> call, Throwable t) {
                Toast.makeText(LeaderboardChoose.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();

            }
        });
    }
}
