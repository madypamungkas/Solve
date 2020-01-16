package com.komsi.solve.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.komsi.solve.Model.QuestionModel;
import com.komsi.solve.Model.ResponseQuestion;
import com.komsi.solve.QuizActivity;
import com.komsi.solve.QuizActivity_viewpager;
import com.komsi.solve.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class PembahasanAdapter extends RecyclerView.Adapter<PembahasanAdapter.VH>{
    Context mCtx;
    ArrayList<QuestionModel> questionModels;
    List<String> colors;

    public PembahasanAdapter(Context mCtx, ArrayList<QuestionModel> questionModels) {
        this.mCtx = mCtx;
        this.questionModels = questionModels;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_answer, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        final QuestionModel answer = questionModels.get(position);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mCtx);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("response", "response");
        Type type = new TypeToken<ResponseQuestion>() {
        }.getType();
        ResponseQuestion responseQuestion = gson.fromJson(json, type);

        ArrayList<QuestionModel> questionModels = responseQuestion.getQuestion();

        int num = position+1;
        holder.typeGame.setText(responseQuestion.getQuiz().getTitle());
        holder.num.setText(num+" ");
        holder.desc.setText(answer.getQuestion());
        holder.answer.setText(answer.getTrueAnswerContent());
        holder.userAnswer.setText(answer.getUser_answer_content());

        if(answer.getUser_answer().equals(answer.getTrueAnswer())){
            holder.cardGame.setCardBackgroundColor(Color.parseColor("#64b5f6"));
            holder.status.setText("Benar");
        }else {
            holder.cardGame.setCardBackgroundColor(Color.parseColor("#FF424242"));
            holder.status.setText("Salah");
        }

    }

    @Override
    public int getItemCount() {
        return questionModels.size();
    }

    class VH extends RecyclerView.ViewHolder {
        TextView typeGame, desc, num, answer, userAnswer, status;
        LinearLayout btnQuiz;
        CardView cardGame;
        ImageView imgGame;

        public VH(@NonNull View itemView) {
            super(itemView);
            cardGame = itemView.findViewById(R.id.cardGame);
            typeGame = itemView.findViewById(R.id.typeGame);
            desc = itemView.findViewById(R.id.desc);
            num = itemView.findViewById(R.id.num);
            answer = itemView.findViewById(R.id.answer);
            status = itemView.findViewById(R.id.status);
            userAnswer = itemView.findViewById(R.id.userAnswer);
            btnQuiz = itemView.findViewById(R.id.btnQuiz);
            imgGame = itemView.findViewById(R.id.imgGame);
        }
    }
}
