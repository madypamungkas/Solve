package id.technow.solve;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import id.technow.solve.Api.RetrofitClient;
import id.technow.solve.Model.ResponseTypeList;
import id.technow.solve.Model.TypeListModel;
import id.technow.solve.Model.UserModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;

import id.technow.solve.Adapter.TypeQuizAdapter;

import id.technow.solve.Storage.SharedPrefManager;

import java.util.ArrayList;

public class QuizChooseActivity extends AppCompatActivity {

    ArrayList<TypeListModel> models;
    RecyclerView RVmain;
    TypeQuizAdapter adapter;
    CardView cardGame;
    int idType;
    TextView typeGame;
    ImageView imgGame;
    LinearLayout layoutNoData;
    RelativeLayout layoutData;
    SwipeRefreshLayout swipeRefresh;
    ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_choose);
        RVmain = findViewById(R.id.RVmain);
        cardGame = findViewById(R.id.cardGame);
        typeGame = findViewById(R.id.typeGame);
        imgGame = findViewById(R.id.imgGame);
        layoutData = findViewById(R.id.layoutData);
        layoutNoData = findViewById(R.id.layoutNoData);
        shimmerFrameLayout = findViewById(R.id.shimmerContainer);

        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setEnabled(true);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkConnection();
            }
        });

        idType = getIntent().getIntExtra("idType", 1);
        cardGame.setOnClickListener(new View.OnClickListener() {
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
    }

    @Override
    public void onResume() {
        super.onResume();
        checkConnection();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void checkConnection() {
        if (isNetworkAvailable()) {
            getListCategory();
        } else {
            final Dialog dialog = new Dialog(QuizChooseActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_no_internet);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;

            dialog.getWindow().setLayout((9 * width) / 10, height);

            MaterialButton btnRetry = dialog.findViewById(R.id.btnRetry);
            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    checkConnection();
                }
            });
            dialog.show();
        }
    }

    public void getListCategory() {
        layoutData.setVisibility(View.VISIBLE);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
        RVmain.setVisibility(View.GONE);
        cardGame.setVisibility(View.GONE);
        layoutNoData.setVisibility(View.GONE);
        UserModel user = SharedPrefManager.getInstance(this).getUser();
        String token = "Bearer " + user.getToken();

        Call<ResponseTypeList> call = RetrofitClient.getInstance().getApi().quiz(token, "application/json", idType);
        call.enqueue(new Callback<ResponseTypeList>() {
            @Override
            public void onResponse(Call<ResponseTypeList> call, Response<ResponseTypeList> response) {
                ResponseTypeList model = response.body();
                if (response.isSuccessful()) {
                    models = response.body().getResult();
                    if (models == null) {
                        shimmerFrameLayout.setVisibility(View.GONE);
                        layoutNoData.setVisibility(View.VISIBLE);
                        layoutData.setVisibility(View.GONE);
                    } else {
                        shimmerFrameLayout.setVisibility(View.GONE);
                        layoutData.setVisibility(View.VISIBLE);
                        RVmain.setVisibility(View.VISIBLE);
                        cardGame.setVisibility(View.VISIBLE);
                        layoutNoData.setVisibility(View.GONE);
                        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
                        adapter = new TypeQuizAdapter(models, QuizChooseActivity.this);
                        RVmain.setLayoutManager(new LinearLayoutManager(QuizChooseActivity.this));
                        RVmain.setLayoutManager(staggeredGridLayoutManager);
                        RVmain.setAdapter(adapter);
                    }

                } else {
                    Toast.makeText(QuizChooseActivity.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
                }
                swipeRefresh.setRefreshing(false);
                shimmerFrameLayout.stopShimmer();
            }

            @Override
            public void onFailure(Call<ResponseTypeList> call, Throwable t) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                RVmain.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                layoutNoData.setVisibility(View.VISIBLE);
                layoutData.setVisibility(View.GONE);
                Toast.makeText(QuizChooseActivity.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
