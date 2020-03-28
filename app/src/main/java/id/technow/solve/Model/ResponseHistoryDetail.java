package id.technow.solve.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseHistoryDetail {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("status_review")
    @Expose
    private String status_review;

    @SerializedName("result")
    @Expose
    private HistoryDetailModel result;

    @SerializedName("question")
    @Expose
    private ArrayList<AnswerSaveHDModel> question;

    public ResponseHistoryDetail(String status, String status_review, HistoryDetailModel result, ArrayList<AnswerSaveHDModel> question) {
        this.status = status;
        this.status_review = status_review;
        this.result = result;
        this.question = question;
    }

    public String getStatus() {
        return status;
    }

    public String getStatus_review() {
        return status_review;
    }

    public HistoryDetailModel getResult() {
        return result;
    }

    public ArrayList<AnswerSaveHDModel> getQuestion() {
        return question;
    }
}
