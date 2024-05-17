package android.onlinecoursesapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.onlinecoursesapp.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonIntro = findViewById(R.id.buttonIntro);
        Button buttonCart = findViewById(R.id.buttonCart);
        Button btn_MyCourse = (Button) findViewById(R.id.btn_MyCourse);
        buttonIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, IntroActivity.class);
                startActivity(intent);
            }
        });

        buttonCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        btn_MyCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(MainActivity.this, MyCourseActivity.class);
                startActivity(intent);
            }
        });
    }
}