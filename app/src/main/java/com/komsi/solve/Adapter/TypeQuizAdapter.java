package com.komsi.solve.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.komsi.solve.Model.TypeListModel;
import com.komsi.solve.QuizActivity;
import com.komsi.solve.QuizActivity_viewpager;
import com.komsi.solve.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TypeQuizAdapter extends RecyclerView.Adapter<TypeQuizAdapter.QuizVH> {

    private ArrayList<TypeListModel> type;
    private Context mCtx;

    public TypeQuizAdapter(ArrayList<TypeListModel> type, Context mCtx) {
        this.type = type;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public QuizVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_quiz_choose, parent, false);
        TypeQuizAdapter.QuizVH holder = new TypeQuizAdapter.QuizVH(view);
        return new TypeQuizAdapter.QuizVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizVH holder, int position) {
        final TypeListModel types = type.get(position);

        holder.titleMenu.setText(types.getTitle()+" ");
        holder.tvSum.setText(types.getDescription()+"\n"+"Jumlah Soal : " +types.getSum_question());
        holder.layoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mCtx);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.clear();
                editor.commit();

                Intent i = new Intent(mCtx, QuizActivity.class);
                i.putExtra("idCategory", types.getId());
                i.putExtra("Type", types.getPic_url());

                mCtx.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return type.size();
    }

    class QuizVH extends RecyclerView.ViewHolder {
        TextView titleMenu, tvSum;
        LinearLayout layoutCard;
        FrameLayout.LayoutParams params;

        public QuizVH(@NonNull View itemView) {
            super(itemView);
            titleMenu = itemView.findViewById(R.id.titleMenu);
            // layoutCard = itemView.findViewById(R.id.layoutCard);
            tvSum = itemView.findViewById(R.id.tvSum);
            layoutCard = itemView.findViewById(R.id.layoutCard);
//            params = (FrameLayout.LayoutParams) layoutCard.getLayoutParams();

        }
    }
}
