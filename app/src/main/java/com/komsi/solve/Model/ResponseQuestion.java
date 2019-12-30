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
    private List<QuestionModel> question = null;

    public ResponseQuestion(String status, TypeListModel quiz, List<QuestionModel> question) {
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

    public List<QuestionModel> getQuestion() {
        return question;
    }

    public void setQuestion(List<QuestionModel> question) {
        this.question = question;
    }
}
