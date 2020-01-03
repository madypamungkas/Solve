package com.komsi.solve;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAnswerModel {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("quiz_collager_id")
    @Expose
    private String quiz_collager_id;

    @SerializedName("question_id")
    @Expose
    private String collager_id;

    @SerializedName("collager_answer")
    @Expose
    private String collager_answer;
    @SerializedName("isTrue")
    @Expose
    private String isTrue;

    @SerializedName("score")
    @Expose
    private String score;

    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    public UserAnswerModel(int id, String quiz_collager_id, String collager_id, String collager_answer, String isTrue, String score, String created_at, String updated_at) {
        this.id = id;
        this.quiz_collager_id = quiz_collager_id;
        this.collager_id = collager_id;
        this.collager_answer = collager_answer;
        this.isTrue = isTrue;
        this.score = score;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public String getQuiz_collager_id() {
        return quiz_collager_id;
    }

    public String getCollager_id() {
        return collager_id;
    }

    public String getCollager_answer() {
        return collager_answer;
    }

    public String getIsTrue() {
        return isTrue;
    }

    public String getScore() {
        return score;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
