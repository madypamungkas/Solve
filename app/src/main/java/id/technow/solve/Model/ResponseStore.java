package id.technow.solve.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseStore {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("result")
    @Expose
    private String result;

    public ResponseStore(String status, String result) {
        this.status = status;
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public String getResult() {
        return result;
    }
}
