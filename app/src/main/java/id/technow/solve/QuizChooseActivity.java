package id.technow.solve;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import id.technow.solve.Adapter.TypeGameAdapter;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import id.technow.solve.Adapter.TypeQuizAdapter;

import id.technow.solve.R;

import id.technow.solve.Storage.SharedPrefManager;

import java.util.ArrayList;

public class QuizChooseActivity extends AppCompatActivity {

    ArrayList<TypeListModel> models;
    RecyclerView RVmain;
    TypeQuizAdapter adapter;
    LinearLayout leaderBoard;
    int idType;
    TextView typeGame;
    ImageView imgGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_choose);
        RVmain = findViewById(R.id.RVmain);
        leaderBoard = findViewById(R.id.leaderBoard);
        typeGame = findViewById(R.id.typeGame);
        imgGame = findViewById(R.id.imgGame);
        idType = getIntent().getIntExtra("idType", 1);
        leaderBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuizChooseActivity.this, LeaderboardChoose.class);
                intent.putExtra("idType", idType);
                startActivity(intent);
            }
        });
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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
                    models = response.body().getResult();
                    if(models == null){
                        Toast.makeText(QuizChooseActivity.this, "Soal Tidak Tersedia", Toast.LENGTH_SHORT).show();
                        typeGame.setText("Soal Tidak Tersedia");
                        imgGame.setVisibility(View.GONE);
                        leaderBoard.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                               /* Intent intent = new Intent(QuizChooseActivity.this, LeaderboardChoose.class);
                                intent.putExtra("idType", idType);
                                startActivity(intent);*/
                            }
                        });
                    }else{
                        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
                        adapter = new TypeQuizAdapter(models, QuizChooseActivity.this);
                        RVmain.setLayoutManager(new LinearLayoutManager(QuizChooseActivity.this));
                        RVmain.setLayoutManager(staggeredGridLayoutManager);
                        RVmain.setAdapter(adapter);
                    }

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
