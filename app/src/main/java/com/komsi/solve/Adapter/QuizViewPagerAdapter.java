package com.komsi.solve.Adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.komsi.solve.Model.QuestionModel;
import com.komsi.solve.QuizActivity;
import com.komsi.solve.QuizActivity_viewpager;
import com.komsi.solve.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.PagerAdapter;

public class QuizViewPagerAdapter extends PagerAdapter {
    private List<QuestionModel> question;
    private Context mCtx;
    private LayoutInflater layoutInflater;
    OptionsAdapter adapter;

    public QuizViewPagerAdapter(List<QuestionModel> question, Context mCtx) {
        this.question = question;
        this.mCtx = mCtx;
    }

    @Override
    public int getCount() {
        return question.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(mCtx);
        View view = layoutInflater.inflate(R.layout.viewpager_quiz, container, false);
        final TextView soal, gameName,sum,number, timer;
        ImageView nextSoal, prevSoal, imgSoal;
        RecyclerView optionRV;

        optionRV = view.findViewById(R.id.optionRV);
        soal = view.findViewById(R.id.soal);
        gameName = view.findViewById(R.id.gameName);
        sum = view.findViewById(R.id.sum);
        number = view.findViewById(R.id.number);
        timer = view.findViewById(R.id.timer);
        nextSoal = view.findViewById(R.id.nextSoal);
        prevSoal = view.findViewById(R.id.prevSoal);
        imgSoal = view.findViewById(R.id.imgSoal);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        adapter = new OptionsAdapter(question.get(position).getOption(), mCtx, question.get(position));
        optionRV.setLayoutManager(new LinearLayoutManager(mCtx));
        optionRV.setLayoutManager(staggeredGridLayoutManager);
        optionRV.setAdapter(adapter);

        int num = position+1;
        soal.setText(question.get(position).getQuestion());
        //gameName.setText(question.get(position).get());
        number.setText(num+"/");
        sum.setText(question.size()+" ");


        if(position == 0){
            prevSoal.setVisibility(View.GONE);
        }
        if (position == question.size()-1){
            nextSoal.setVisibility(View.GONE);
        }
        nextSoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCtx instanceof QuizActivity_viewpager) {
                    ((QuizActivity_viewpager)mCtx).viewPager.setCurrentItem(position+1);
                }
            }
        });
        prevSoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCtx instanceof QuizActivity_viewpager) {
                    ((QuizActivity_viewpager)mCtx).viewPager.setCurrentItem(position-1);
                }
            }
        });

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
