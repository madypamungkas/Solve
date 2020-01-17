package id.technow.solve.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseHistoryDetail {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("result")
    @Expose
    private HistoryDetailModel result;

    public ResponseHistoryDetail(String status, HistoryDetailModel result) {
        this.status = status;
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public HistoryDetailModel getResult() {
        return result;
    }
}
