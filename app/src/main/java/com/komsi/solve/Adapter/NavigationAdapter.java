package com.komsi.solve.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.komsi.solve.Model.QuestionModel;
import com.komsi.solve.Model.SelectedItem;
import com.komsi.solve.QuizActivity;
import com.komsi.solve.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.NavVH> {

    private int mSelectedItem = -1;
    private ArrayList<QuestionModel> question;
    private Context mCtx;
    QuizActivity nQuizActivity;
    SelectedItem selectedItem;

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

        // holder.rbChoose.setChecked(position == mSelectedItem);
        holder.jawaban.setText(questionModel.getId_soal() + "");
        if (questionModel.getUser_answer() == null) {

        } else {
            holder.placeA.setCardBackgroundColor(Color.parseColor("#4f9a94"));
        }
        /*holder.placeA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nQuizActivity.navigationSoal();
            }
        });
*/

    }

    @Override
    public int getItemCount() {
        return question.size();
    }

    class NavVH extends RecyclerView.ViewHolder {
        TextView jawaban;
        CardView placeA;
//        RadioButton rbChoose;

        public int id;

        public NavVH(@NonNull final View itemView) {
            super(itemView);
            jawaban = itemView.findViewById(R.id.jawaban);
            //rbChoose = itemView.findViewById(R.id.rbChoose);
            placeA = itemView.findViewById(R.id.placeA);
            View.OnClickListener l = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    notifyItemRangeChanged(0, question.size());
                    nQuizActivity.num(mSelectedItem+1);
                }
            };

            itemView.setOnClickListener(l);
            // rbChoose.setOnClickListener(l);

        }
    }

    public int getSelectedItem() {
        return mSelectedItem + 2;
    }

    public QuestionModel getSelected() {
        if (mSelectedItem != -1) {
            return question.get(mSelectedItem);
        }
        return null;
    }
}
