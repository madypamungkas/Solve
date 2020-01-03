package com.komsi.solve.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OptionModel {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("question_id")
    @Expose
    private String question_id;

    @SerializedName("option")
    @Expose
    private String option;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("pic_url")
    @Expose
    private String pic_url;

    @SerializedName("isTrue")
    @Expose
    private String isTrue;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    @SerializedName("deleted_at")
    @Expose
    private String deleted_at;
    @SerializedName("choosen")
    @Expose
    private String choosen;

    public OptionModel(int id, String question_id, String option, String content, String pic_url, String isTrue, String created_at, String updated_at, String deleted_at, String choosen) {
        this.id = id;
        this.question_id = question_id;
        this.option = option;
        this.content = content;
        this.pic_url = pic_url;
        this.isTrue = isTrue;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.choosen = choosen;
    }

    public int getId() {
        return id;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public String getOption() {
        return option;
    }

    public String getContents() {
        return content;
    }

    public String getPic_url() {
        return pic_url;
    }

    public String getIsTrue() {
        return isTrue;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public String getChoosen() {
        return choosen;
    }

    public void setChoosen(String choosen) {
        this.choosen = choosen;
    }
}
