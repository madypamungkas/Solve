package com.komsi.solve.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.komsi.solve.Model.OptionModel;
import com.komsi.solve.Model.QuestionModel;
import com.komsi.solve.Model.ResponseQuestion;
import com.komsi.solve.QuizActivity;
import com.komsi.solve.R;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Response;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.OptionVH> {
    private int mSelectedItem = -1;
    private List<OptionModel> optionModel;
    private Context mCtx;
    private QuestionModel question;
    String link = "http://10.33.85.59/solve/solve-jst/public/storage/answer/";
    String content;

    public OptionsAdapter(List<OptionModel> optionModel, Context mCtx, QuestionModel question) {
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
//        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mCtx);
//        Gson gson = new Gson();
//        String json = sharedPrefs.getString("question", "question");
//        Type type = new TypeToken<ArrayList<QuestionModel>>() {
//        }.getType();
//        ArrayList<QuestionModel> questionModels = gson.fromJson(json, type);
//

        final OptionModel option = optionModel.get(position);

        content = option.getContents();
        Picasso.get().load(link + option.getId())
                .into(holder.imgOption);

        holder.rbChoose.setChecked(position == mSelectedItem);
        holder.jawaban.setText(option.getContents() + " ");


        if (option.getChoosen() == 1) {
        //    holder.rbChoose.setChecked(true);
        } else {
        }

       // int num = sharedPrefs.getInt("num", 0);
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
        return optionModel.size();
    }

    class OptionVH extends RecyclerView.ViewHolder {
        TextView jawaban;
        RadioButton rbChoose;
        public int id;
        CardView placeA;
        ImageView imgOption;

        public OptionVH(@NonNull View itemView) {
            super(itemView);
            jawaban = itemView.findViewById(R.id.jawaban);
            rbChoose = itemView.findViewById(R.id.rbChoose);
            imgOption = itemView.findViewById(R.id.imgOption);
            placeA = itemView.findViewById(R.id.placeA);

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
