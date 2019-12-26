package com.komsi.solve;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.komsi.solve.Adapter.NavigationAdapter;
import com.komsi.solve.Adapter.OptionsAdapter;
import com.komsi.solve.Model.QuestionModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class NavigationFragment extends BottomSheetDialogFragment {

    RecyclerView optionRV;
    NavigationAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_navigation, container, false);


        optionRV = fragmentView.findViewById(R.id.optionRV);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("question", "question");
      //  String json = getArguments().getString("question");
        Type type = new TypeToken<ArrayList<QuestionModel>>() {
        }.getType();
        ArrayList<QuestionModel> questionModels = gson.fromJson(json, type);


        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        adapter = new NavigationAdapter(questionModels, getActivity());
        optionRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        optionRV.setLayoutManager(staggeredGridLayoutManager);
        optionRV.setAdapter(adapter);
        return fragmentView;
    }


}
