package id.technow.solve;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import id.technow.solve.Api.RetrofitClient;
import id.technow.solve.Model.QuestionModel;
import id.technow.solve.Model.ResponseQuestion;
import id.technow.solve.Model.UserModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import id.technow.solve.Adapter.ReviewAdapter;
import id.technow.solve.Model.ResponsePostAnswer;

import id.technow.solve.R;

import id.technow.solve.Storage.SharedPrefManager;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity {
    RecyclerView reviewRV;
    ReviewAdapter adapter;
    Context mCtx = ReviewActivity.this;
    ProgressDialog progress;
    String namaSoal;
    int idSoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        idSoal = getIntent().getIntExtra("idSoal", 1);
        namaSoal = getIntent().getStringExtra("namaSoal");
        reviewRV = findViewById(R.id.RVmain);
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        loadAns();

    }

    public void loadAns() {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ReviewActivity.this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("response", "response");
        //  String json = getArguments().getString("question");
        Type type = new TypeToken<ResponseQuestion>() {
        }.getType();
        ResponseQuestion responseQuestion = gson.fromJson(json, type);
        ArrayList<QuestionModel> questionModels = responseQuestion.getQuestion();


        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        adapter = new ReviewAdapter(questionModels, ReviewActivity.this);
        reviewRV.setLayoutManager(new LinearLayoutManager(ReviewActivity.this));
        reviewRV.setLayoutManager(staggeredGridLayoutManager);
        reviewRV.setAdapter(adapter);
        // saveInternal();
    }

    public void saveInternal() {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ReviewActivity.this);
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

        //    Toast.makeText(getApplicationContext(), "Composition saved", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("Exception", "File write failed: " + e.toString());
        }

    }

    public void Submit(View view) {
        // storeAnswer();
        confirmStore();
    }

    public void storeAnswer() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ReviewActivity.this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("response", "response");
        //  String json = getArguments().getString("question");
        Type type = new TypeToken<ResponseQuestion>() {
        }.getType();
        ResponseQuestion responseQuestion = gson.fromJson(json, type);
        ArrayList<QuestionModel> questionModels = responseQuestion.getQuestion();


        progress = ProgressDialog.show(mCtx, null, "Loading ...", true, false);
        UserModel user = SharedPrefManager.getInstance(this).getUser();

        String token = "Bearer " + user.getToken();
        Call<ResponsePostAnswer> call = RetrofitClient.getInstance().getApi().postQuestion("application/json", token,idSoal,
                responseQuestion);
        call.enqueue(new Callback<ResponsePostAnswer>() {
            @Override
            public void onResponse(Call<ResponsePostAnswer> call, final Response<ResponsePostAnswer> response) {
                ResponsePostAnswer questionResponse = response.body();
                if (response.isSuccessful()) {

                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ReviewActivity.this);
                    SharedPreferences.Editor editorList = sharedPrefs.edit();
                    Gson gson = new Gson();

                    String responsePost = gson.toJson(response.body());
                    editorList.putString("answerPost", responsePost);
                    editorList.commit();

                    /*Toast.makeText(mCtx,
                            "Sukses",
                            Toast.LENGTH_LONG).show();*/

                    progress.dismiss();

                    Intent intent = new Intent(ReviewActivity.this, ResultQuizActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("idsoal", response.body().getResult().getQuiz_id());
                    intent.putExtra("namaSoal", namaSoal);
                    editorList.commit();
                    startActivity(intent);

                } else {
                    progress.dismiss();
                    Log.i("debug", "Failed " + response.errorBody() + " ");

               //     Toast.makeText(mCtx, response.code() + " " + response.message() + response.errorBody(),
                            //R.string.something_wrong,
                 //           Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ResponsePostAnswer> call, Throwable t) {
                Log.i("debug", "onResponse : FAILED");
                progress.dismiss();
                Toast.makeText(mCtx,
                                "Quiz Tidak Dapat Diakses",
                        //R.string.something_wrong + t.toString(),
                        Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void confirmStore() {
        final Dialog dialog = new Dialog(ReviewActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_confirm_save);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        dialog.getWindow().setLayout((9 * width) / 10, (4 * height) / 5);

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
                storeAnswer();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
