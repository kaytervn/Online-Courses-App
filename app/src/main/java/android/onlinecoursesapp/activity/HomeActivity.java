package android.onlinecoursesapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.onlinecoursesapp.R;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    Button buttonHome, buttonMyCourses, buttonCart, buttonProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mapping();
        setEvent();
    }

    private void setEvent() {
    }

    void mapping(){
        buttonHome = findViewById(R.id.buttonHome);
        buttonMyCourses = findViewById(R.id.buttonMyCourses);
        buttonCart = findViewById(R.id.buttonCart);
        buttonProfile = findViewById(R.id.buttonProfile);
    }
}