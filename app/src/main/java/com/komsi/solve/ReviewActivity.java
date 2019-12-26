package com.komsi.solve;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.komsi.solve.Adapter.NavigationAdapter;
import com.komsi.solve.Adapter.ReviewAdapter;
import com.komsi.solve.Model.QuestionModel;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity {
    RecyclerView reviewRV;
    ReviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        reviewRV = findViewById(R.id.RVmain);
        loadAns();
    }

    public void loadAns() {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ReviewActivity.this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("question", "question");
        //  String json = getArguments().getString("question");
        Type type = new TypeToken<ArrayList<QuestionModel>>() {
        }.getType();
        ArrayList<QuestionModel> questionModels = gson.fromJson(json, type);


        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        adapter = new ReviewAdapter(questionModels, ReviewActivity.this);
        reviewRV.setLayoutManager(new LinearLayoutManager(ReviewActivity.this));
        reviewRV.setLayoutManager(staggeredGridLayoutManager);
        reviewRV.setAdapter(adapter);
    }
}
