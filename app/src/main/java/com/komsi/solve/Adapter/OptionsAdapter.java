package com.komsi.solve.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.komsi.solve.Model.OptionModel;
import com.komsi.solve.Model.QuestionModel;
import com.komsi.solve.Model.ResponseQuestion;
import com.komsi.solve.QuizActivity;
import com.komsi.solve.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Response;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.OptionVH> {
    private int mSelectedItem = -1;
    private ArrayList<OptionModel> optionModel;
    private Context mCtx;
    private QuestionModel question;

    public OptionsAdapter(ArrayList<OptionModel> optionModel, Context mCtx, QuestionModel question) {
        this.optionModel = optionModel;
        this.mCtx = mCtx;
        this.question = question;
    }

    @NonNull
    @Override
    public OptionVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_option, parent, false);
        OptionsAdapter.OptionVH holder = new OptionsAdapter.OptionVH(view);
        return new OptionsAdapter.OptionVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionVH holder, int position) {
        final OptionModel option = optionModel.get(position);

        holder.rbChoose.setChecked(position == mSelectedItem);
        holder.jawaban.setText(option.getContents() + " ");
        

       /* if(option.getChoosen() == 0){
            holder.rbChoose.setChecked(false);
        }
        else {
            holder.rbChoose.setChecked(true);
        }*/
        if(!holder.rbChoose.isChecked()){
            option.setChoosen(0);
        }else {
            option.setChoosen(1);
        }
        if (holder.id == 1) {
            QuestionModel questionModel = null;
            question.setUser_answer(option.getContents());
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mCtx);
            SharedPreferences.Editor editorList = sharedPrefs.edit();
            Gson gson = new Gson();

            String json = gson.toJson(questionModel);

            editorList.putString("question", json);
            editorList.commit();

        } else {

        }
    }

    @Override
    public int getItemCount() {
        return optionModel.size();
    }

    class OptionVH extends RecyclerView.ViewHolder {
        TextView jawaban;
        RadioButton rbChoose;
        public int id;

        public OptionVH(@NonNull View itemView) {
            super(itemView);
            jawaban = itemView.findViewById(R.id.jawaban);
            rbChoose = itemView.findViewById(R.id.rbChoose);

            View.OnClickListener l = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    notifyItemRangeChanged(0, optionModel.size());

                }
            };

            itemView.setOnClickListener(l);
            rbChoose.setOnClickListener(l);

        }
    }

    public OptionModel getSelected() {
        if (mSelectedItem != -1) {
            return optionModel.get(mSelectedItem);
        }
        return null;
    }
}
