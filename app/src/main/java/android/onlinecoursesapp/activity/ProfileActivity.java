package android.onlinecoursesapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.onlinecoursesapp.utils.APIService;
import android.onlinecoursesapp.utils.RetrofitClient;
import android.onlinecoursesapp.utils.SessionManager;
import android.os.Bundle;
import android.onlinecoursesapp.R;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    Button buttonHome, buttonMyCourses, buttonCart, buttonProfile;
    TextView textName, textEmail, textRole, textLogout;
    ImageView imagePicture;
    APIService apiService;
    FloatingActionButton buttonUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mapping();
        setEvent();
        getUser();
    }

    private void setEvent() {
        buttonProfile.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E91E63")));
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        textLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

    void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Log out").setMessage("Confirm log out?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SessionManager.getInstance(ProfileActivity.this).clearLoginUser();
                        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();
    }

    void mapping() {
        buttonHome = findViewById(R.id.buttonHome);
        buttonMyCourses = findViewById(R.id.buttonMyCourses);
        buttonCart = findViewById(R.id.buttonCart);
        buttonProfile = findViewById(R.id.buttonProfile);
        textName = findViewById(R.id.textName);
        imagePicture = findViewById(R.id.imagePicture);
        textEmail = findViewById(R.id.textEmail);
        textRole = findViewById(R.id.textRole);
        buttonUpload = findViewById(R.id.buttonUpload);
        textLogout = findViewById(R.id.textLogout);
    }

    void getUser() {
        String token = SessionManager.getInstance(ProfileActivity.this).getKeyToken();
        if (token == "") {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            apiService = RetrofitClient.getAPIService();
            Call<ResponseBody> call = apiService.getUser("Bearer " + token);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String json = response.body().string();
                            JsonObject userObject = new Gson().fromJson(json, JsonObject.class).getAsJsonObject("user");
                            textName.setText(userObject.get("name").getAsString());
                            textRole.setText(userObject.get("role").getAsString());
                            textEmail.setText(userObject.get("email").getAsString());
                            String imageURL = userObject.get("picture").getAsString();
                            if (imageURL.length() > 0) {
                                Glide.with(ProfileActivity.this).load(imageURL).into(imagePicture);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(ProfileActivity.this, "Response Failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("Failed to call API", t.getMessage());
                }
            });
        }
    }
}