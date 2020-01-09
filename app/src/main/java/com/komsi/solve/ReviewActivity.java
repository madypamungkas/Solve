package com.komsi.solve;

import androidx.appcompat.app.AppCompatActivity;
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
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.komsi.solve.Adapter.NavigationAdapter;
import com.komsi.solve.Adapter.ReviewAdapter;
import com.komsi.solve.Api.RetrofitClient;
import com.komsi.solve.Model.QuestionModel;
import com.komsi.solve.Model.ResponsePostAnswer;
import com.komsi.solve.Model.ResponseQuestion;
import com.komsi.solve.Model.UserModel;
import com.komsi.solve.Storage.SharedPrefManager;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity {
    RecyclerView reviewRV;
    ReviewAdapter adapter;
    Context mCtx = ReviewActivity.this;
    ProgressDialog progress;

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

            Toast.makeText(getApplicationContext(), "Composition saved", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("Exception", "File write failed: " + e.toString());
        }

/*        File file = new File(Environment.getExternalStorageDirectory(),
                "Report.pdf");
        Uri path = Uri.fromFile(file);*/
    }

    public void Submit(View view) {
        storeAnswer();

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
        Call<ResponsePostAnswer> call = RetrofitClient.getInstance().getApi().postQuestion("application/json", token, 1,
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

                    Toast.makeText(mCtx,
                            "Sukses",
                            Toast.LENGTH_LONG).show();
                    //saveInternal();
                    progress.dismiss();

                    Intent intent = new Intent(ReviewActivity.this, ResultQuizActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(intent);

                } else {
                    progress.dismiss();
                    Log.i("debug", "Failed "+response.errorBody()+" ");

                    Toast.makeText(mCtx, response.code() + " "+ response.message() + response.errorBody(),
                            //R.string.something_wrong,
                            Toast.LENGTH_LONG).show();

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

            }
        });
    }

}
