package android.onlinecoursesapp.utils;

import android.onlinecoursesapp.model.Course;
import android.onlinecoursesapp.model.User;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface APIService {
    @POST("users/login-app-user")
    Call<ResponseBody> login(@Body User user);

    @POST("users/register-app-user")
    Call<ResponseBody> register(@Body User user);

    @GET("carts/getCart")
    Call<ResponseBody> getCart(@Header("Authorization") String authorization);

    @GET("users/")
    Call<ResponseBody> getUser(@Header("Authorization") String authorization);

    @POST("courses/search-courses")
    Call<ResponseBody> searchCourses(@Body Course.SearchCourses searchCourses);

    @Multipart
    @PUT("users/update-profile-picture")
    Call<ResponseBody> updateUserProfilePicture(@Header("Authorization") String token, @Part MultipartBody.Part image);
}
