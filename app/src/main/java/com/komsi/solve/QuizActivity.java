package com.komsi.solve;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.komsi.solve.Adapter.OptionsAdapter;
import com.komsi.solve.Adapter.TypeQuizAdapter;
import com.komsi.solve.Api.RetrofitClient;
import com.komsi.solve.Model.OptionModel;
import com.komsi.solve.Model.QuestionModel;
import com.komsi.solve.Model.ResponseQuestion;
import com.komsi.solve.Model.TypeListModel;
import com.komsi.solve.Model.UserModel;
import com.komsi.solve.Storage.SharedPrefManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {
    LinearLayout soalLayout, readyLayout;
    private TextView timer, soal, number, sum, points, gameName;
    ProgressDialog progress;
    CountDownTimer cd;
    private ArrayList<QuestionModel> questionModel;
    private ArrayList<OptionModel> optionModel;
    private int currentQusetionId = 0;
    int idsoal, sumQues;
    String token;
    ImageView prevSoal, nextSoal, imgSoal;
    Context mCtx = QuizActivity.this;
    Button readyBtn;
    int status = 0;
    RecyclerView optionRV;
    OptionsAdapter adapter;
    FloatingActionButton fab;
    public static final String TAG = "bottom_sheet";
    String link = "http://10.33.72.22/solve/solve-jst/public/api/storage/question/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        soalLayout = findViewById(R.id.soalLayout);
        readyLayout = findViewById(R.id.readyLayout);
        idsoal = getIntent().getIntExtra("idsoal", 1);
        String namaSoal = getIntent().getStringExtra("namaSoal");
        //answerModels = new ArrayList<>();
        timer = findViewById(R.id.timer);
        number = findViewById(R.id.number);
        sum = findViewById(R.id.sum);
        soal = findViewById(R.id.soal);
        imgSoal = findViewById(R.id.imgSoal);
        nextSoal = findViewById(R.id.nextSoal);
        prevSoal = findViewById(R.id.prevSoal);
        readyBtn = findViewById(R.id.readyBtn);
        optionRV = findViewById(R.id.optionRV);
        fab = findViewById(R.id.fab);

        gameName = findViewById(R.id.gameName);
        gameName.setText(getIntent().getStringExtra("namaSoal") + " ");
        loadSoal();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                /*bundle.putString("productId", id);
                bundle.putString("productName", productName);
                bundle.putInt("productPrice", productPrice);
                bundle.putString("productPic", productPic);
                bundle.putInt("productStock", productStock);*/
                NavigationFragment fragment = new NavigationFragment();
                fragment.setArguments(bundle);
                fragment.show(((FragmentActivity) QuizActivity.this).getSupportFragmentManager(), TAG);
            }
        });


    }

    public void loadSoal() {
        progress = ProgressDialog.show(mCtx, null, "Loading ...", true, false);
        UserModel user = SharedPrefManager.getInstance(this).getUser();

        token = "Bearer " + user.getToken();
        Call<ResponseQuestion> call = RetrofitClient.getInstance().getApi().question("application/json", token, 1);
        call.enqueue(new Callback<ResponseQuestion>() {
            @Override
            public void onResponse(Call<ResponseQuestion> call, final Response<ResponseQuestion> response) {
                ResponseQuestion questionResponse = response.body();
                if (response.isSuccessful()) {
                    if (response.body().getQuestion().size() != 0) {


                        questionModel = response.body().getQuestion();
                        progress.dismiss();
                        sum.setText("/" + questionModel.size());
                        sumQues = questionModel.size();
                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(QuizActivity.this);
                        SharedPreferences.Editor editorList = sharedPrefs.edit();
                        Gson gson = new Gson();

                        String json = gson.toJson(questionModel);

                        editorList.putString("question", json);
                        editorList.commit();

                        readyBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                readyLayout.setVisibility(View.GONE);
                                soalLayout.setVisibility(view.VISIBLE);
                            }
                        });

                        showQuestion();
                        //timerA();
                    } else {
                        readyBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(mCtx,
                                        R.string.empty,
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                        progress.dismiss();
                    }
                } else {
                    progress.dismiss();
                    Log.i("debug", "onResponse : FAILED");
                    Toast.makeText(mCtx, R.string.something_wrong, Toast.LENGTH_LONG).show();
                    readyBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(mCtx,
                                    "Quiz Tidak Dapat Diakses",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ResponseQuestion> call, Throwable t) {
                Log.i("debug", "onResponse : FAILED");
                progress.dismiss();
                Toast.makeText(mCtx, R.string.something_wrong + t.toString(), Toast.LENGTH_LONG).show();
                readyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mCtx,
                                "Quiz Tidak Dapat Diakses",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void showQuestion() {
        QuestionModel questions = questionModel.get(currentQusetionId);
        soal.setText(questions.getQuestion());
        optionModel = questions.getOption();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        adapter = new OptionsAdapter(optionModel, QuizActivity.this, questions);
        optionRV.setLayoutManager(new LinearLayoutManager(QuizActivity.this));
        optionRV.setLayoutManager(staggeredGridLayoutManager);
        optionRV.setAdapter(adapter);
        if(questions.getPic_question().isEmpty()){
            imgSoal.setVisibility(View.GONE);
        }else {
            imgSoal.setVisibility(View.VISIBLE);
            Picasso.get().load(link + questions.getPic_question())
                    .into(imgSoal);
        }

        if (currentQusetionId == 0) {
            prevSoal.setVisibility(View.INVISIBLE);
        }
    }

    public void nextSoal(View view) {
        prevSoal.setVisibility(View.VISIBLE);
        if (currentQusetionId +1 == questionModel.size()) {
            nextSoal.setVisibility(View.INVISIBLE);
        }

        if (currentQusetionId + 1 == questionModel.size()) {
            if (status == 1) {
            } else {
            }

        } else {
            currentQusetionId++;
            final QuestionModel questions = questionModel.get(currentQusetionId);

            if (currentQusetionId > questionModel.size()) {

            } else {
                soal.setText(questions.getQuestion());
                number.setText(currentQusetionId + 1 + "");
                optionModel = questions.getOption();
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
                adapter = new OptionsAdapter(optionModel, QuizActivity.this, questions);
                optionRV.setLayoutManager(new LinearLayoutManager(QuizActivity.this));
                optionRV.setLayoutManager(staggeredGridLayoutManager);
                optionRV.setAdapter(adapter);
                if(questions.getPic_question().isEmpty()){
                    imgSoal.setVisibility(View.GONE);
                }else {
                    imgSoal.setVisibility(View.VISIBLE);
                    Picasso.get().load(link + questions.getPic_question())
                            .into(imgSoal);
                }
            }

        }
    }

    public void prevSoal(View view) {
        nextSoal.setVisibility(View.VISIBLE);
        if (currentQusetionId == 0) {
        } else {
            if (currentQusetionId == 1) {
                prevSoal.setVisibility(View.INVISIBLE);
            }
            if (currentQusetionId > 0) {
                currentQusetionId--;
                final QuestionModel questions = questionModel.get(currentQusetionId);
                soal.setText(questions.getQuestion());
                number.setText(currentQusetionId + 1 + "");
                optionModel = questions.getOption();
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
                adapter = new OptionsAdapter(optionModel, QuizActivity.this, questions);
                optionRV.setLayoutManager(new LinearLayoutManager(QuizActivity.this));
                optionRV.setLayoutManager(staggeredGridLayoutManager);
                optionRV.setAdapter(adapter);
                if(questions.getPic_question().isEmpty()){
                    imgSoal.setVisibility(View.GONE);
                }else {
                    imgSoal.setVisibility(View.VISIBLE);
                    Picasso.get().load(link + questions.getPic_question())
                            .into(imgSoal);
                }
            } else {
                prevSoal.setVisibility(View.INVISIBLE);
            }

        }
    }

    public void navigationSoal(View view) {
    }


}
