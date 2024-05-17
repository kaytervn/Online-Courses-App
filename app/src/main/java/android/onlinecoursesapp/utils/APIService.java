package android.onlinecoursesapp.utils;

import android.onlinecoursesapp.model.MyCourse;
import android.onlinecoursesapp.model.Review;
import android.onlinecoursesapp.model.ReviewData;
import android.onlinecoursesapp.model.ReviewResult;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIService {
    @GET("invoices/my_course")
    Call<MyCourse> getMyCourses(@Header("Authorization") String token);
    @GET("reviews/get-my-review-for-course/{courseId}")
    Call<Review> getMyReviewForCourse(@Header("Authorization") String token, @Path("courseId") String courseId);
    @POST("reviews/create_review/{courseId}")
    Call<ReviewResult> createReview(@Header("Authorization") String token, @Path("courseId") String temp, @Body ReviewData reviewData);
}
