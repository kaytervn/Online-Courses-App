package android.onlinecoursesapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.onlinecoursesapp.adapter.MyCourseAdapter;
import android.onlinecoursesapp.model.Course;
import android.onlinecoursesapp.model.MyCourse;
import android.onlinecoursesapp.model.ReviewData;
import android.onlinecoursesapp.utils.APIService;
import android.onlinecoursesapp.utils.RetrofitClient;
import android.os.Bundle;
import android.onlinecoursesapp.R;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewCourseActivity extends AppCompatActivity {

    TextView tvCourseName;
    ImageView imgCourse;
    RatingBar starReview;
    EditText contentReview;
    Button btn_send, btn_back;
    ReviewData review_data;
    String token;
    Course course;
    APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_course);
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        course = (Course) intent.getSerializableExtra("course");
        mapping();
        setView();
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ReviewCourseActivity.this, MyCourseActivity.class);
                startActivity(intent);
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                review_data = new ReviewData(starReview.getNumStars(), contentReview.getText().toString());
                sendReview();
                finish();
                Intent intent = new Intent(ReviewCourseActivity.this, ReviewCourseActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setView(){
        tvCourseName.setText(course.getTitle());
        Glide.with(getApplicationContext()).load(course.getPicture())
                .into(imgCourse);
        apiService = RetrofitClient.getAPIService();
        apiService.getMyReviewForCourse("Bearer "+token, course.get_id()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    String jsonString = response.body().string();
                    JSONObject jsonObject = new JSONObject(jsonString);
                    String review_id = jsonObject.getString("_id");

                    if (review_id.isEmpty()) {
                        btn_send.setEnabled(true);
                    } else {
                        btn_send.setEnabled(false);
                    }
                }else{
                    String errorMessage = "";
                    try {
                        JsonObject errorJson = new Gson().fromJson(response.errorBody().string(), JsonObject.class);
                        errorMessage = errorJson.get("error").getAsString();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
//                    int statusCode = response.code();
//                    Log.d("logg", String.valueOf(statusCode));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    private void sendReview(){

    }
    private void mapping(){
        tvCourseName = (TextView) findViewById(R.id.txtCourseName);
        imgCourse = (ImageView) findViewById(R.id.imagePic);
        starReview = (RatingBar) findViewById(R.id.ratingBarReview);
        contentReview = (EditText) findViewById(R.id.editTextContentReview);
        btn_send = (Button) findViewById(R.id.btn_sent_review);
        btn_back = (Button) findViewById(R.id.btn_back);
    }
}