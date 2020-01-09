package com.komsi.solve.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseSchools {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("result")
    @Expose
    private ArrayList<SchoolsModel> result;

    public ResponseSchools(String status, ArrayList<SchoolsModel> result) {
        this.status = status;
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<SchoolsModel> getResult() {
        return result;
    }
}
