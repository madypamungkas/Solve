package id.technow.solve.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TypeListModel {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("quiz_type_id")
    @Expose
    private int quiz_type_id;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("pic_url")
    @Expose
    private String pic_url;

    @SerializedName("sum_question")
    @Expose
    private String sum_question;

    @SerializedName("tot_visible")
    @Expose
    private String tot_visible;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    @SerializedName("deleted_at")
    @Expose
    private String deleted_at;

    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("start_time")
    @Expose
    private String start_time;
    @SerializedName("end_time")
    @Expose
    private String end_time;
    @SerializedName("created_by")
    @Expose
    private String created_by;

    public TypeListModel(int id, int quiz_type_id, String code, String title, String description, String pic_url, String sum_question, String tot_visible, String created_at, String updated_at, String deleted_at, String time, String status, String start_time, String end_time, String created_by) {
        this.id = id;
        this.quiz_type_id = quiz_type_id;
        this.code = code;
        this.title = title;
        this.description = description;
        this.pic_url = pic_url;
        this.sum_question = sum_question;
        this.tot_visible = tot_visible;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.time = time;
        this.status = status;
        this.start_time = start_time;
        this.end_time = end_time;
        this.created_by = created_by;
    }

    public int getId() {
        return id;
    }

    public int getQuiz_type_id() {
        return quiz_type_id;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPic_url() {
        return pic_url;
    }

    public String getSum_question() {
        return sum_question;
    }

    public String getTot_visible() {
        return tot_visible;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setQuiz_type_id(int quiz_type_id) {
        this.quiz_type_id = quiz_type_id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public void setSum_question(String sum_question) {
        this.sum_question = sum_question;
    }

    public void setTot_visible(String tot_visible) {
        this.tot_visible = tot_visible;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }
}
