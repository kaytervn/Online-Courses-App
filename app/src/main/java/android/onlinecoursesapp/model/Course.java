package android.onlinecoursesapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Course implements Serializable {
    @SerializedName("_id")
    private String _id;
    @SerializedName("userId")
    private String userId;
    @SerializedName("cloudinary")
    private String cloudinary;
    @SerializedName("topic")
    private String topic;
    @SerializedName("title")
    private String title;
    @SerializedName("picture")
    private String picture;
    @SerializedName("description")
    private String description;
    @SerializedName("price")
    private float price;
    @SerializedName("createAt")
    private Date createAt;
    @SerializedName("updatedAt")
    private Date updatedAt;
    @SerializedName("visibility")
    private boolean visibility;
    @SerializedName("status")
    private boolean status;
    @SerializedName("__v")
    private int __v;
    @SerializedName("instructorName")
    private String instructorName;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }
}
