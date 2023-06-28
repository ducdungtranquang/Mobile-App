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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StudentList extends AppCompatActivity {

    public void deleteStudent(int _id) {

    }

    public void updateStudent(String gradeS, String tvLoginName, String tvAge, String tvClassName, String idS) {
        OkHttpClient client = new OkHttpClient();

        JSONObject jsonObject = new JSONObject();
        try {
            // Tạo đối tượng JSON chứa thông tin sinh viên
            jsonObject.put("name", tvLoginName);
            jsonObject.put("age", tvAge);
            jsonObject.put("classId", tvClassName);
            jsonObject.put("grade", gradeS);

            // Tạo request body từ JSON
            RequestBody requestBody = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    jsonObject.toString()
            );
            Log.d("StudentList", "Edit - Grade:" + requestBody);
            Log.d("StudentList", "Edit - IDS:" + idS);

            // Tạo request POST
            Request request = new Request.Builder()
                    .url("https://nice-teal-lobster-tam.cyclic.app/api/students/" + idS)
                    .put(requestBody)
                    .build();

            // Gửi request và xử lý response
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    // Xử lý khi request gặp lỗi
                    Log.e("Error", String.valueOf(e));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    // Xử lý khi nhận được response từ API
                    if (response.isSuccessful()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(StudentList.this, "Student updated successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                        // Xử lý khi request thành công
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(StudentList.this, "Request failed. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        // Xử lý khi request không thành công
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        TextView backBtn = findViewById(R.id.back);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentList.this, Home.class);
                startActivity(intent);
            }
        });
        final RecyclerView rvStudent = (RecyclerView) findViewById(R.id.rv_students);
        rvStudent.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo OkHttpClient để lấy dữ liệu.
        OkHttpClient client = new OkHttpClient();

        // Khởi tạo Moshi adapter để biến đổi json sang model java (ở đây là Students)
        Moshi moshi = new Moshi.Builder().build();
        Type studentType = Types.newParameterizedType(List.class, Student.class);
        final JsonAdapter<List<Student>> jsonAdapter = moshi.adapter(studentType);

        // Tạo request lên server.
        Request request = new Request.Builder()
                .url("https://nice-teal-lobster-tam.cyclic.app/api/students")
                .build();

        // Thực thi request.
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Error", String.valueOf(e));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                // Lấy thông tin JSON trả về.
                String json = response.body().string();
                final List<Student> students = jsonAdapter.fromJson(json);


                // Cho hiển thị lên RecyclerView.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        StudentAdapter adapter = new StudentAdapter(students, StudentList.this);

                        adapter.setEditButtonClickListener(new StudentAdapter.EditButtonClickListener() {
                            @Override
                            public void onEditButtonClick(String gradeS, String tvLoginName, String tvAge, String tvClassName, String idS) {
                                updateStudent(gradeS, tvLoginName, tvAge, tvClassName, idS);
                            }
                        });

                        adapter.setDeleteListener(new StudentAdapter.DeleteListenerClick() {
                            @Override
                            public void onDeleteClick(int position) {
                                Student student = students.get(position);
                                String id = student._id;

                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder()
                                        .url("https://nice-teal-lobster-tam.cyclic.app/api/students/" + id)
                                        .delete()
                                        .build();

                                client.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        // Xử lý khi có lỗi xảy ra
                                        e.printStackTrace();
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        if (response.isSuccessful()) {
                                            students.remove(position);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Intent intent = new Intent(StudentList.this, Home.class);
                                                    startActivity(intent);
                                                    Toast.makeText(StudentList.this, "Delete successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else {

                                        }
                                    }
                                });
                            }

                        });
                        rvStudent.setAdapter(adapter);
                    }
                });

            }
        });
    }
}