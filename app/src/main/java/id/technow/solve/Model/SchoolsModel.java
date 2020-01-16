package id.technow.solve.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SchoolsModel {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("text")
    @Expose
    private String text;

    public SchoolsModel(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }
}
