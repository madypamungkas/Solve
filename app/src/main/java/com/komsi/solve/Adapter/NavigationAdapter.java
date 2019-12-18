package com.komsi.solve.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import com.komsi.solve.Model.QuestionModel;
import com.komsi.solve.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.NavVH>{

    private int mSelectedItem = -1;
    private ArrayList<QuestionModel> question;
    private Context mCtx;

    public NavigationAdapter(ArrayList<QuestionModel> question, Context mCtx) {
        this.question = question;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public NavVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_navigation, parent, false);
        NavigationAdapter.NavVH holder = new NavigationAdapter.NavVH(view);
        return new NavigationAdapter.NavVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NavVH holder, int position) {
        final QuestionModel questionModel = question.get(position);

        holder.rbChoose.setChecked(position == mSelectedItem);
        holder.jawaban.setText(questionModel.getId_soal()+"");

    }

    @Override
    public int getItemCount() {
        return question.size();
    }

    class NavVH extends RecyclerView.ViewHolder {
        TextView jawaban;
        RadioButton rbChoose;
        public int id;
        public NavVH(@NonNull View itemView) {
            super(itemView);
            jawaban = itemView.findViewById(R.id.jawaban);
            rbChoose = itemView.findViewById(R.id.rbChoose);
        }
    }
}
