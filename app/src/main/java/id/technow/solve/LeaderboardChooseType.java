package id.technow.solve;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.technow.solve.Adapter.LeaChooseTypeAdapter;
import id.technow.solve.Adapter.LeaderboardChooseAdapter;
import id.technow.solve.Adapter.TypeGameAdapter;
import id.technow.solve.Api.RetrofitClient;
import id.technow.solve.Model.CategoryModel;
import id.technow.solve.Model.MenuHomeModel;
import id.technow.solve.Model.ResponseCategory;
import id.technow.solve.Model.TypeListModel;
import id.technow.solve.Model.UserModel;
import id.technow.solve.R;
import id.technow.solve.Storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaderboardChooseType extends AppCompatActivity {
    private ArrayList<CategoryModel> categories;
    private RecyclerView typeRV;
    LeaChooseTypeAdapter adapter;
    int idCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard_choose_type);
        typeRV = findViewById(R.id.typeRV);
        typeRV.setLayoutManager(new LinearLayoutManager(LeaderboardChooseType.this));
        idCategory = getIntent().getIntExtra("idCategory",1);
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
        Call<ResponseCategory> call = RetrofitClient.getInstance().getApi().typeList(token, "application/json", idCategory);
        call.enqueue(new Callback<ResponseCategory>() {
            @Override
            public void onResponse(Call<ResponseCategory> call, Response<ResponseCategory> response) {
                ResponseCategory category__response = response.body();
                if (response.isSuccessful()) {
                    int size = category__response.getResult().size();
                    categories = response.body().getResult();
                    adapter = new LeaChooseTypeAdapter(LeaderboardChooseType.this, categories);
                    typeRV.setAdapter(adapter);
                } else {
                        Toast.makeText(LeaderboardChooseType.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseCategory> call, Throwable t) {

                    Toast.makeText(LeaderboardChooseType.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();

            }
        });

    }

}
