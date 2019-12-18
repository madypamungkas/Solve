package com.komsi.solve.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseMenuHome {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("result")
    @Expose
    private ArrayList<MenuHomeModel> result ;

    public ResponseMenuHome(String status, ArrayList<MenuHomeModel> result) {
        this.status = status;
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<MenuHomeModel> getResult() {
        return result;
    }
}
