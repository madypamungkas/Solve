package com.komsi.solve.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseVersion {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("result")
    @Expose
    private VersionModel result ;

    public ResponseVersion(String status, VersionModel result) {
        this.status = status;
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public VersionModel getResult() {
        return result;
    }
}
