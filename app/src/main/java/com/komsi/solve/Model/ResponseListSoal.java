package com.komsi.solve.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseListSoal {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("result")
    @Expose
    private ArrayList<ListSoalModel> result ;

    public ResponseListSoal(String status, ArrayList<ListSoalModel> result) {
        this.status = status;
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<ListSoalModel> getResult() {
        return result;
    }
}
