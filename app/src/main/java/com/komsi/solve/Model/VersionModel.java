package com.komsi.solve.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VersionModel {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("version")
    @Expose
    private String version ;

    @SerializedName("sub_version")
    @Expose
    private String sub_version ;

    @SerializedName("year")
    @Expose
    private String year ;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    public VersionModel(String id, String version, String sub_version, String year, String created_at, String updated_at) {
        this.id = id;
        this.version = version;
        this.sub_version = sub_version;
        this.year = year;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public String getSub_version() {
        return sub_version;
    }

    public String getYear() {
        return year;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
