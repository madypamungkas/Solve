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
import android.graphics.Color;
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
import com.google.gson.reflect.TypeToken;
import com.komsi.solve.Adapter.NavigationAdapter;
import com.komsi.solve.Adapter.OptionsAdapter;
import com.komsi.solve.Adapter.TypeQuizAdapter;
import com.komsi.solve.Api.RetrofitClient;
import com.komsi.solve.Model.OptionModel;
import com.komsi.solve.Model.QuestionModel;
import com.komsi.solve.Model.ResponseLogin;
import com.komsi.solve.Model.ResponseQuestion;
import com.komsi.solve.Model.TypeListModel;
import com.komsi.solve.Model.UserModel;
import com.komsi.solve.Storage.SharedPrefManager;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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
    String link = "http://10.33.85.59/solve/solve-jst/public/api/storage/question/";
    Button submitBtn;
    Runnable runnable;
    NavigationAdapter nAdapter;


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
        submitBtn = findViewById(R.id.submitBtn);


        gameName = findViewById(R.id.gameName);
        gameName.setText("K1 T1 Q1");
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
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(QuizActivity.this);
                Gson gson = new Gson();
                String json = sharedPrefs.getString("question", "question");
                Type type = new TypeToken<ArrayList<QuestionModel>>() {
                }.getType();
                ArrayList<QuestionModel> questionModels = gson.fromJson(json, type);
               // checkOption();

                //navigationSoal();
                bundle.putString("question", json);
                NavigationFragment fragment = new NavigationFragment();
                fragment.setArguments(bundle);
                fragment.show(((FragmentActivity) QuizActivity.this).getSupportFragmentManager(), TAG);

            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // storeAnswer();
                Intent intent = new Intent(QuizActivity.this, ReviewActivity.class);
                startActivity(intent);
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

                        String responseQuiz = gson.toJson(response.body());
                        editorList.putString("response", responseQuiz);

                        String json = gson.toJson(questionModel);
                        editorList.putString("question", json);
                        editorList.commit();

                        SharedPreferences.Editor editor = sharedPrefs.edit();
                        editor.putInt("num", currentQusetionId);
                        editor.commit();

                        readyBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                readyLayout.setVisibility(View.GONE);
                                soalLayout.setVisibility(view.VISIBLE);
                                getCurrentTime();
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
                                "Quiz Tidak Dapat ddDiakses",
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
        if (questions.getPic_question().isEmpty()) {
            imgSoal.setVisibility(View.GONE);
        } else {
            imgSoal.setVisibility(View.VISIBLE);
            Picasso.get().load(link + questions.getPic_question())
                    .into(imgSoal);
        }

        if (currentQusetionId == 0) {
            prevSoal.setVisibility(View.INVISIBLE);
        }
    }

    public void checkOption() {
       /* if (adapter.getSelected().getId() == 1) {
            questionModel.get(currentQusetionId).setUser_answer(adapter.getSelected().getContents());

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mCtx);
            SharedPreferences.Editor editorList = sharedPrefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(questionModel);
            editorList.putString("question", json);
            editorList.commit();
        } else {

        }*/
    }

    public void nextSoal(View view) {
        prevSoal.setVisibility(View.VISIBLE);
        checkOption();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(QuizActivity.this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("question", "question");
        Type type = new TypeToken<ArrayList<QuestionModel>>() {
        }.getType();
        ArrayList<QuestionModel> questionSave = gson.fromJson(json, type);

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt("num", currentQusetionId);
        editor.commit();

        if (currentQusetionId + 1 == questionSave.size()) {
            nextSoal.setVisibility(View.INVISIBLE);
            submitBtn.setVisibility(View.VISIBLE);
        }

        if (currentQusetionId + 1 == questionSave.size()) {
            if (status == 1) {
            } else {
            }

        } else {
            currentQusetionId++;
            final QuestionModel questions = questionSave.get(currentQusetionId);

            if (currentQusetionId > questionSave.size()) {

            } else {
                soal.setText(questions.getQuestion());
                number.setText(currentQusetionId + 1 + "");
                optionModel = questions.getOption();
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
                adapter = new OptionsAdapter(optionModel, QuizActivity.this, questions);
                optionRV.setLayoutManager(new LinearLayoutManager(QuizActivity.this));
                optionRV.setLayoutManager(staggeredGridLayoutManager);
                optionRV.setAdapter(adapter);
                if (questions.getPic_question().isEmpty()) {
                    imgSoal.setVisibility(View.GONE);
                } else {
                    imgSoal.setVisibility(View.VISIBLE);
                    Picasso.get().load(link + questions.getPic_question())
                            .into(imgSoal);
                }
            }

        }
    }

    public void prevSoal(View view) {
        nextSoal.setVisibility(View.VISIBLE);
        prevSoal.setVisibility(View.VISIBLE);
        submitBtn.setVisibility(View.GONE);
        checkOption();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(QuizActivity.this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("question", "question");
        Type type = new TypeToken<ArrayList<QuestionModel>>() {
        }.getType();
        ArrayList<QuestionModel> questionSave = gson.fromJson(json, type);

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt("num", currentQusetionId);
        editor.commit();

        if (currentQusetionId == 0) {
        } else {
            if (currentQusetionId == 1) {
                prevSoal.setVisibility(View.INVISIBLE);
            }
            if (currentQusetionId > 0) {
                currentQusetionId--;
                final QuestionModel questions = questionSave.get(currentQusetionId);
                soal.setText(questions.getQuestion());
                number.setText(currentQusetionId + 1 + "");
                optionModel = questions.getOption();
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
                adapter = new OptionsAdapter(optionModel, QuizActivity.this, questions);
                optionRV.setLayoutManager(new LinearLayoutManager(QuizActivity.this));
                optionRV.setLayoutManager(staggeredGridLayoutManager);
                optionRV.setAdapter(adapter);
                if (questions.getPic_question().isEmpty()) {
                    imgSoal.setVisibility(View.GONE);
                } else {
                    imgSoal.setVisibility(View.VISIBLE);
                    Picasso.get().load(link + questions.getPic_question())
                            .into(imgSoal);
                }
            } else {
                prevSoal.setVisibility(View.INVISIBLE);
            }
        }
    }
    public int num(int num){
        num =nAdapter.getSelectedItem();
        currentQusetionId = num;
        navigationSoal();
        return currentQusetionId;
    }

    public void navigationSoal() {

        nextSoal.setVisibility(View.VISIBLE);
        prevSoal.setVisibility(View.VISIBLE);
        submitBtn.setVisibility(View.GONE);
//        checkOption();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(QuizActivity.this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("question", "question");
        Type type = new TypeToken<ArrayList<QuestionModel>>() {
        }.getType();
        ArrayList<QuestionModel> questionSave = gson.fromJson(json, type);

       /* SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt("num", currentQusetionId);
        editor.commit();*/


        if (currentQusetionId == 1) {
            prevSoal.setVisibility(View.INVISIBLE);
        }
        if (currentQusetionId > 0) {
            currentQusetionId--;
            final QuestionModel questions = questionSave.get(currentQusetionId);
            soal.setText(questions.getQuestion());
            number.setText(currentQusetionId + 1 + "");
            optionModel = questions.getOption();
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
            adapter = new OptionsAdapter(optionModel, QuizActivity.this, questions);
            optionRV.setLayoutManager(new LinearLayoutManager(QuizActivity.this));
            optionRV.setLayoutManager(staggeredGridLayoutManager);
            optionRV.setAdapter(adapter);
            if (questions.getPic_question().isEmpty()) {
                imgSoal.setVisibility(View.GONE);
            } else {
                imgSoal.setVisibility(View.VISIBLE);
                Picasso.get().load(link + questions.getPic_question())
                        .into(imgSoal);
            }
        } else {
            prevSoal.setVisibility(View.INVISIBLE);
        }
    }

    public void storeAnswer() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mCtx);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("response", "response");
        Type type = new TypeToken<ResponseQuestion>() {
        }.getType();
        ResponseQuestion responseQuestion = gson.fromJson(json, type);

        progress = ProgressDialog.show(mCtx, null, "Loading ...", true, false);
        UserModel user = SharedPrefManager.getInstance(this).getUser();

        token = "Bearer " + user.getToken();
        Call<ResponseQuestion> call = RetrofitClient.getInstance().getApi().postQuestion("application/json", token, 1,
                responseQuestion);
        call.enqueue(new Callback<ResponseQuestion>() {
            @Override
            public void onResponse(Call<ResponseQuestion> call, final Response<ResponseQuestion> response) {
                ResponseQuestion questionResponse = response.body();
                if (response.isSuccessful()) {
                    Toast.makeText(mCtx,
                            "Sukses",
                            Toast.LENGTH_LONG).show();
                   /* if (response.body().getQuestion().size() != 0) {


                        questionModel = response.body().getQuestion();
                        progress.dismiss();
                        sum.setText("/" + questionModel.size());
                        sumQues = questionModel.size();
                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(QuizActivity.this);
                        SharedPreferences.Editor editorList = sharedPrefs.edit();
                        Gson gson = new Gson();

                        String responseQuiz = gson.toJson(response.body());
                        editorList.putString("response", responseQuiz);

                        String json = gson.toJson(questionModel);
                        editorList.putString("question", json);
                        editorList.commit();

                        SharedPreferences.Editor editor = sharedPrefs.edit();
                        editor.putInt("num", currentQusetionId);
                        editor.commit();

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
                    }*/
                } else {
                    progress.dismiss();
                    Log.i("debug", "onResponse : FAILED");
                    Toast.makeText(mCtx, response.code() + " ",
                            //R.string.something_wrong,
                            Toast.LENGTH_LONG).show();
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
                Toast.makeText(mCtx,
                        t.toString() + " ",
                        //R.string.something_wrong + t.toString(),
                        Toast.LENGTH_LONG).show();
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
    private void getCurrentTime(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
        Calendar et = cal;
        et.add(Calendar.MINUTE, 90);
        Calendar endTime = et;

        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm:ss");
        date.setTimeZone(TimeZone.getTimeZone("GMT+7:00"));

        Date endLocalTime = endTime.getTime();
        DateFormat dateEnd = new SimpleDateFormat("HH:mm:ss");
        dateEnd.setTimeZone(TimeZone.getTimeZone("GMT+7:00"));

        final long calMilis = cal.getTimeInMillis();
        final long etMilis = cal.getTimeInMillis()+ (90*60000);


//        String localTime = date.format(currentLocalTime);

       //timer.setText(localTime);
        final long diff = etMilis - calMilis;

        new CountDownTimer(diff, 1000) {
            @Override
            public void onTick(long l) {
                int hours = (int) (((l / 1000) / 3600) % 24);
                int minutes = (int) ((l / 1000) / 60 %60);
                int seconds = (int) ((l / 1000) % 60);
                String limitFormat = String.format(Locale.getDefault(), "%02d:%02d:%02d",hours, minutes, seconds);
                timer.setText( //etMilis + "\n"+ calMilis +
                        limitFormat);
            }

            @Override
            public void onFinish() {
                storeAnswer();
            }
        }.start();

    }

}
