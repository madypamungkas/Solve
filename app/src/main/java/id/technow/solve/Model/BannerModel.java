package id.technow.solve.Model;

public class BannerModel {
    private int id;
    private String picture;
    private String description;
    private String isView;
    private String linkTo;
    private String created_at;
    private String updated_at;

    public BannerModel(int id, String picture, String description, String isView, String linkTo, String created_at, String updated_at) {
        this.id = id;
        this.picture = picture;
        this.description = description;
        this.isView = isView;
        this.linkTo = linkTo;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public String getPicture() {
        return picture;
    }

    public String getDescription() {
        return description;
    }

    public String getIsView() {
        return isView;
    }

    public String getLinkTo() {
        return linkTo;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
