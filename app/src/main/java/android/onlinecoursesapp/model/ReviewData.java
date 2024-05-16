package android.onlinecoursesapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReviewData implements Serializable {
    @SerializedName("ratingStar")
    private int ratingStar;
    @SerializedName("content")
    private String content;

    public ReviewData(int ratingStar, String content) {
        this.ratingStar = ratingStar;
        this.content = content;
    }

    public int getRatingStar() {
        return ratingStar;
    }

    public void setRatingStar(int ratingStar) {
        this.ratingStar = ratingStar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
