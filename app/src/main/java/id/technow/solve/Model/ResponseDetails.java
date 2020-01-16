package id.technow.solve.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseDetails {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("user")
    @Expose
    private DetailUser user;

    public ResponseDetails(String status, DetailUser user) {
        this.status = status;
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public DetailUser getUser() {
        return user;
    }
}
