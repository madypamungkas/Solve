package id.technow.solve;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import id.technow.solve.Api.RetrofitClient;
import id.technow.solve.Model.CategoryModel;
import id.technow.solve.Model.ResponseCategory;
import id.technow.solve.Model.UserModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import id.technow.solve.Adapter.TypeGameAdapter;

import id.technow.solve.R;

import id.technow.solve.Storage.SharedPrefManager;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TypeChooseActivity extends AppCompatActivity {
    private ArrayList<CategoryModel> categories;
    private RecyclerView typeRV;
    TypeGameAdapter adapter;
    int idCategory;
    ImageView imgCategory;
    Context mCtx;
    LinearLayout layoutNoData, layoutContainer;
    SwipeRefreshLayout swipeRefresh;
    ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_choose);

        mCtx = this;
        idCategory = getIntent().getIntExtra("idCategory", 1);

        String Link = "https://solve.technow.id/storage/quiz_category2/";
        typeRV = findViewById(R.id.typeRV);
        shimmerFrameLayout = findViewById(R.id.shimmerContainer);
        imgCategory = findViewById(R.id.imgCategory);
        layoutNoData = findViewById(R.id.layoutNoData);
        layoutContainer = findViewById(R.id.layoutContainer);
        typeRV.setLayoutManager(new LinearLayoutManager(mCtx));
        TextView txtDesc = findViewById(R.id.txtDesc);
        TextView tvQuizType = findViewById(R.id.tvQuizType);
        tvQuizType.setText(getIntent().getStringExtra("Type"));
        txtDesc.setText(getIntent().getStringExtra("desc"));
        Picasso.get().load(Link + idCategory).error(R.drawable.img_type_astronomi).into(imgCategory);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setEnabled(true);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkConnection();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(mCtx, Main2Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
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
            load();
        } else {
            final Dialog dialog = new Dialog(mCtx);
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

    public void load() {
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
        layoutContainer.setVisibility(View.GONE);
        layoutNoData.setVisibility(View.GONE);
        typeRV.setVisibility(View.GONE);
        UserModel user = SharedPrefManager.getInstance(this).getUser();
        String token = "Bearer " + user.getToken();
        Call<ResponseCategory> call = RetrofitClient.getInstance().getApi().typeList(token, "application/json", idCategory);
        call.enqueue(new Callback<ResponseCategory>() {
            @Override
            public void onResponse(Call<ResponseCategory> call, Response<ResponseCategory> response) {
                ResponseCategory category__response = response.body();
                if (response.isSuccessful()) {
                    int size = category__response.getResult().size();
                    categories = response.body().getResult();
                    if (categories.isEmpty()) {
                        shimmerFrameLayout.setVisibility(View.GONE);
                        layoutContainer.setVisibility(View.VISIBLE);
                        layoutNoData.setVisibility(View.VISIBLE);
                        typeRV.setVisibility(View.GONE);
                    } else {
                        shimmerFrameLayout.setVisibility(View.GONE);
                        layoutContainer.setVisibility(View.VISIBLE);
                        layoutNoData.setVisibility(View.GONE);
                        typeRV.setVisibility(View.VISIBLE);
                        adapter = new TypeGameAdapter(mCtx, categories);
                        typeRV.setAdapter(adapter);
                    }

                } else {
                    Toast.makeText(mCtx, R.string.something_wrong, Toast.LENGTH_SHORT).show();
                }
                swipeRefresh.setRefreshing(false);
                shimmerFrameLayout.stopShimmer();
            }

            @Override
            public void onFailure(Call<ResponseCategory> call, Throwable t) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                typeRV.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                layoutContainer.setVisibility(View.GONE);
                Toast.makeText(mCtx, R.string.something_wrong, Toast.LENGTH_SHORT).show();
            }
        });

    }

}