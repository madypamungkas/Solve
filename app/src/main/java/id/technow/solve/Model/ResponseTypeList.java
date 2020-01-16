package id.technow.solve.Model;

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

    @SerializedName("message")
    @Expose
    private String message ;

    public ResponseTypeList(String status, ArrayList<TypeListModel> result, String message) {
        this.status = status;
        this.result = result;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<TypeListModel> getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }
}
