package android.onlinecoursesapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.onlinecoursesapp.adapter.DocumentsAdapter;
import android.onlinecoursesapp.model.Document;
import android.onlinecoursesapp.utils.APIService;
import android.onlinecoursesapp.utils.RetrofitClient;
import android.os.Bundle;
import android.onlinecoursesapp.R;
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

public class LessonDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerViewDocuments;
    private DocumentsAdapter documentsAdapter;
    private List<Document> documentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_detail);

        recyclerViewDocuments = findViewById(R.id.recyclerViewDocuments);
        recyclerViewDocuments.setLayoutManager(new LinearLayoutManager(this));

        String lessonId = getIntent().getStringExtra("lesson_id");
        getLessonDocuments(lessonId);
    }

    private void getLessonDocuments(String lessonId) {
        // Gọi API để lấy danh sách tài liệu cho bài học
        // Sử dụng Retrofit hoặc các thư viện HTTP khác để thực hiện cuộc gọi API

        // Ví dụ sử dụng Retrofit
        APIService apiService = RetrofitClient.getAPIService();
        Call<ResponseBody> call = apiService.getLessonDocuments(new Document(lessonId));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseBody);

                        JSONArray documentsJSONArray = jsonResponse.getJSONArray("documents");
                        Document[] documents = new Document[documentsJSONArray.length()];
                        for (int i = 0; i < documentsJSONArray.length(); i++) {
                            JSONObject documentJSON = documentsJSONArray.getJSONObject(i);
                            Document document = new Document(
                                    documentJSON.getString("_id"),
                                    documentJSON.getString("lessonId"),
                                    documentJSON.getString("cloudinary"),
                                    documentJSON.getString("title"),
                                    documentJSON.getString("description"),
                                    documentJSON.getString("content")
                            );
                            documents[i] = document;
                        }

                        showDocuments(Arrays.asList(documents));

                    } catch (IOException | JSONException e) {
                        Toast.makeText(LessonDetailActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(LessonDetailActivity.this, "Lỗi: Response not successful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(LessonDetailActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void showDocuments(List<Document> documentList) {
        documentsAdapter = new DocumentsAdapter(LessonDetailActivity.this, documentList);
        recyclerViewDocuments.setAdapter(documentsAdapter);
    }
}
