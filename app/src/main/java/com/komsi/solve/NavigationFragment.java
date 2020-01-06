package com.komsi.solve;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.komsi.solve.Adapter.NavigationAdapter;
import com.komsi.solve.Adapter.OptionsAdapter;
import com.komsi.solve.Adapter.QuizViewPagerAdapter;
import com.komsi.solve.Api.RetrofitClient;
import com.komsi.solve.Model.QuestionModel;
import com.komsi.solve.Model.ResponseQuestion;
import com.komsi.solve.Model.UserModel;
import com.komsi.solve.Storage.SharedPrefManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class NavigationFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    RecyclerView optionRV;
    NavigationAdapter adapter;
    Button readyBtn;

    List<QuestionModel> questionModels;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_navigation, container, false);


        readyBtn = fragmentView.findViewById(R.id.readyBtn);
        optionRV = fragmentView.findViewById(R.id.optionRV);

        readyBtn.setOnClickListener(this);
        loadSoal();
        return fragmentView;
    }

    public void loadSoal() {

        UserModel user = SharedPrefManager.getInstance(getActivity()).getUser();

        String token = "Bearer " + user.getToken();
        Call<ResponseQuestion> call = RetrofitClient.getInstance().getApi().question("application/json", token, 1);
        call.enqueue(new Callback<ResponseQuestion>() {
            @Override
            public void onResponse(Call<ResponseQuestion> call, final Response<ResponseQuestion> response) {
                ResponseQuestion questionResponse = response.body();
                if (response.isSuccessful()) {
                    if (response.body().getQuestion().size() != 0) {
                        questionModels = questionResponse.getQuestion();
                        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
                        adapter = new NavigationAdapter(questionModels, getActivity());
                        optionRV.setLayoutManager(new LinearLayoutManager(getActivity()));
                        optionRV.setLayoutManager(staggeredGridLayoutManager);
                        optionRV.setAdapter(adapter);
                    } else {
                    }
                } else {
                    Log.i("debug", "onResponse : FAILED");
                    Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseQuestion> call, Throwable t) {
                Log.i("debug", "onResponse : FAILED");
                Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.readyBtn:
                Intent readyBtn = new Intent(getActivity(), ReviewActivity.class);
                startActivity(readyBtn);
                break;
        }
    }
}
