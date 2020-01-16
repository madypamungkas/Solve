package id.technow.solve.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponsePostAnswer {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("result")
    @Expose
    private PostAnswerModel result;

    public ResponsePostAnswer(String status, PostAnswerModel result) {
        this.status = status;
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public PostAnswerModel getResult() {
        return result;
    }
}
