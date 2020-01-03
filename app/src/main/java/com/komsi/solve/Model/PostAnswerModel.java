package com.komsi.solve.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.komsi.solve.UserAnswerModel;

import java.util.ArrayList;

public class PostAnswerModel {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("quiz_id")
    @Expose
    private String quiz_id;

    @SerializedName("collager_id")
    @Expose
    private String collager_id;

    @SerializedName("total_score")
    @Expose
    private String total_score;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;
    @SerializedName("answer_save")
    @Expose
    private ArrayList<UserAnswerModel> answer_save;

    public PostAnswerModel(int id, String quiz_id, String collager_id, String total_score, String created_at, String updated_at, ArrayList<UserAnswerModel> answer_save) {
        this.id = id;
        this.quiz_id = quiz_id;
        this.collager_id = collager_id;
        this.total_score = total_score;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.answer_save = answer_save;
    }

    public int getId() {
        return id;
    }

    public String getQuiz_id() {
        return quiz_id;
    }

    public String getCollager_id() {
        return collager_id;
    }

    public String getTotal_score() {
        return total_score;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public ArrayList<UserAnswerModel> getAnswer_save() {
        return answer_save;
    }
}
