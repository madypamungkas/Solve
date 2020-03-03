package id.technow.solve;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.technow.solve.Adapter.HistoryDetailAdapter;
import id.technow.solve.Api.RetrofitClient;
import id.technow.solve.Model.AnswerSaveHDModel;
import id.technow.solve.Model.ResponseHistoryDetail;
import id.technow.solve.Model.UserModel;
import id.technow.solve.Storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;

public class HistoryDetailActivity extends AppCompatActivity {
    private ArrayList<AnswerSaveHDModel> answer;
    private RecyclerView typeRV;
    HistoryDetailAdapter adapter;
    int idHistory;
    String type;
    private com.google.android.gms.ads.AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);
        typeRV = findViewById(R.id.typeRV);
        typeRV.setLayoutManager(new LinearLayoutManager(HistoryDetailActivity.this));
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        idHistory = getIntent().getIntExtra("idHistory", 0);
        type = getIntent().getStringExtra("gameName");

        adView = findViewById(R.id.adView);
        adView = new AdView(this);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        MobileAds.initialize(this, "ca-app-pub-3952453830525109~9642702833");
        AdSize adSize = new AdSize(300, 70);
        adView.setAdSize(adSize);
        adView.setAdUnitId("ca-app-pub-3952453830525109/8670905080");
        AdRequest request = new AdRequest.Builder().build();
        adView.loadAd(request);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Toast.makeText(HistoryDetailActivity.this,
                        "Ad Failed To Load",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Toast.makeText(HistoryDetailActivity.this,
                        "Ad Load",
                        Toast.LENGTH_LONG).show();
            }
        });
        load();
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
                    adapter = new HistoryDetailAdapter(answer, HistoryDetailActivity.this, type);
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
