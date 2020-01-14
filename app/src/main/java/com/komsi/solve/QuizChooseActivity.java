package com.komsi.solve;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.komsi.solve.Adapter.TypeQuizAdapter;
import com.komsi.solve.Api.RetrofitClient;
import com.komsi.solve.Model.ResponseTypeList;
import com.komsi.solve.Model.TypeListModel;
import com.komsi.solve.Model.UserModel;
import com.komsi.solve.Storage.SharedPrefManager;

import java.util.ArrayList;

public class QuizChooseActivity extends AppCompatActivity {

    ArrayList<TypeListModel> models;
    RecyclerView RVmain;
    TypeQuizAdapter adapter;
    LinearLayout leaderBoard;
    int idType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_choose);
        RVmain = findViewById(R.id.RVmain);
        leaderBoard = findViewById(R.id.leaderBoard);
        idType = getIntent().getIntExtra("idType", 1);
        leaderBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuizChooseActivity.this, LeaderboardChoose.class);
                intent.putExtra("idType", 1);
                startActivity(intent);
            }
        });

        getListCategory();
    }

    public void getListCategory() {
        UserModel user = SharedPrefManager.getInstance(this).getUser();
        String token = "Bearer " + user.getToken();

        Call<ResponseTypeList> call = RetrofitClient.getInstance().getApi().quiz(token, "application/json", idType);
        call.enqueue(new Callback<ResponseTypeList>() {
            @Override
            public void onResponse(Call<ResponseTypeList> call, Response<ResponseTypeList> response) {
                ResponseTypeList model = response.body();
                if (response.isSuccessful()) {
                    //  int size = model.getResult().size();
                    models = response.body().getResult();
                    StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
                    adapter = new TypeQuizAdapter(models, QuizChooseActivity.this);
                    // Toast.makeText(QuizChooseActivity.this, models.size()+" ", Toast.LENGTH_SHORT).show();
                    RVmain.setLayoutManager(new LinearLayoutManager(QuizChooseActivity.this));
                    RVmain.setLayoutManager(staggeredGridLayoutManager);
                    RVmain.setAdapter(adapter);
                } else {
                    Toast.makeText(QuizChooseActivity.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseTypeList> call, Throwable t) {
                Toast.makeText(QuizChooseActivity.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
