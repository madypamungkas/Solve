package id.technow.solve;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.technow.solve.Adapter.HistoryDetailAdapter;
import id.technow.solve.Api.RetrofitClient;
import id.technow.solve.Model.AnswerSaveHDModel;
import id.technow.solve.Model.QuestionModel;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import id.technow.solve.Adapter.PembahasanAdapter;

import id.technow.solve.Model.ResponseHistoryDetail;
import id.technow.solve.Model.UserModel;
import id.technow.solve.R;
import id.technow.solve.Storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PembahasanQuizActivity extends AppCompatActivity {
    private ArrayList<QuestionModel> answerModels;
    private ArrayList<AnswerSaveHDModel> answer;

    private RecyclerView typeRV;
    HistoryDetailAdapter adapter;
    int id, idHistory;
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembahasan_quiz);
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        typeRV = findViewById(R.id.typeRV);
        typeRV.setLayoutManager(new LinearLayoutManager(PembahasanQuizActivity.this));
        id = getIntent().getIntExtra("idtype", 4);
/*
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(PembahasanQuizActivity.this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("question", "question");
        Type type = new TypeToken<ArrayList<QuestionModel>>() {
        }.getType();
        ArrayList<QuestionModel> answerModels = gson.fromJson(json, type);

        if (answerModels.size() != 0) {
            adapter = new PembahasanAdapter(PembahasanQuizActivity.this, answerModels);
            typeRV.setAdapter(adapter);
        } else {
            Toast.makeText(PembahasanQuizActivity.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
        }

    }*/
        idHistory = getIntent().getIntExtra("idHistory", 0);
        type = getIntent().getStringExtra("gameName");

        load();

       /* ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });*/

    }

    public void load() {
        UserModel user = SharedPrefManager.getInstance(this).getUser();
        String token = "Bearer " + user.getToken();

        Call<ResponseHistoryDetail> call = RetrofitClient.getInstance().getApi().historyDetail("application/json", token, idHistory);
        call.enqueue(new Callback<ResponseHistoryDetail>() {
            @Override
            public void onResponse(Call<ResponseHistoryDetail> call, Response<ResponseHistoryDetail> response) {
                ResponseHistoryDetail category__response = response.body();
                if (response.isSuccessful()) {
                    int size = category__response.getQuestion().size();
                    answer = response.body().getQuestion();
                    adapter = new HistoryDetailAdapter(answer, PembahasanQuizActivity.this, type);
                    typeRV.setAdapter(adapter);
                } else {
                    Toast.makeText(PembahasanQuizActivity.this, R.string.something_wrong , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseHistoryDetail> call, Throwable t) {

                Toast.makeText(PembahasanQuizActivity.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();

            }
        });



}
}
