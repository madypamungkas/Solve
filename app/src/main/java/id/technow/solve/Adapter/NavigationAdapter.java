package id.technow.solve.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import id.technow.solve.Model.QuestionModel;
import id.technow.solve.Model.ResponseQuestion;
import id.technow.solve.Model.SelectedItem;
import id.technow.solve.QuizActivity;
import id.technow.solve.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.NavVH> {

    private List<QuestionModel> question;
    private Context mCtx;

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

       /* final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mCtx);
        final Gson gson = new Gson();
        final SharedPreferences.Editor editorList = sharedPrefs.edit();
        holder.jawaban.setText((position + 1) + "");
        String json = sharedPrefs.getString("response", "response");
        Type type = new TypeToken<ResponseQuestion>() {
        }.getType();
        ResponseQuestion responseQuestion = gson.fromJson(json, type);

        String json2 = sharedPrefs.getString("question", "question");
        Type type2 = new TypeToken<ArrayList<QuestionModel>>() {
        }.getType();
        ArrayList<QuestionModel> questionSave = gson.fromJson(json2, type2);
*/
/*
        if (questionModel.getUser_answer_content() != "**") {
            holder.placeA.setCardBackgroundColor(Color.parseColor("#64b5f6"));
        }
*/

        if (questionModel.getUser_answer().equals("**")) {
            holder.placeA.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            holder.placeA.setCardBackgroundColor(Color.parseColor("#64b5f6"));

        }
        holder.jawaban.setText((position+1)+" ");
        //holder.jawaban.setText(questionModel.getUser_answer_content());

        holder.placeA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCtx instanceof QuizActivity) {
                    ((QuizActivity) mCtx).navigationSoal(position);
                    holder.rbNav.setChecked(true);

                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return question.size();
    }

    class NavVH extends RecyclerView.ViewHolder {
        TextView jawaban;
        CardView placeA;
        RadioButton rbNav;

        public int id;

        public NavVH(@NonNull final View itemView) {
            super(itemView);
            jawaban = itemView.findViewById(R.id.jawaban);
            rbNav = itemView.findViewById(R.id.rbNav);
            placeA = itemView.findViewById(R.id.placeA);

          /*  View.OnClickListener l = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    notifyItemRangeChanged(0, question.size());
                    notifyDataSetChanged();
                }
            };

            itemView.setOnClickListener(l);
            //rbNav.setOnClickListener(l);*/

        }
    }


}
