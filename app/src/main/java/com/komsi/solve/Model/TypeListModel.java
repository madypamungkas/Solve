package com.komsi.solve.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TypeListModel {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("quiz_type_id")
    @Expose
    private int quiz_type_id;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("pic_url")
    @Expose
    private String pic_url;

    @SerializedName("sum_question")
    @Expose
    private String sum_question;

    @SerializedName("tot_visible")
    @Expose
    private String tot_visible;

    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("quiz_category_id")
    @Expose
    private String quiz_category_id;

    @SerializedName("name")
    @Expose
    private String name;


    public TypeListModel(int id, int quiz_type_id, String code, String title, String description, String pic_url, String sum_question, String tot_visible, String time, String quiz_category_id, String name) {
        this.id = id;
        this.quiz_type_id = quiz_type_id;
        this.code = code;
        this.title = title;
        this.description = description;
        this.pic_url = pic_url;
        this.sum_question = sum_question;
        this.tot_visible = tot_visible;
        this.time = time;
        this.quiz_category_id = quiz_category_id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getQuiz_type_id() {
        return quiz_type_id;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPic_url() {
        return pic_url;
    }

    public String getSum_question() {
        return sum_question;
    }

    public String getTot_visible() {
        return tot_visible;
    }

    public String getTime() {
        return time;
    }

    public String getQuiz_category_id() {
        return quiz_category_id;
    }

    public String getName() {
        return name;
    }
}
