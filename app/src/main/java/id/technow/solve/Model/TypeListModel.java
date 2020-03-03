package id.technow.solve.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TypeListModel {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("description")
    @Expose
    private String description;


    @SerializedName("sum_question")
    @Expose
    private String sum_question;

    @SerializedName("tot_visible")
    @Expose
    private String tot_visible;
    @SerializedName("pic_url")
    @Expose
    private String pic_url;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("time")
    @Expose
    private String time;


    public TypeListModel(int id, String type, String title, String code, String description, String sum_question, String tot_visible, String pic_url, String status, String time) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.code = code;
        this.description = description;
        this.sum_question = sum_question;
        this.tot_visible = tot_visible;
        this.pic_url = pic_url;
        this.status = status;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getSum_question() {
        return sum_question;
    }

    public String getTot_visible() {
        return tot_visible;
    }

    public String getPic_url() {
        return pic_url;
    }

    public String getStatus() {
        return status;
    }

    public String getTime() {
        return time;
    }
}
