package com.komsi.solve.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.komsi.solve.Model.HistoryModel;
import com.komsi.solve.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryVH> {
    ArrayList<HistoryModel> historyModels;
    Context mCtx;

    public HistoryAdapter(ArrayList<HistoryModel> historyModels, Context mCtx) {
        this.historyModels = historyModels;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public HistoryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_history, parent, false);

        return new HistoryVH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryVH holder, int position) {
        HistoryModel history = historyModels.get(position);
        holder.quizName.setText(history.getQuiz().getTitle());
        holder.txtDateTime.setText(history.getCreated_at());
        holder.txtScore.setText(history.getTotal_score()+" ");
        holder.txtTrueAns.setText("-");
        holder.txtSumQues.setText(history.getQuiz().getSum_question()+" ");


    }

    @Override
    public int getItemCount() {
        return historyModels.size();
    }



    class HistoryVH extends RecyclerView.ViewHolder {
        private TextView quizName, txtDateTime, txtScore,  txtTrueAns, txtSumQues;
        private ImageView imgLiveReport;
        public HistoryVH(@NonNull View itemView) {
            super(itemView);
            quizName = itemView.findViewById(R.id.quizName);
            txtDateTime = itemView.findViewById(R.id.txtDateTime);
            txtScore = itemView.findViewById(R.id.txtScore);
            txtTrueAns = itemView.findViewById(R.id.txtTrueAns);
            txtSumQues = itemView.findViewById(R.id.txtSumQues);
            imgLiveReport = itemView.findViewById(R.id.imgLiveReport);
        }

    }
}
