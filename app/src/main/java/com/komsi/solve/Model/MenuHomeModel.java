package com.komsi.solve.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MenuHomeModel {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("pic_url")
    @Expose
    private String pic_url;

    public MenuHomeModel(int id, String name, String description, String pic_url) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.pic_url = pic_url;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPic_url() {
        return pic_url;
    }
}
