package id.technow.solve.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseQuestion {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("status_review")
    @Expose
    private String status_review;


    @SerializedName("quiz")
    @Expose
    private TypeListModel quiz;

    @SerializedName("question")
    @Expose
    private ArrayList<QuestionModel> question = null;

    public ResponseQuestion(String status, String status_review, TypeListModel quiz, ArrayList<QuestionModel> question) {
        this.status = status;
        this.status_review = status_review;
        this.quiz = quiz;
        this.question = question;
    }

    public String getStatus() {
        return status;
    }

    public String getStatus_review() {
        return status_review;
    }

    public TypeListModel getQuiz() {
        return quiz;
    }

    public ArrayList<QuestionModel> getQuestion() {
        return question;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStatus_review(String status_review) {
        this.status_review = status_review;
    }

    public void setQuiz(TypeListModel quiz) {
        this.quiz = quiz;
    }

    public void setQuestion(ArrayList<QuestionModel> question) {
        this.question = question;
    }
}
