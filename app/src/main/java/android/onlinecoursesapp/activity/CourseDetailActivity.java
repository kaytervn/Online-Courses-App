package android.onlinecoursesapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.onlinecoursesapp.R;
import android.onlinecoursesapp.adapter.LessonAdapter;
import android.onlinecoursesapp.model.Lesson;
import android.onlinecoursesapp.utils.APIService;
import android.onlinecoursesapp.utils.RetrofitClient;
import android.os.Bundle;
import android.widget.Toast;

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

public class CourseDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerViewLessons;
    private LessonAdapter lessonAdapter;
    private APIService apiService;
    private String courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        recyclerViewLessons = findViewById(R.id.recyclerViewLessons);
        recyclerViewLessons.setLayoutManager(new LinearLayoutManager(this));

        courseId = getIntent().getStringExtra("course_id");

        getCourseLessons(courseId);
    }

    private void getCourseLessons(String courseId) {
        apiService = RetrofitClient.getAPIService();
        Call<ResponseBody> call = apiService.getCourseLessons(new Lesson(courseId));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseBody);

                        JSONArray lessonsJSONArray = jsonResponse.getJSONArray("lessons");
                        Lesson[] lessons = new Lesson[lessonsJSONArray.length()];
                        for (int i = 0; i < lessonsJSONArray.length(); i++) {
                            JSONObject lessonJSON = lessonsJSONArray.getJSONObject(i);
                            Lesson lesson = new Lesson(
                                    lessonJSON.getString("_id"),
                                    lessonJSON.getString("courseId"),
                                    lessonJSON.getString("title"),
                                    lessonJSON.getString("description")
                            );
                            lessons[i] = lesson;
                        }

                        showLessons(Arrays.asList(lessons));

                    } catch (IOException | JSONException e) {
                        Toast.makeText(CourseDetailActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(CourseDetailActivity.this, "Error: Response not successful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CourseDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void showLessons(List<Lesson> lessonList) {
        lessonAdapter = new LessonAdapter(this, lessonList);
        recyclerViewLessons.setAdapter(lessonAdapter);
    }
}
