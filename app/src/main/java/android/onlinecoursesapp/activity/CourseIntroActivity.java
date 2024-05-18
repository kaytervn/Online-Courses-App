package android.onlinecoursesapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import android.onlinecoursesapp.R;
import android.onlinecoursesapp.adapter.ReviewsAdapter;
import android.onlinecoursesapp.model.CartItem;
import android.onlinecoursesapp.model.CourseIntro;
import android.onlinecoursesapp.utils.APIService;
import android.onlinecoursesapp.utils.RetrofitClient;
import android.onlinecoursesapp.utils.SessionManager;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private Button btnAddToCart;
    private String courseId;
    private Set<String> purchasedCourseIds = new HashSet<>();
    private Button buttonAddToCart;

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
        btnAddToCart = findViewById(R.id.buttonAddToCart);

        courseId = getIntent().getStringExtra("course_id");

        getMyCoursesAndCourseDetails();
      
        buttonAddToCart = findViewById(R.id.buttonAddToCart);
        courseId = getIntent().getStringExtra("course_id");
        Log.d("addtocart2", "courseid"+ courseId);
        getCourseDetails(courseId);
        setupAddToCartButton();
    }
    private void setupAddToCartButton() {
        buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });
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
                        double averageStars = jsonResponse.getDouble("averageStars");
                        Gson gson = new Gson();
                        CourseIntro course = gson.fromJson(courseJSON.toString(), CourseIntro.class);

                        textTitle.setText(course.getTitle());
                        textDescription.setText(course.getDescription());
                        textPrice.setText(String.valueOf(course.getPrice()));
                        textInstructorName.setText(course.getInstructorName());
                        textTopic.setText(course.getTopic());
                        textAverageStar.setText(String.valueOf(averageStars));
                        Glide.with(CourseIntroActivity.this).load(course.getPicture()).into(imagePicture);

                        JSONArray reviewsJSONArray = jsonResponse.getJSONArray("reviews");
                        CourseIntro.Review[] reviews = gson.fromJson(reviewsJSONArray.toString(), CourseIntro.Review[].class);
                        showReviews(Arrays.asList(reviews));

                        if (purchasedCourseIds.contains(courseId)) {
                            btnAddToCart.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                            setupButton("View Course Details");

                        } else {
                            setupButton("Add to cart");
                        }

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
    private void addToCart() {
        String cartId = SessionManager.getInstance(CourseIntroActivity.this).getKeyCartId();
        String token = SessionManager.getInstance(CourseIntroActivity.this).getKeyToken();
        apiService = RetrofitClient.getAPIService();
        apiService.addToCart("Bearer " + token, new CartItem(cartId, courseId)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CourseIntroActivity.this, "Course added to cart successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle error response
                    String errorMessage = "Failed to add course to cart"; // Default error message
                    if (response.errorBody() != null) {
                        try {
                            // Try to parse the error body to get detailed error message
                            String errorBody = response.errorBody().string();
                            JSONObject errorObject = new JSONObject(errorBody);
                            if (errorObject.has("error")) {
                                errorMessage = errorObject.getString("error");
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace(); // Log the exception
                            errorMessage = "Failed to parse error message"; // Fallback error message
                        }
                    }
                    Toast.makeText(CourseIntroActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CourseIntroActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getMyCoursesAndCourseDetails() {
        apiService = RetrofitClient.getAPIService();
        String token = "Bearer " + SessionManager.getInstance(this).getKeyToken();
        Call<ResponseBody> call = apiService.getMyCourses(token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseBody);

                        JSONArray coursesJSONArray = jsonResponse.getJSONArray("courses");
                        for (int i = 0; i < coursesJSONArray.length(); i++) {
                            JSONObject courseJSON = coursesJSONArray.getJSONObject(i);
                            purchasedCourseIds.add(courseJSON.getString("_id"));
                        }

                        getCourseDetails(courseId);

                    } catch (IOException | JSONException e) {
                        Toast.makeText(CourseIntroActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(CourseIntroActivity.this, "Error: Response not successful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CourseIntroActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showReviews(List<CourseIntro.Review> reviewList) {
        reviewsAdapter = new ReviewsAdapter(CourseIntroActivity.this, reviewList);
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(CourseIntroActivity.this));
        recyclerViewReviews.setAdapter(reviewsAdapter);
    }

    private void setupButton(String buttonText) {
        Button btnAddToCart = findViewById(R.id.buttonAddToCart);
        btnAddToCart.setText(buttonText);
        btnAddToCart.setOnClickListener(view -> {
            if (buttonText.equals("View Course Details")) {
                Intent intent = new Intent(CourseIntroActivity.this, CourseDetailActivity.class);
                intent.putExtra("course_id", courseId);
                startActivity(intent);
                finish();
            } else {
                // Xử lý thêm vào giỏ hàng
//                addToCart(courseId);
            }
        });
    }

}
