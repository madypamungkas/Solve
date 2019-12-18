package com.komsi.solve.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListSoalModel {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("sum_question")
    @Expose
    private int sum_question;

    @SerializedName("pic_url")
    @Expose
    private String pic_url;

    public ListSoalModel(int id, int sum_question, String pic_url) {
        this.id = id;
        this.sum_question = sum_question;
        this.pic_url = pic_url;
    }

    public int getId() {
        return id;
    }

    public int getSum_question() {
        return sum_question;
    }

    public String getPic_url() {
        return pic_url;
    }
}
