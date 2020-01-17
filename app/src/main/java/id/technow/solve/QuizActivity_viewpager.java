package id.technow.solve;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import id.technow.solve.Api.RetrofitClient;
import id.technow.solve.Model.QuestionModel;
import id.technow.solve.Model.ResponseQuestion;
import id.technow.solve.Model.UserModel;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import id.technow.solve.Adapter.QuizViewPagerAdapter;
import id.technow.solve.Model.ResponsePostAnswer;

import id.technow.solve.R;

import id.technow.solve.Storage.SharedPrefManager;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class QuizActivity_viewpager extends AppCompatActivity {
    LinearLayout soalLayout, readyLayout;
    private TextView timer;
    ProgressDialog progress;
    private ArrayList<QuestionModel> questionModel;
    String token;
    Context mCtx = QuizActivity_viewpager.this;
    Button readyBtn;
    FloatingActionButton fab;
    public static final String TAG = "bottom_sheet";
    String link = "http://10.33.85.59/solve/solve-jst/public/api/storage/question/";
    QuizViewPagerAdapter vpAdapter;
    public String limitFormat;
    public ViewPager viewPager;
    public int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_viewpager);
        soalLayout = findViewById(R.id.soalLayout);
        readyLayout = findViewById(R.id.readyLayout);
        viewPager = findViewById(R.id.viewPager);
        readyBtn = findViewById(R.id.readyBtn);
        fab = findViewById(R.id.fab);
        timer = findViewById(R.id.timer);

        loadSoal();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();

                NavigationFragment fragment = new NavigationFragment();
                fragment.setArguments(bundle);
                fragment.show(((FragmentActivity) QuizActivity_viewpager.this).getSupportFragmentManager(), TAG);

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

                        time = Integer.parseInt(questionResponse.getQuiz().getTime());
                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(QuizActivity_viewpager.this);
                        SharedPreferences.Editor editorList = sharedPrefs.edit();
                        Gson gson = new Gson();

                        String responseQuiz = gson.toJson(response.body());
                        editorList.putString("response", responseQuiz);

                        String json = gson.toJson(response.body().getQuestion());
                        editorList.putString("question", json);
                        editorList.commit();

                        progress.dismiss();
                        questionModel = response.body().getQuestion();
                        vpAdapter = new QuizViewPagerAdapter(questionModel, mCtx);

                        viewPager.setAdapter(vpAdapter);
                        readyBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                readyLayout.setVisibility(View.GONE);
                                soalLayout.setVisibility(view.VISIBLE);
                                getCurrentTime();
                            }
                        });
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

    public int getItem() {
        return viewPager.getCurrentItem() + 1;
    }

    private void getCurrentTime() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
        Calendar et = cal;
        et.add(Calendar.MINUTE, time);
        Calendar endTime = et;

        final long calMilis = cal.getTimeInMillis();
        final long etMilis = cal.getTimeInMillis() + (time * 60000);


        final long diff = etMilis - calMilis;

        new CountDownTimer(diff, 1000) {
            @Override
            public void onTick(long l) {
                int hours = (int) (((l / 1000) / 3600) % 24);
                int minutes = (int) ((l / 1000) / 60 % 60);
                int seconds = (int) ((l / 1000) % 60);
                String limitFormat = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
                timer.setText(
                        limitFormat);
            }

            @Override
            public void onFinish() {
                storeAnswer();
            }
        }.start();

    }

    public void saveInternal() {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(QuizActivity_viewpager.this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("response", "response");
        Type type = new TypeToken<ResponseQuestion>() {
        }.getType();
        ResponseQuestion responseQuestion = gson.fromJson(json, type);
        ArrayList<QuestionModel> questionModels = responseQuestion.getQuestion();

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("answer2.json", Context.MODE_PRIVATE));
            outputStreamWriter.write(json);
            outputStreamWriter.close();

            Toast.makeText(getApplicationContext(), "Composition saved", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("Exception", "File write failed: " + e.toString());
        }    }

    public void storeAnswer() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(QuizActivity_viewpager.this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("response", "response");
        //  String json = getArguments().getString("question");
        Type type = new TypeToken<ResponseQuestion>() {
        }.getType();
        ResponseQuestion responseQuestion = gson.fromJson(json, type);
        ArrayList<QuestionModel> questionModels = responseQuestion.getQuestion();


        progress = ProgressDialog.show(mCtx, null, "Loading ...", true, false);
        UserModel user = SharedPrefManager.getInstance(this).getUser();

        token = "Bearer " + user.getToken();
        Call<ResponsePostAnswer> call = RetrofitClient.getInstance().getApi().postQuestion("application/json", token, 1,
                responseQuestion);
        call.enqueue(new Callback<ResponsePostAnswer>() {
            @Override
            public void onResponse(Call<ResponsePostAnswer> call, final Response<ResponsePostAnswer> response) {
                ResponsePostAnswer questionResponse = response.body();
                if (response.isSuccessful()) {

                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(QuizActivity_viewpager.this);
                    SharedPreferences.Editor editorList = sharedPrefs.edit();
                    Gson gson = new Gson();

                    String responsePost = gson.toJson(response.body());
                    editorList.putString("answerPost", responsePost);
                    editorList.commit();

                    Toast.makeText(mCtx,
                            "Sukses",
                            Toast.LENGTH_LONG).show();
                    //saveInternal();
                    progress.dismiss();

                    Intent intent = new Intent(QuizActivity_viewpager.this, ResultQuizActivity.class);
                    startActivity(intent);

                } else {
                    progress.dismiss();
                    Log.i("debug", "Failed " + response.errorBody() + " ");

                    Toast.makeText(mCtx, response.code() + " " + response.message() + response.errorBody(),
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
            public void onFailure(Call<ResponsePostAnswer> call, Throwable t) {
                Log.i("debug", "onResponse : FAILED");
                progress.dismiss();
                Toast.makeText(mCtx,
                        t.toString() +
                                "Quiz Tidak Dapat Diakses",
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



}
