package com.komsi.solve.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.komsi.solve.Model.OptionModel;
import com.komsi.solve.Model.QuestionModel;
import com.komsi.solve.Model.SchoolsModel;
import com.komsi.solve.R;
import com.komsi.solve.RegisterFragment;
import com.komsi.solve.SchoolsSearchFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class SchoolAdapter extends RecyclerView.Adapter<SchoolAdapter.SchoolVH>  {

    private List<SchoolsModel> schoolsModels;
    private Context mCtx;
    SchoolsSearchFragment schoolsSearchFragment;

    public SchoolAdapter(List<SchoolsModel> schoolsModels, Context mCtx, SchoolsSearchFragment schoolsSearchFragment) {
        this.schoolsModels = schoolsModels;
        this.mCtx = mCtx;
        this.schoolsSearchFragment = schoolsSearchFragment;
    }

    @NonNull
    @Override
    public SchoolVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_schools, parent, false);
        SchoolVH holder = new SchoolVH(view);
        return new SchoolVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SchoolVH holder, int position) {
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mCtx);
        final Gson gson = new Gson();
        final SharedPreferences.Editor editorList = sharedPrefs.edit();
        final SchoolsModel schools = schoolsModels.get(position);

        holder.jawaban.setText(schools.getText() + "");
        holder.placeA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editorList.putString("schoolName", schools.getText());
                editorList.putString("schoolId", schools.getId());
                editorList.commit();


            }
        });
    }
    @Override
    public int getItemCount() {
        return schoolsModels.size();
    }

    class SchoolVH extends RecyclerView.ViewHolder {
        TextView jawaban;
        RadioButton rbChoose;
        public int id;
        CardView placeA;
        ImageView imgOption;

        public SchoolVH(@NonNull View itemView) {
            super(itemView);
            jawaban = itemView.findViewById(R.id.jawaban);
            rbChoose = itemView.findViewById(R.id.rbChoose);
            imgOption = itemView.findViewById(R.id.imgOption);
            placeA = itemView.findViewById(R.id.placeA);

        }
    }
}
