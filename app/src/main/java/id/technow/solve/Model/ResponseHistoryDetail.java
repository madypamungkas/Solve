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

    @SerializedName("question")
    @Expose
    private ArrayList<AnswerSaveHDModel> question;

    public ResponseHistoryDetail(String status, HistoryDetailModel result, ArrayList<AnswerSaveHDModel> question) {
        this.status = status;
        this.result = result;
        this.question = question;
    }

    public String getStatus() {
        return status;
    }

    public HistoryDetailModel getResult() {
        return result;
    }

    public ArrayList<AnswerSaveHDModel> getQuestion() {
        return question;
    }
}
