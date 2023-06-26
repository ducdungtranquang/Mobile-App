package com.example.myloginapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView logout = findViewById(R.id.textView);
        TextView classList = findViewById(R.id.textView3);
        TextView studentList = findViewById(R.id.textView4);
        TextView addStudent = findViewById(R.id.textView2);

        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, AddStudent.class);
                startActivity(intent);
            }
        });

        studentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //correct
                Intent intent = new Intent(Home.this, StudentList.class);
                startActivity(intent);
            }
        });

        classList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //correct
                Intent intent = new Intent(Home.this, ClassList.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //correct
                    Intent intent = new Intent(Home.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(Home.this,"LOGOUT SUCCESSFUL",Toast.LENGTH_SHORT).show();
            }
        });
    }
}