package com.komsi.solve.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseTypeList {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("result")
    @Expose
    private ArrayList<TypeListModel> result ;

    public ResponseTypeList(String status, ArrayList<TypeListModel> result) {
        this.status = status;
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<TypeListModel> getResult() {
        return result;
    }
}
