package com.example.myloginapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StudentPerClass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_per_class);
        String classId = getIntent().getStringExtra("classId");
        Toast.makeText(this, "Class ID: " + classId, Toast.LENGTH_SHORT).show();

        TextView backBtn = findViewById(R.id.back);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentPerClass.this, Home.class);
                startActivity(intent);
            }
        });
        final RecyclerView rvStudent = (RecyclerView) findViewById(R.id.rv_students);
        rvStudent.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo OkHttpClient để lấy dữ liệu.
        OkHttpClient client = new OkHttpClient();

        // Khởi tạo Moshi adapter để biến đổi json sang model java
        Moshi moshi = new Moshi.Builder().build();
        Type studentType = Types.newParameterizedType(List.class, Student.class);
        final JsonAdapter<List<Student>> jsonAdapter = moshi.adapter(studentType);

        // Tạo request lên server.
        Request request = new Request.Builder()
                .url("https://nice-teal-lobster-tam.cyclic.app/api/students/" + classId)
                .build();

        // Thực thi request.
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Error", String.valueOf(e));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String json = response.body().string();
                final List<Student> students = jsonAdapter.fromJson(json);


                // Cho hiển thị lên RecyclerView.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rvStudent.setAdapter(new StudentAdapter(students, StudentPerClass.this));
                    }
                });
            }});
    }
}