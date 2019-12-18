package com.komsi.solve.Model;

import java.util.ArrayList;

public class ResponseBanner {
    private String status;
    private ArrayList<BannerModel> result;

    public ResponseBanner(String status, ArrayList<BannerModel> result) {
        this.status = status;
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<BannerModel> getResult() {
        return result;
    }
}
