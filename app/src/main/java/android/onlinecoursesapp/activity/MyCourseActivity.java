package android.onlinecoursesapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.onlinecoursesapp.adapter.MyCourseAdapter;
import android.onlinecoursesapp.model.Course;
import android.onlinecoursesapp.model.MyCourse;
import android.onlinecoursesapp.utils.APIService;
import android.onlinecoursesapp.utils.RetrofitClient;
import android.os.Bundle;
import android.onlinecoursesapp.R;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCourseActivity extends AppCompatActivity {

    RecyclerView rc_my_course;
    APIService apiService;
    String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2NjQwZjZlOTI4NzQ4YWRhMTU3ZmNmN2IiLCJpYXQiOjE3MTU4MzgzMDYsImV4cCI6MTcxNjcwMjMwNn0.Ux364VlueQAVjwhxCPJUXOY5pckCQPaat8iM-gwFgmk";
    List<Course> listCourse;
    MyCourseAdapter myCourseadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_course);
        mapping();
        GetMyCourse();
    }
    private void mapping(){
        rc_my_course = (RecyclerView) findViewById(R.id.rv_my_course);
    }
    private void GetMyCourse(){
        apiService = RetrofitClient.getAPIService();

        apiService.getMyCourses("Bearer "+token).enqueue(new Callback<MyCourse>() {
            @Override
            public void onResponse(@NonNull Call<MyCourse> call, @NonNull Response<MyCourse> response) {
                if(response.isSuccessful()){
                    MyCourse mycourse = response.body();
                    listCourse = mycourse.getCourses();
                    Log.d("logg", listCourse.get(0).getTitle());
                    myCourseadapter = new MyCourseAdapter(MyCourseActivity.this, listCourse, token);
                    rc_my_course.setHasFixedSize(true);
                    rc_my_course.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    rc_my_course.setAdapter(myCourseadapter);
                    myCourseadapter.notifyDataSetChanged();
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
            public void onFailure(@NonNull Call<MyCourse> call, @NonNull Throwable t) {
                Log.d("logg", t.getMessage());
            }
        });
    }
}