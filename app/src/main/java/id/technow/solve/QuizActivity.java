package id.technow.solve;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import id.technow.solve.Api.RetrofitClient;
import id.technow.solve.Model.OptionModel;
import id.technow.solve.Model.QuestionModel;
import id.technow.solve.Model.ResponseQuestion;
import id.technow.solve.Model.UserModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import id.technow.solve.Adapter.NavigationAdapter;
import id.technow.solve.Adapter.OptionsAdapter;
import id.technow.solve.Model.ResponsePostAnswer;

import id.technow.solve.R;

import id.technow.solve.Storage.SharedPrefManager;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class QuizActivity extends AppCompatActivity {
    LinearLayout soalLayout, readyLayout;
    private TextView timer, soal, number, sum, namaSoal, gameName, tvIsian;
    ProgressDialog progress;
    CountDownTimer cd;
    private List<QuestionModel> questionModel;
    private List<OptionModel> optionModel;
    private int currentQusetionId = 0;
    int idsoal, sumQues;
    String token;
    ImageView prevSoal, nextSoal, imgSoal;
    Context mCtx = QuizActivity.this;
    MaterialButton readyBtn;
    int status = 0;
    RecyclerView optionRV;
    OptionsAdapter adapter;
    NestedScrollView scrollOption;
    public int time;
    FloatingActionButton fab, fab2;
    public static final String TAG = "bottom_sheet";
    String link = "https://solve.technow.id/storage/question/";
    TextInputLayout layoutAnswer;
    LinearLayout layoutAns;
    TextInputEditText inputAnswer;
    String namaQuiz, codeQuiz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        soalLayout = findViewById(R.id.soalLayout);
        layoutAnswer = findViewById(R.id.layoutAnswer);
        layoutAns = findViewById(R.id.layoutAns);
        inputAnswer = findViewById(R.id.inputAnswer);
        tvIsian = findViewById(R.id.tvIsian);
        readyLayout = findViewById(R.id.readyLayout);
        idsoal = getIntent().getIntExtra("idsoal", 1);
        namaQuiz = getIntent().getStringExtra("namaSoal");
        codeQuiz = getIntent().getStringExtra("codeSoal");

        timer = findViewById(R.id.timer);
        number = findViewById(R.id.number);
        sum = findViewById(R.id.sum);
        soal = findViewById(R.id.soal);
        namaSoal = findViewById(R.id.namaSoal);
        imgSoal = findViewById(R.id.imgSoal);
        nextSoal = findViewById(R.id.nextSoal);
        prevSoal = findViewById(R.id.prevSoal);
        readyBtn = findViewById(R.id.readyBtn);
        scrollOption = findViewById(R.id.scrollOption);
        optionRV = findViewById(R.id.optionRV);
        fab = findViewById(R.id.fab);
        fab2 = findViewById(R.id.fab2);
        gameName = findViewById(R.id.gameName);
        loadSoal();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navSave();
                Bundle bundle = new Bundle();
                bundle.putInt("idSoal", idsoal);
                bundle.putString("namaSoal", namaQuiz);

                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(QuizActivity.this);
                Gson gson = new Gson();
                String json = sharedPrefs.getString("question", "question");
                Type type = new TypeToken<ArrayList<QuestionModel>>() {
                }.getType();
                ArrayList<QuestionModel> questionModels = gson.fromJson(json, type);

                bundle.putString("question", json);
                NavigationFragment fragment = new NavigationFragment();
                fragment.setArguments(bundle);
                fragment.show(((FragmentActivity) QuizActivity.this).getSupportFragmentManager(), TAG);

            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //storeAnswer();
                Intent readyBtn = new Intent(QuizActivity.this, ReviewActivity.class);
                startActivity(readyBtn);

            }
        });

        scrollOption.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    fab.hide();
                    if (fab2.getVisibility() == View.VISIBLE) {
                        fab2.setVisibility(View.INVISIBLE);
                    }
                } else {
                    fab.show();
                    if (fab2.getVisibility() == View.INVISIBLE) {
                        fab2.show();
                    }
                }
            }
        });

        nextSoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextSoal();
            }
        });
        prevSoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevSoal();
            }
        });

        inputAnswer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    saveIsian();
                    return true;
                }
                return false;
            }
        });
    }

    public void loadSoal() {
        progress = ProgressDialog.show(mCtx, null, "Loading ...", true, false);
        UserModel user = SharedPrefManager.getInstance(this).getUser();

        token = "Bearer " + user.getToken();
        Call<ResponseQuestion> call = RetrofitClient.getInstance().getApi().question("application/json", token, idsoal);
        call.enqueue(new Callback<ResponseQuestion>() {
            @Override
            public void onResponse(Call<ResponseQuestion> call, final Response<ResponseQuestion> response) {
                ResponseQuestion questionResponse = response.body();
                if (response.isSuccessful()) {
                    if (response.body().getQuestion().size() != 0) {

                        gameName.setText(response.body().getQuiz().getTitle());
                        namaSoal.setText(response.body().getQuiz().getTitle());

                        time = Integer.parseInt(questionResponse.getQuiz().getTime());
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
                        editorList.apply();

                        SharedPreferences.Editor editor = sharedPrefs.edit();
                        editor.putInt("num", currentQusetionId);
                        editor.apply();

                        readyBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getCurrentTime();
                                readyLayout.setVisibility(View.GONE);
                                soalLayout.setVisibility(View.VISIBLE);
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
                Toast.makeText(mCtx, R.string.something_wrong, Toast.LENGTH_LONG).show();
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
        if (questions.getType().equals("option")) {
            optionRV.setVisibility(View.VISIBLE);
            layoutAns.setVisibility(View.GONE);
            adapter = new OptionsAdapter(optionModel, QuizActivity.this, questions);
            optionRV.setLayoutManager(new LinearLayoutManager(QuizActivity.this));
            optionRV.setLayoutManager(staggeredGridLayoutManager);
            nextSoal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nextSoal();
                }
            });
            prevSoal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    prevSoal();
                }
            });
            optionRV.setAdapter(adapter);
        } else {
            layoutAns.setVisibility(View.VISIBLE);
            optionRV.setVisibility(View.GONE);
            tvIsian.setText("Jawaban Anda : " + questionModel.get(currentQusetionId).getUser_answer_content());
            inputAnswer.setText(questionModel.get(currentQusetionId).getUser_answer_content());
            if (questions.getUser_answer_content().equals("**")) {
                tvIsian.setText("Jawaban Anda : ");
                inputAnswer.setText("");
            }
            nextSoal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveNext();
                }
            });
            prevSoal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    savePrev();
                }
            });

        }

        if (questions.getPic_question() == null) {
            imgSoal.setVisibility(View.GONE);
        } else {
            imgSoal.setVisibility(View.VISIBLE);
            Picasso.get().load(link + questions.getId_soal())
                    .into(imgSoal);
        }

        if (currentQusetionId == 0) {
            prevSoal.setVisibility(View.INVISIBLE);
        }
    }

    public void saveOption() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mCtx);
        Gson gson = new Gson();

        String json = sharedPrefs.getString("response", "response");
        Type type = new TypeToken<ResponseQuestion>() {
        }.getType();
        ResponseQuestion responseQuestion = gson.fromJson(json, type);

        String json2 = sharedPrefs.getString("question", "question");
        Type type2 = new TypeToken<ArrayList<QuestionModel>>() {
        }.getType();
        ArrayList<QuestionModel> questionSave = gson.fromJson(json2, type2);


        ArrayList<QuestionModel> que = questionSave;
        String userAnswer = sharedPrefs.getString("userAnswer", "**");
        String userAnswerContent = sharedPrefs.getString("userAnswerContent", "**");

  /*      int questionPosition = sharedPrefs.getInt("position", 0);

        List<OptionModel> ops = que.get(currentQusetionId).getOption();
*/
        que.get(currentQusetionId).setUser_answer(userAnswer);
        que.get(currentQusetionId).setUser_answer_content(userAnswerContent);

        SharedPreferences.Editor editorList = sharedPrefs.edit();
        // editorList.putString("userAnswer", option.getOption());

        String questionSt = gson.toJson(questionSave);
        editorList.putString("question", questionSt);

        responseQuestion.setQuestion(questionSave);
        String responseQuiz = gson.toJson(responseQuestion);
        editorList.putString("response", responseQuiz);

        editorList.apply();
    }


    @SuppressLint("RestrictedApi")
    public void nextSoal() {
        prevSoal.setVisibility(View.VISIBLE);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(QuizActivity.this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("question", "question");
        Type type = new TypeToken<ArrayList<QuestionModel>>() {
        }.getType();
        ArrayList<QuestionModel> questionSave = gson.fromJson(json, type);

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt("num", currentQusetionId);
        editor.apply();

        if (currentQusetionId + 1 == questionSave.size()) {
            nextSoal.setVisibility(View.INVISIBLE);
            // submitBtn.setVisibility(View.VISIBLE);
            fab2.setVisibility(View.INVISIBLE);
            if (fab.getVisibility() == View.VISIBLE) {
                fab2.setVisibility(View.VISIBLE);
            }
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
                if (questions.getType().equals("option")) {
                    optionRV.setVisibility(View.VISIBLE);
                    layoutAns.setVisibility(View.GONE);
                    optionModel = questions.getOption();
                    nextSoal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            nextSoal();
                        }
                    });
                    prevSoal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            prevSoal();
                        }
                    });
                    StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
                    adapter = new OptionsAdapter(optionModel, QuizActivity.this, questions);
                    optionRV.setLayoutManager(new LinearLayoutManager(QuizActivity.this));
                    optionRV.setLayoutManager(staggeredGridLayoutManager);
                    optionRV.setAdapter(adapter);
                } else {
                    layoutAns.setVisibility(View.VISIBLE);
                    optionRV.setVisibility(View.GONE);
                    nextSoal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            saveNext();
                        }
                    });
                    prevSoal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            savePrev();
                        }
                    });
                    tvIsian.setText("Jawaban Anda : " + questionSave.get(currentQusetionId).getUser_answer_content());
                    inputAnswer.setText(questionSave.get(currentQusetionId).getUser_answer_content());
                    if (questions.getUser_answer_content().equals("**")) {
                        tvIsian.setText("Jawaban Anda : ");
                        inputAnswer.setText("");
                    }

                }


                if (questions.getPic_question() == null) {
                    imgSoal.setVisibility(View.GONE);
                } else {
                    imgSoal.setVisibility(View.VISIBLE);
                    Picasso.get().load(link + questions.getId_soal())
                            .into(imgSoal);
                }
            }

        }
    }


    public void prevSoal() {
        nextSoal.setVisibility(View.VISIBLE);
        prevSoal.setVisibility(View.VISIBLE);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(QuizActivity.this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("question", "question");
        Type type = new TypeToken<ArrayList<QuestionModel>>() {
        }.getType();
        ArrayList<QuestionModel> questionSave = gson.fromJson(json, type);

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt("num", currentQusetionId);
        editor.apply();

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
                if (questions.getType().equals("option")) {
                    optionRV.setVisibility(View.VISIBLE);
                    layoutAns.setVisibility(View.GONE);
                    nextSoal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            nextSoal();
                        }
                    });
                    prevSoal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            prevSoal();
                        }
                    });
                    optionModel = questions.getOption();
                    StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
                    adapter = new OptionsAdapter(optionModel, QuizActivity.this, questions);
                    optionRV.setLayoutManager(new LinearLayoutManager(QuizActivity.this));
                    optionRV.setLayoutManager(staggeredGridLayoutManager);
                    optionRV.setAdapter(adapter);
                } else {
                    layoutAns.setVisibility(View.VISIBLE);
                    optionRV.setVisibility(View.GONE);
                    nextSoal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            saveNext();
                        }
                    });
                    prevSoal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            savePrev();
                        }
                    });
                    tvIsian.setText("Jawaban Anda : " + questionSave.get(currentQusetionId).getUser_answer_content());
                    inputAnswer.setText(questionSave.get(currentQusetionId).getUser_answer_content());
                    if (questions.getUser_answer_content().equals("**")) {
                        tvIsian.setText("Jawaban Anda : ");
                        inputAnswer.setText("");
                    }
                }
                if (questions.getPic_question() == null) {
                    imgSoal.setVisibility(View.GONE);
                } else {
                    imgSoal.setVisibility(View.VISIBLE);
                    Picasso.get().load(link + questions.getId_soal())
                            .into(imgSoal);
                }
            } else {
                prevSoal.setVisibility(View.INVISIBLE);
            }
        }
    }

    @SuppressLint("RestrictedApi")
    public void navigationSoal(int num) {
        //saveOption();
        currentQusetionId = num;
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(QuizActivity.this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("question", "question");
        Type type = new TypeToken<ArrayList<QuestionModel>>() {
        }.getType();
        ArrayList<QuestionModel> questionSave = gson.fromJson(json, type);

        nextSoal.setVisibility(View.VISIBLE);
        prevSoal.setVisibility(View.VISIBLE);

        final QuestionModel questions = questionSave.get(currentQusetionId);
        soal.setText(questions.getQuestion());
        number.setText(currentQusetionId + 1 + "");
        if (questions.getType().equals("option")) {
            optionRV.setVisibility(View.VISIBLE);
            layoutAns.setVisibility(View.GONE);

            nextSoal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nextSoal();
                }
            });
            prevSoal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    prevSoal();
                }
            });
            optionModel = questions.getOption();
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
            adapter = new OptionsAdapter(optionModel, QuizActivity.this, questions);
            optionRV.setLayoutManager(new LinearLayoutManager(QuizActivity.this));
            optionRV.setLayoutManager(staggeredGridLayoutManager);
            optionRV.setAdapter(adapter);
        } else {
            layoutAns.setVisibility(View.VISIBLE);
            optionRV.setVisibility(View.GONE);
            nextSoal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveNext();
                }
            });
            prevSoal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    savePrev();
                }
            });
            tvIsian.setText("Jawaban Anda : " + questionSave.get(currentQusetionId).getUser_answer_content());
            inputAnswer.setText(questionSave.get(currentQusetionId).getUser_answer_content());
            if (questions.getUser_answer_content().equals("**")) {
                tvIsian.setText("Jawaban Anda : ");
                inputAnswer.setText("");
            }
        }

        if (questions.getPic_question() == null) {
            imgSoal.setVisibility(View.GONE);
        } else {
            imgSoal.setVisibility(View.VISIBLE);
            Picasso.get().load(link + questions.getId_soal())
                    .into(imgSoal);
        }
        if (currentQusetionId == 0) {
            prevSoal.setVisibility(View.INVISIBLE);

        } else if (currentQusetionId + 1 == questionSave.size()) {
            nextSoal.setVisibility(View.INVISIBLE);
            fab2.setVisibility(View.INVISIBLE);
            if (fab.getVisibility() == View.VISIBLE) {
                fab2.setVisibility(View.VISIBLE);
            }
        }

    }

    public void storeAnswer() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(QuizActivity.this);
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
        Call<ResponsePostAnswer> call = RetrofitClient.getInstance().getApi().postQuestion("application/json", token, idsoal,
                responseQuestion);
        call.enqueue(new Callback<ResponsePostAnswer>() {
            @Override
            public void onResponse(Call<ResponsePostAnswer> call, final Response<ResponsePostAnswer> response) {
                ResponsePostAnswer questionResponse = response.body();
                if (response.isSuccessful()) {

                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(QuizActivity.this);
                    SharedPreferences.Editor editorList = sharedPrefs.edit();
                    Gson gson = new Gson();

                    String responsePost = gson.toJson(response.body());
                    editorList.putString("answerPost", responsePost);
                    editorList.apply();

                    progress.dismiss();

                    Intent intent = new Intent(QuizActivity.this, ResultQuizActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("idSoal", response.body().getResult().getQuiz_id());
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
                timer.setText(limitFormat);
            }

            @Override
            public void onFinish() {
                storeAnswer();
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        confirmOnBackPressed();
    }

    private void confirmOnBackPressed() {
        final Dialog dialog = new Dialog(QuizActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_confirm_exit_quiz);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        dialog.getWindow().setLayout((9 * width) / 10, height);

        MaterialButton btnNo = dialog.findViewById(R.id.btnNo);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        MaterialButton btnYes = dialog.findViewById(R.id.btnYes);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QuizActivity.this, Main2Activity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void saveIsian() {

        inputAnswer.clearFocus();
        InputMethodManager in = (InputMethodManager) QuizActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(inputAnswer.getWindowToken(), 0);
        String answer = inputAnswer.getText().toString().trim();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mCtx);
        Gson gson = new Gson();
        SharedPreferences.Editor editorList = sharedPrefs.edit();

        String userAnswer = answer;
        String userAnswerContent = answer;

        String json = sharedPrefs.getString("response", "response");
        Type type = new TypeToken<ResponseQuestion>() {
        }.getType();
        ResponseQuestion responseQuestion = gson.fromJson(json, type);

        String json2 = sharedPrefs.getString("question", "question");
        Type type2 = new TypeToken<ArrayList<QuestionModel>>() {
        }.getType();
        ArrayList<QuestionModel> questionSave = gson.fromJson(json2, type2);


        if (userAnswer.equals("")) {
            editorList.putString("userAnswer", "**");
        } else if (userAnswer.equals("**")) {
            editorList.putString("userAnswer", "**");
            editorList.putString("userAnswerContent", "**");
        } else {
            editorList.putString("userAnswer", userAnswer);
            editorList.putString("userAnswerContent", userAnswer);
        }

        questionSave.get(currentQusetionId).setUser_answer(userAnswer);
        questionSave.get(currentQusetionId).setUser_answer_content(userAnswerContent);

        String questionSt = gson.toJson(questionSave);
        editorList.putString("question", questionSt);

        responseQuestion.setQuestion(questionSave);
        String responseQuiz = gson.toJson(responseQuestion);
        editorList.putString("response", responseQuiz);

        editorList.commit();
        editorList.apply();
        inputAnswer.setText(answer);
        tvIsian.setText("Jawaban Anda : " + answer);

    }

    private void saveNext() {
        saveIsian();
        nextSoal();
    }

    private void savePrev() {
        saveIsian();
        prevSoal();
    }

    private void navSave() {
        QuestionModel questions = questionModel.get(currentQusetionId);
        soal.setText(questions.getQuestion());
        optionModel = questions.getOption();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        if (questions.getType().equals("option")) {
        } else {
            saveIsian();
        }
    }


}
