package android.onlinecoursesapp.model;

import com.google.gson.annotations.SerializedName;

public class UserModel {
    @SerializedName("id")
    String id;

    @SerializedName("image")
    String image;

    @SerializedName("name")
    String name;

    @SerializedName("email")
    String email;

    @SerializedName("password")
    String password;
}
