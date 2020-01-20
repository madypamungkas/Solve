package id.technow.solve.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailUser {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("email_verified_at")
    @Expose
    private String email_verified_at;

    @SerializedName("picture")
    @Expose
    private String picture;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    @SerializedName("deleted_at")
    @Expose
    private String deleted_at;

    @SerializedName("school_id")
    @Expose
    private String school_id;
    @SerializedName("phone_number")
    @Expose
    private String phone_number;

    @SerializedName("count_played")
    @Expose
    private String count_played;
    @SerializedName("high_score")
    @Expose
    private String high_score;
    @SerializedName("collager")
    @Expose
    private CollagerModel collager;
    @SerializedName("school")
    @Expose
    private DetailSchoolModel school;

    public DetailUser(String id, String name, String username, String email, String email_verified_at, String picture, String created_at, String updated_at, String deleted_at, String school_id, String phone_number, String count_played, String high_score, CollagerModel collager, DetailSchoolModel school) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.email_verified_at = email_verified_at;
        this.picture = picture;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.school_id = school_id;
        this.phone_number = phone_number;
        this.count_played = count_played;
        this.high_score = high_score;
        this.collager = collager;
        this.school = school;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public String getPicture() {
        return picture;
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

    public String getSchool_id() {
        return school_id;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getCount_played() {
        return count_played;
    }

    public String getHigh_score() {
        return high_score;
    }

    public CollagerModel getCollager() {
        return collager;
    }

    public DetailSchoolModel getSchool() {
        return school;
    }
}
