package com.example.myloginapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

public class ClassList extends AppCompatActivity {

    private int currentDetailId = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);
        TextView backBtn = findViewById(R.id.back);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassList.this, Home.class);
                startActivity(intent);
            }
        });

        final RecyclerView rvClass = (RecyclerView) findViewById(R.id.rv_class);
        rvClass.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo OkHttpClient để lấy dữ liệu.
        OkHttpClient client = new OkHttpClient();

        Moshi moshi = new Moshi.Builder().build();
        Type classType = Types.newParameterizedType(List.class, ClassInfo.class);
        final JsonAdapter<List<ClassInfo>> jsonAdapter = moshi.adapter(classType);

        // Tạo request lên server.
        Request request = new Request.Builder()
                .url("https://nice-teal-lobster-tam.cyclic.app/api/classes")
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
                final List<ClassInfo> classInfos = jsonAdapter.fromJson(json);


                // Cho hiển thị lên RecyclerView.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ClassInfoAdapter adapter = new ClassInfoAdapter(classInfos, ClassList.this);
                        adapter.setOnDetailClickListener(new ClassInfoAdapter.OnDetailClickListener() {
                            @Override
                            public void onDetailClick(int position) {
                                // Xử lý sự kiện khi nhấn vào nút "detailBtn" ở vị trí position
                                ClassInfo classInfo = classInfos.get(position);
                                String classId = classInfo._id;
                                Intent intent = new Intent(ClassList.this, StudentPerClass.class);
                                intent.putExtra("classId", classId);
                                startActivity(intent);
                            }
                        });
                        rvClass.setAdapter(adapter);
                    }
                });
            }});

    }
}