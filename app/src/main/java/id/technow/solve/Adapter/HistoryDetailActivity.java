package id.technow.solve.Adapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.technow.solve.Api.RetrofitClient;
import id.technow.solve.LeaderboardChooseType;
import id.technow.solve.Model.AnswerSaveHDModel;
import id.technow.solve.Model.CategoryModel;
import id.technow.solve.Model.ResponseCategory;
import id.technow.solve.Model.ResponseHistoryDetail;
import id.technow.solve.Model.UserModel;
import id.technow.solve.R;
import id.technow.solve.Storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class HistoryDetailActivity extends AppCompatActivity {
    private ArrayList<AnswerSaveHDModel> answer;
    private RecyclerView typeRV;
    HistoryDetailAdapter adapter;
    int idCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);
        typeRV = findViewById(R.id.typeRV);
        typeRV.setLayoutManager(new LinearLayoutManager(HistoryDetailActivity.this));
        idCategory = getIntent().getIntExtra("idCategory", 1);
        load();

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
    public void load() {
        UserModel user = SharedPrefManager.getInstance(this).getUser();
        String token = "Bearer " + user.getToken();
        Call<ResponseHistoryDetail> call = RetrofitClient.getInstance().getApi().historyDetail(token, "application/json", idCategory);
        call.enqueue(new Callback<ResponseHistoryDetail>() {
            @Override
            public void onResponse(Call<ResponseHistoryDetail> call, Response<ResponseHistoryDetail> response) {
                ResponseHistoryDetail category__response = response.body();
                if (response.isSuccessful()) {
                    int size = category__response.getResult().getAnswer_save().size();
                    answer = response.body().getResult().getAnswer_save();
                    adapter = new HistoryDetailAdapter(answer, HistoryDetailActivity.this);
                    typeRV.setAdapter(adapter);
                } else {
                    Toast.makeText(HistoryDetailActivity.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseHistoryDetail> call, Throwable t) {

                Toast.makeText(HistoryDetailActivity.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();

            }
        });

    }

}
