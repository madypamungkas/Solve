package com.komsi.solve.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.komsi.solve.ChangeProfile;
import com.komsi.solve.Model.OptionModel;
import com.komsi.solve.Model.QuestionModel;
import com.komsi.solve.Model.SchoolsModel;
import com.komsi.solve.QuizActivity_viewpager;
import com.komsi.solve.R;
import com.komsi.solve.RegisterActivity;
import com.komsi.solve.RegisterFragment;
import com.komsi.solve.SchoolsSearchFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class SchoolAdapter extends RecyclerView.Adapter<SchoolAdapter.SchoolVH> {
    private int mSelectedItem = -1;
    private List<SchoolsModel> schoolsModels;
    private Context mCtx;
    SchoolsSearchFragment schoolsSearchFragment;
    RegisterFragment registerFragment;

    private static final String TAG = "Register";

    public SchoolAdapter(List<SchoolsModel> schoolsModels, Context mCtx, SchoolsSearchFragment schoolsSearchFragment, RegisterFragment registerFragment) {
        this.schoolsModels = schoolsModels;
        this.mCtx = mCtx;
        this.schoolsSearchFragment = schoolsSearchFragment;
        this.registerFragment = registerFragment;
    }

    @NonNull
    @Override
    public SchoolVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_schools, parent, false);
        SchoolVH holder = new SchoolVH(view);
        return new SchoolVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SchoolVH holder, int position) {
        final SchoolsModel schools = schoolsModels.get(position);

        holder.jawaban.setText(schools.getText() + "");
        if(position == mSelectedItem){
            holder.placeA.setCardBackgroundColor(Color.parseColor("#545454"));
            if (mCtx instanceof RegisterActivity) {
                ((RegisterActivity) mCtx).setTextSch(schools.getText(), schools.getId());
            }
            if (mCtx instanceof ChangeProfile) {
                ((ChangeProfile) mCtx).setTextSch(schools.getText(), schools.getId());
            }
        }
        else{
         }

    }

    @Override
    public int getItemCount() {
        return schoolsModels.size();
    }

    class SchoolVH extends RecyclerView.ViewHolder {
        TextView jawaban;
        RadioButton rbChoose;
        public int id;
        public String school;
        CardView placeA;
        ImageView imgOption;

        public SchoolVH(@NonNull View itemView) {
            super(itemView);
            jawaban = itemView.findViewById(R.id.jawaban);
            rbChoose = itemView.findViewById(R.id.rbChoose);
            imgOption = itemView.findViewById(R.id.imgOption);
            placeA = itemView.findViewById(R.id.placeA);

            View.OnClickListener l = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    notifyItemRangeChanged(0, schoolsModels.size());
                    notifyDataSetChanged();

                }
            };

            rbChoose.setOnClickListener(l);
            itemView.setOnClickListener(l);
            placeA.setOnClickListener(l);
        }
    }


}
