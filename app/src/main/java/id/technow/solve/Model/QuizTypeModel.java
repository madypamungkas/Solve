package id.technow.solve.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuizTypeModel {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("quiz_category_id")
    @Expose
    private String quiz_category_id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("pic_url")
    @Expose
    private String pic_url;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    @SerializedName("deleted_at")
    @Expose
    private String deleted_at;

    @SerializedName("quiz_category")
    @Expose
    private QuizCategoryModel quiz_category;

    public QuizTypeModel(int id, String quiz_category_id, String name, String description, String pic_url, String created_at, String updated_at, String deleted_at, QuizCategoryModel quiz_category) {
        this.id = id;
        this.quiz_category_id = quiz_category_id;
        this.name = name;
        this.description = description;
        this.pic_url = pic_url;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.quiz_category = quiz_category;
    }

    public int getId() {
        return id;
    }

    public String getQuiz_category_id() {
        return quiz_category_id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPic_url() {
        return pic_url;
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

    public QuizCategoryModel getQuiz_category() {
        return quiz_category;
    }
}
