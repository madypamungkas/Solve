package id.technow.solve.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistoryModel {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("quiz_id")
    @Expose
    private String quiz_id;

    @SerializedName("collager_id")
    @Expose
    private String collager_id;

    @SerializedName("total_score")
    @Expose
    private String total_score;


    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    @SerializedName("true_sum")
    @Expose
    private String true_sum;

    @SerializedName("false_sum")
    @Expose
    private String false_sum;


    @SerializedName("quiz")
    @Expose
    private QuizModels quiz;

    public HistoryModel(int id, String quiz_id, String collager_id, String total_score, String created_at, String updated_at, String true_sum, String false_sum, QuizModels quiz) {
        this.id = id;
        this.quiz_id = quiz_id;
        this.collager_id = collager_id;
        this.total_score = total_score;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.true_sum = true_sum;
        this.false_sum = false_sum;
        this.quiz = quiz;
    }

    public int getId() {
        return id;
    }

    public String getQuiz_id() {
        return quiz_id;
    }

    public String getCollager_id() {
        return collager_id;
    }

    public String getTotal_score() {
        return total_score;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getTrue_sum() {
        return true_sum;
    }

    public String getFalse_sum() {
        return false_sum;
    }

    public QuizModels getQuiz() {
        return quiz;
    }
}
