package com.komsi.solve.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.komsi.solve.Model.OptionModel;
import com.komsi.solve.Model.QuestionModel;
import com.komsi.solve.Model.SelectedItem;
import com.komsi.solve.QuizActivity;
import com.komsi.solve.QuizActivity_viewpager;
import com.komsi.solve.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.NavVH> {

    private int mSelectedItem = -1;
    private List<QuestionModel> question;
    private Context mCtx;
    QuizActivity nQuizActivity;
    SelectedItem selectedItem;

    public NavigationAdapter(List<QuestionModel> question, Context mCtx) {
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
    public void onBindViewHolder(@NonNull final NavVH holder, final int position) {
        final QuestionModel questionModel = question.get(position);


        holder.rbChoose.setChecked(position == mSelectedItem);
        holder.jawaban.setText(questionModel.getId_soal() + "");
        holder.placeA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.rbChoose.setChecked(true);
                if (mCtx instanceof QuizActivity) {
                    ((QuizActivity) mCtx).navigationSoal(position);
                }
            }
        });
        if (holder.rbChoose.isChecked()) {
            holder.placeA.setCardBackgroundColor(Color.parseColor("#4f9a94"));

        } else {
            holder.placeA.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        if (holder.id == 1) {

        } else {

        }
    }

    @Override
    public int getItemCount() {
        return question.size();
    }

    class NavVH extends RecyclerView.ViewHolder {
        TextView jawaban;
        CardView placeA;
        RadioButton rbChoose;

        public int id;

        public NavVH(@NonNull final View itemView) {
            super(itemView);
            jawaban = itemView.findViewById(R.id.jawaban);
            rbChoose = itemView.findViewById(R.id.rbChoose);
            placeA = itemView.findViewById(R.id.placeA);

            View.OnClickListener l = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    notifyItemRangeChanged(0, question.size());

                }
            };

           // itemView.setOnClickListener(l);
            rbChoose.setOnClickListener(l);

        }
    }

    public QuestionModel getSelectedNav() {
        if (mSelectedItem != -1) {
            return question.get(mSelectedItem);
        }
        return null;
    }
}
