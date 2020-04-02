package id.technow.solve;

import androidx.appcompat.app.AppCompatActivity;

import id.technow.solve.Model.UserModel;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import id.technow.solve.Model.ResponsePostAnswer;

import id.technow.solve.R;

import id.technow.solve.Storage.SharedPrefManager;

import java.lang.reflect.Type;

public class ResultQuizActivity extends AppCompatActivity {
    TextView point, textCorrect, textSumQues;
    MaterialButton play, btn_home, btn_pembahasan;
    UserModel user = SharedPrefManager.getInstance(this).getUser();
    int total_score, idquiz, idhistory;
    String namaSoal, category, status;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_quiz);
        // idquiz = getIntent().getIntExtra("idsoal", 1);
        point = findViewById(R.id.point);
        namaSoal = getIntent().getStringExtra("namaSoal");
        status = getIntent().getStringExtra("status");

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ResultQuizActivity.this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("answerPost", "answerPost");
        //  String json = getArguments().getString("question");
        Type type = new TypeToken<ResponsePostAnswer>() {
        }.getType();
        ResponsePostAnswer responseAnswer = gson.fromJson(json, type);
        idquiz = responseAnswer.getResult().getQuiz_id();
        idhistory = responseAnswer.getResult().getId();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3952453830525109/1168174801");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }
        });
        point.setText(responseAnswer.getResult().getTotal_score() + "  pts");
        total_score = getIntent().getIntExtra("points", 0);
        textSumQues = findViewById(R.id.textSumQues);
        textCorrect = findViewById(R.id.textCorrect);
        String correct = String.valueOf(responseAnswer.getResult().getTrue_sum());
        int sum = getIntent().getIntExtra("sum", 10);
        textCorrect.setText(correct + " ");
        category = getIntent().getStringExtra("category");
        textSumQues.setText(responseAnswer.getResult().getAnswer_save().size() + " Pertanyaan");
        btn_pembahasan = findViewById(R.id.btn_pembahasan);

        btn_pembahasan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status.equals("active")){
                    Intent intent = new Intent(ResultQuizActivity.this, PembahasanQuizActivity.class);
                    intent.putExtra("idsoal", idquiz);
                    intent.putExtra("idHistory", idhistory);
                    intent.putExtra("namaSoal", namaSoal);
                    intent.putExtra("gameName", namaSoal);
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }
                    startActivity(intent);
                }
                else{
                    Toast.makeText(ResultQuizActivity.this, "Pembahasan Tidak Tersedia Untuk Quiz Ini", Toast.LENGTH_LONG).show();
                    btn_pembahasan.setEnabled(false);
                }

            }
        });

        btn_home = findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ResultQuizActivity.this, Main2Activity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ResultQuizActivity.this);
                SharedPreferences.Editor editorList = sharedPrefs.edit();
                editorList.clear();
                startActivity(i);
            }
        });
        play = findViewById(R.id.btn_play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultQuizActivity.this, QuizActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("category", category);
                intent.putExtra("namaSoal", getIntent().getStringExtra("namaSoal") + " ");
                intent.putExtra("idsoal", idquiz);

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                // Toast.makeText(ResultQuizActivity.this, idquiz+" ", Toast.LENGTH_LONG).show();
                intent.putExtra("namaSoal", namaSoal);

                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ResultQuizActivity.this);
                SharedPreferences.Editor editorList = sharedPrefs.edit();
                editorList.clear();
                editorList.apply();
                startActivity(intent);
            }


        });
    }

    @Override
    public void onBackPressed() {
        confirmOnBackPressed();
    }

    private void confirmOnBackPressed() {
        Intent i = new Intent(ResultQuizActivity.this, Main2Activity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
