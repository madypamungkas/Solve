package id.technow.solve.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailSchoolModel {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    @SerializedName("province")
    @Expose
    private String province;

    @SerializedName("region")
    @Expose
    private String region;

    @SerializedName("district")
    @Expose
    private String district;


    public DetailSchoolModel(String id, String name, String address, String created_at, String updated_at, String province, String region, String district) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.province = province;
        this.region = region;
        this.district = district;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getProvince() {
        return province;
    }

    public String getRegion() {
        return region;
    }

    public String getDistrict() {
        return district;
    }
}
