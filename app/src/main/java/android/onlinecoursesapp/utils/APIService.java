package android.onlinecoursesapp.utils;

import android.onlinecoursesapp.model.User;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface APIService {
    @POST("users/login-app-user")
    Call<ResponseBody> login(@Body User user);

    @GET("users/")
    Call<ResponseBody> getUser(@Header("Authorization") String authorization);
}
