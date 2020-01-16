package id.technow.solve.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaderboarModel {
    @SerializedName("user_id")
    @Expose
    private int user_id;

    @SerializedName("collagers_id")
    @Expose
    private int collagers_id;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("picture")
    @Expose
    private String picture;

    @SerializedName("total_score")
    @Expose
    private String total_score;

    public LeaderboarModel(int user_id, int collagers_id, String username, String picture, String total_score) {
        this.user_id = user_id;
        this.collagers_id = collagers_id;
        this.username = username;
        this.picture = picture;
        this.total_score = total_score;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getCollagers_id() {
        return collagers_id;
    }

    public String getUsername() {
        return username;
    }

    public String getPicture() {
        return picture;
    }

    public String getTotal_score() {
        return total_score;
    }
}
