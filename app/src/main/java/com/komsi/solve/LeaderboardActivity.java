package com.komsi.solve;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.komsi.solve.Adapter.LeaderboardAdapter;
import com.komsi.solve.Api.RetrofitClient;
import com.komsi.solve.Model.LeaderboarModel;
import com.komsi.solve.Model.ResponseLeaderboard;
import com.komsi.solve.Model.UserModel;
import com.komsi.solve.Storage.SharedPrefManager;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {
    private RecyclerView leaders;
    private LeaderboardAdapter adapter;
    private List<LeaderboarModel> leaModel;
    private List<LeaderboarModel> leaModelPodium;
    int idQuiz;
    CircleImageView avatar3, avatar2, avatar1;
    UserModel user = SharedPrefManager.getInstance(this).getUser();
    ProgressDialog loading;
    TextView name1, name2, name3, score1, score2, score3, gameName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        leaders = findViewById(R.id.leaders);
        leaders.setLayoutManager(new LinearLayoutManager(this));
        name1 = findViewById(R.id.name1);
        name2 = findViewById(R.id.name2);
        name3 = findViewById(R.id.name3);
        score1 = findViewById(R.id.score1);
        score2 = findViewById(R.id.score2);
        score3 = findViewById(R.id.score3);
        gameName = findViewById(R.id.gameName);
        gameName.setText(getIntent().getStringExtra("namaSoal"));
        idQuiz = getIntent().getIntExtra("idsoal", 1);


        //loadData1();
        loadData();

        avatar1 = findViewById(R.id.avatar1);
        avatar2 = findViewById(R.id.avatar2);
        avatar3 = findViewById(R.id.avatar3);
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    public void loadData() {
        loading = ProgressDialog.show(LeaderboardActivity.this, null, "Loading...", true, false);
        String token = "Bearer " + user.getToken();
        Call<ResponseLeaderboard> call = RetrofitClient.getInstance().getApi().getnonPodium(token, "application/json", idQuiz);
        call.enqueue(new Callback<ResponseLeaderboard>() {
            @Override
            public void onResponse(Call<ResponseLeaderboard> call, Response<ResponseLeaderboard> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    leaModel = response.body().getResult();
                    adapter = new LeaderboardAdapter(LeaderboardActivity.this, leaModel);
                    leaders.setAdapter(adapter);
                } else {
                    Toast.makeText(LeaderboardActivity.this, response.code() + "", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseLeaderboard> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(LeaderboardActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
        //String token = "Bearer " + user.getToken();
        Call<ResponseLeaderboard> call2 = RetrofitClient.getInstance().getApi().getPodium(token, "application/json", idQuiz);
        call2.enqueue(new Callback<ResponseLeaderboard>() {
            @Override
            public void onResponse(Call<ResponseLeaderboard> call2, Response<ResponseLeaderboard> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getResult().size() == 0) {

                    } else if (response.body().getResult().size() == 1) {
                        leaModel = response.body().getResult();
                        leaModelPodium = response.body().getResult();
                        LeaderboarModel model1, model2, model3;
                        model1 = leaModelPodium.get(0);
                        name1.setText(model1.getUsername());
                        score1.setText(model1.getTotal_score());
                        String linkdefault = "https://solve.technow.id/storage/";
                        String link1 = linkdefault + "user/" + model1.getPicture();
                        Picasso.get().load(link1).error(R.drawable.ic_userprofile)
                                .into(avatar1);
                    } else if (response.body().getResult().size() == 2) {
                        leaModel = response.body().getResult();
                        leaModelPodium = response.body().getResult();
                        LeaderboarModel model1, model2, model3;
                        model1 = leaModelPodium.get(0);
                        model2 = leaModelPodium.get(1);
                        name1.setText(model1.getUsername());
                        name2.setText(model2.getUsername());
                        score1.setText(model1.getTotal_score());
                        score2.setText(model2.getTotal_score());
                        String linkdefault = "https://solve.technow.id/storage/";
                        String link1 = linkdefault + "user/" + model1.getPicture();
                        String link2 = linkdefault + "user/" + model2.getPicture();
                        Picasso.get().load(link1).error(R.drawable.ic_userprofile)
                                .into(avatar1);
                        Picasso.get().load(link2).error(R.drawable.ic_userprofile)
                                .into(avatar2);
                    } else {
                        leaModel = response.body().getResult();
                        leaModelPodium = response.body().getResult();
                        LeaderboarModel model1, model2, model3;
                        model1 = leaModelPodium.get(0);
                        model2 = leaModelPodium.get(1);
                        model3 = leaModelPodium.get(2);
                        name1.setText(model1.getUsername());
                        name2.setText(model2.getUsername());
                        name3.setText(model3.getUsername());
                        score1.setText(model1.getTotal_score());
                        score2.setText(model2.getTotal_score());
                        score3.setText(model3.getTotal_score());
                        String linkdefault = "https://solve.technow.id/storage/";
                        String link1 = linkdefault + "user/" + model1.getPicture();
                        String link2 = linkdefault + "user/" + model2.getPicture();
                        String link3 = linkdefault + "user/" + model3.getPicture();
                        Picasso.get().load(link1).error(R.drawable.ic_userprofile)
                                .into(avatar1);
                        Picasso.get().load(link2).error(R.drawable.ic_userprofile)
                                .into(avatar2);
                        Picasso.get().load(link3).error(R.drawable.ic_userprofile)
                                .into(avatar3);
                    }
                } else {
                    Toast.makeText(LeaderboardActivity.this, response.code() + "", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseLeaderboard> call2, Throwable t) {
                loading.dismiss();
                Toast.makeText(LeaderboardActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
