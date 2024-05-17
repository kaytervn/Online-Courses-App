package android.onlinecoursesapp.activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.onlinecoursesapp.R;
import android.onlinecoursesapp.adapter.ReviewsAdapter;
import android.onlinecoursesapp.model.CourseIntro;
import android.onlinecoursesapp.utils.APIService;
import android.onlinecoursesapp.utils.RetrofitClient;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseIntroActivity extends AppCompatActivity {

    private TextView textTitle, textDescription, textPrice, textInstructorName, textTopic, textAverageStar;
    private ImageView imagePicture;
    private RecyclerView recyclerViewReviews;
    private ReviewsAdapter reviewsAdapter;
    private APIService apiService;
    private String courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_intro);

        textTitle = findViewById(R.id.tvTitleCourse);
        textDescription = findViewById(R.id.tvDescriptionCourse);
        textPrice = findViewById(R.id.tvPriceCourse);
        textInstructorName = findViewById(R.id.tvInstructorNameCourse);
        textTopic = findViewById(R.id.tvTopicCourse);
        imagePicture = findViewById(R.id.ivPictureCourse);
        recyclerViewReviews = findViewById(R.id.recyclerViewReviews);
        textAverageStar = findViewById(R.id.tvRatingStar);

        courseId = getIntent().getStringExtra("course_id");

        getCourseDetails(courseId);
    }

    private void getCourseDetails(String courseId) {
        apiService = RetrofitClient.getAPIService();
        Call<ResponseBody> call = apiService.getCourseIntro(courseId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseBody);


                        JSONObject courseJSON = jsonResponse.getJSONObject("course");
                        Gson gson = new Gson();
                        CourseIntro course = gson.fromJson(courseJSON.toString(), CourseIntro.class);


                        textTitle.setText(course.getTitle());
                        textDescription.setText(course.getDescription());
                        textPrice.setText(String.valueOf(course.getPrice()));
                        textInstructorName.setText(course.getInstructorName());
                        textTopic.setText(course.getTopic());

                        double averageStars = jsonResponse.getDouble("averageStars");
                        textAverageStar.setText(String.valueOf(averageStars));

                        Glide.with(CourseIntroActivity.this).load(course.getPicture()).into(imagePicture);

                        JSONArray reviewsJSONArray = jsonResponse.getJSONArray("reviews");
                        CourseIntro.Review[] reviews = gson.fromJson(reviewsJSONArray.toString(), CourseIntro.Review[].class);


                        showReviews(Arrays.asList(reviews));

                    } catch (IOException | JSONException e) {
                        textDescription.setText("Error: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    textDescription.setText("Error: Response not successful");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                textDescription.setText("Error: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void showReviews(List<CourseIntro.Review> reviewList) {
        reviewsAdapter = new ReviewsAdapter(CourseIntroActivity.this, reviewList);
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(CourseIntroActivity.this));
        recyclerViewReviews.setAdapter(reviewsAdapter);
    }
}
