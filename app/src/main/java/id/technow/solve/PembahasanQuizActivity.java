package id.technow.solve;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

import id.technow.solve.R;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PembahasanQuizActivity extends AppCompatActivity {
    private ArrayList<QuestionModel> answerModels;
    private RecyclerView typeRV;
    PembahasanAdapter adapter;
    int id;
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

    }
}
