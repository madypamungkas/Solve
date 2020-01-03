package com.komsi.solve.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class QuestionModel {
    @SerializedName("id")
    @Expose
    private int id_soal;

    @SerializedName("question")
    @Expose
    private String question;

    @SerializedName("pic_question")
    @Expose
    private String pic_question;
    @SerializedName("duration")
    @Expose
    private String duration;

    @SerializedName("trueAnswer")
    @Expose
    private String trueAnswer;

    @SerializedName("trueAnswerPic")
    @Expose
    private String trueAnswerPic;

    @SerializedName("user_answer")
    @Expose
    private String user_answer;

    @SerializedName("option")
    @Expose
    private List<OptionModel> option = null;

    public QuestionModel(int id_soal, String question, String pic_question, String duration, String trueAnswer, String trueAnswerPic, String user_answer, List<OptionModel> option) {
        this.id_soal = id_soal;
        this.question = question;
        this.pic_question = pic_question;
        this.duration = duration;
        this.trueAnswer = trueAnswer;
        this.trueAnswerPic = trueAnswerPic;
        this.user_answer = user_answer;
        this.option = option;
    }

    public int getId_soal() {
        return id_soal;
    }

    public String getQuestion() {
        return question;
    }

    public String getPic_question() {
        return pic_question;
    }

    public String getDuration() {
        return duration;
    }

    public String getTrueAnswer() {
        return trueAnswer;
    }

    public String getTrueAnswerPic() {
        return trueAnswerPic;
    }

    public String getUser_answer() {
        return user_answer;
    }

    public List<OptionModel> getOption() {
        return option;
    }

    public void setUser_answer(String user_answer) {
        this.user_answer = user_answer;
    }

    public void setOption(List<OptionModel> option) {
        this.option = option;
    }
}
