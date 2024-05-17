package android.onlinecoursesapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReviewResult implements Serializable {
    @SerializedName("message")
    private String message;
    @SerializedName("review")
    private String review;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
