package android.onlinecoursesapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.onlinecoursesapp.model.Course;
import android.onlinecoursesapp.model.ReviewData;
import android.os.Bundle;
import android.onlinecoursesapp.R;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ReviewCourseActivity extends AppCompatActivity {

    TextView tvCourseName;
    ImageView imgCourse;
    RatingBar starReview;
    EditText contentReview;
    Button btn_send, btn_back;
    ReviewData review_data;
    String token;
    Course course;

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