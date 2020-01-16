package id.technow.solve.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import id.technow.solve.Model.QuestionModel;
import com.technow.solve.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewVH>{
    private ArrayList<QuestionModel> question;
    private Context mCtx;

    public ReviewAdapter(ArrayList<QuestionModel> question, Context mCtx) {
        this.question = question;
        this.mCtx = mCtx;
    }
    @NonNull
    @Override
    public ReviewVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_review_soal, parent, false);
        ReviewAdapter.ReviewVH holder = new ReviewAdapter.ReviewVH(view);
        return new ReviewAdapter.ReviewVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewVH holder, int position) {
        final QuestionModel questionModel = question.get(position);
        holder.tvSoal.setText(questionModel.getQuestion());
        if(questionModel.getUser_answer().equals("**")){
            holder.tvAns.setText("| -");
        }
        else {
            holder.tvAns.setText("| "+ questionModel.getUser_answer_content());

        }
        int number = position +1;
        holder.tvNum.setText(number+"");

    }

    @Override
    public int getItemCount() {
        return question.size();
    }

    class ReviewVH extends RecyclerView.ViewHolder {
        TextView tvSoal,tvAns, tvNum;
        public ReviewVH(@NonNull View itemView) {
            super(itemView);
            tvSoal = itemView.findViewById(R.id.tvSoal);
            tvAns = itemView.findViewById(R.id.tvAns);
            tvNum = itemView.findViewById(R.id.tvNum);


        }
    }
}
