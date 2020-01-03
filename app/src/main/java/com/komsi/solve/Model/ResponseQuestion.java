package com.komsi.solve.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ResponseQuestion {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("quiz")
    @Expose
    private TypeListModel quiz;

    @SerializedName("question")
    @Expose
    private ArrayList<QuestionModel> question = null;

    public ResponseQuestion(String status, TypeListModel quiz, ArrayList<QuestionModel> question) {
        this.status = status;
        this.quiz = quiz;
        this.question = question;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TypeListModel getQuiz() {
        return quiz;
    }

    public void setQuiz(TypeListModel quiz) {
        this.quiz = quiz;
    }

    public ArrayList<QuestionModel> getQuestion() {
        return question;
    }

    public void setQuestion(ArrayList<QuestionModel> question) {
        this.question = question;
    }
}
