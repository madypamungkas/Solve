package id.technow.solve.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import id.technow.solve.Model.AnswerSaveHDModel;
import id.technow.solve.Model.QuestionModel;
import id.technow.solve.R;

public class HistoryDetailAdapter extends RecyclerView.Adapter<HistoryDetailAdapter.HistoryDetailVH> {
    ArrayList<AnswerSaveHDModel> answerSaveHDModels;
    Context mCtx;
    String typeGame;

    public HistoryDetailAdapter(ArrayList<AnswerSaveHDModel> answerSaveHDModels, Context mCtx, String typeGame) {
        this.answerSaveHDModels = answerSaveHDModels;
        this.mCtx = mCtx;
        this.typeGame = typeGame;
    }

    @NonNull
    @Override
    public HistoryDetailVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_answer, parent, false);
        return new HistoryDetailVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryDetailVH holder, int position) {
        final AnswerSaveHDModel answer = answerSaveHDModels.get(position);

        int num = position+1;
       // holder.typeGame.setText(responseQuestion.getQuiz().getTitle());

        holder.num.setText(num+" ");
        holder.desc.setText(answer.getQuestion());
        holder.answer.setText(answer.getTrueAnswerContent());
        holder.userAnswer.setText(answer.getUser_answer_content());

        if(answer.getUser_answer().equals(answer.getTrueAnswer())){
            holder.cardGame.setCardBackgroundColor(Color.parseColor("#64b5f6"));
            holder.status.setText("Benar");
        }else {
            holder.cardGame.setCardBackgroundColor(Color.parseColor("#545454"));
            holder.status.setText("Salah");
        }
    }

    @Override
    public int getItemCount() {
        return answerSaveHDModels.size();
    }

    class HistoryDetailVH extends RecyclerView.ViewHolder {
        TextView typeGame, desc, num, answer, userAnswer, status;
        LinearLayout btnQuiz;
        CardView cardGame;
        ImageView imgGame;
        public HistoryDetailVH(@NonNull View itemView) {
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
