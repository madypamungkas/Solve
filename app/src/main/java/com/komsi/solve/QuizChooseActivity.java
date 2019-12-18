package com.komsi.solve;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
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
    int idType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_choose);
        RVmain = findViewById(R.id.RVmain);
        getListCategory();
    }

    public void getListCategory() {
        UserModel user = SharedPrefManager.getInstance(this).getUser();
        String token = "Bearer " + user.getToken();

        Call<ResponseTypeList> call = RetrofitClient.getInstance().getApi().quiz(token, "application/json", 1);
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
