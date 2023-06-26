package com.example.myloginapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddStudent extends AppCompatActivity {
    private Spinner spinner;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        TextView stdentName = findViewById(R.id.studentname);
        TextView grade = findViewById(R.id.grade);
        TextView ageStudent = findViewById(R.id.age);
        Spinner classId = findViewById(R.id.spinner);
        TextView addBtn = findViewById(R.id.addstudent);
        TextView backBtn = findViewById(R.id.back);

        spinner = findViewById(R.id.spinner);
        String[] items = {"6489651eccdb9812e87e717f", "648d5314f5c9168a2b718444", "648d5302f5c9168a2b718442"};
//        ArrayAdapter adapter = new ArrayAdapter<>(context, R.layout.activity_add_student, items);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddStudent.this, Home.class);
                startActivity(intent);
            }
        });

        addBtn.setOnClickListener(view -> {
            String studentName = stdentName.getText().toString();
            String gradeValue = grade.getText().toString();
            String ageValue = ageStudent.getText().toString();
            String selectedClassId = spinner.getSelectedItem().toString();

            if (studentName.isEmpty() || gradeValue.isEmpty() || ageValue.isEmpty() || selectedClassId.isEmpty()) {
                // Hiển thị thông báo lỗi hoặc thực hiện xử lý khi các giá trị không hợp lệ
                return; // Dừng thực hiện tiếp các bước sau nếu có giá trị không hợp lệ
            }
            // Tạo JSON object từ dữ liệu cần gửi
            JSONObject requestData = new JSONObject();
            try {
                requestData.put("name", studentName);
                requestData.put("grade", gradeValue);
                requestData.put("age", ageValue);
                requestData.put("classInfo", selectedClassId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Tạo request gửi lên API
            RequestBody requestBody = RequestBody.create(
                    MediaType.parse("application/json"), requestData.toString());
            Request request = new Request.Builder()
                    .url("https://nice-teal-lobster-tam.cyclic.app/api/students")
                    .post(requestBody)
                    .build();

            // Tạo OkHttpClient để gửi request
            OkHttpClient client = new OkHttpClient();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("Error",String.valueOf(e));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    // Xử lý khi nhận được phản hồi từ API
                    if (response.isSuccessful()) {
                        // Xử lý khi request thành công
                        Intent intent = new Intent(AddStudent.this, Home.class);
                        startActivity(intent);
                    } else {
                        // Xử lý khi request không thành công
                    }
                }
            });
        });

    }
}