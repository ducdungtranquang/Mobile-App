package com.example.myloginapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentItemViewHolder> {
    private List<Student> students;
    private Context context;
    private EditButtonClickListener editButtonClickListener;

    private DeleteListenerClick deleteListener;

    public StudentAdapter(List<Student> students, Context c) {
        this.students = students;
        this.context = c;
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    @Override
    public StudentItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student, parent, false);

        return new StudentItemViewHolder(itemView);
    }

    public interface EditButtonClickListener {
        void onEditButtonClick(String gradeS, String tvLoginName, String tvAge, String tvClassName, String idS);
    }

    public void setEditButtonClickListener(EditButtonClickListener listener) {
        this.editButtonClickListener = listener;
    }

    public interface DeleteListenerClick{
        void onDeleteClick(int position);
    }

    public void setDeleteListener(DeleteListenerClick listener) {
        this.deleteListener = listener;
    }


    @Override
    public void onBindViewHolder(StudentItemViewHolder holder, int position) {
        Student u = students.get(position);
        holder.tvLoginName.setText("Name: " + u.name);
        holder.tvAge.setText("Age: " + String.valueOf(u.age));
        holder.tvId.setText("ID: " + u._id);
        String classInfo = u.classInfo.toString();
        String idClass;
        try {
            JSONObject classInfoObj = new JSONObject(classInfo);
            String nameClass = classInfoObj.getString("name");
            holder.tvClassName.setText("Class: " + nameClass);
            idClass = classInfoObj.getString("_id");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editButtonClickListener != null) {
                    String nameS = holder.tvLoginName.getText().toString().split(":\\s+")[1];
                    String ageS = holder.tvAge.getText().toString().split(":\\s+")[1];
                    String gradeS = String.valueOf(u.grade);
                    String classInforS = idClass.toString();
                    String idS = u._id.toString();
                    // Truyền giá trị từ TextView vào phương thức onEditButtonClick()
                    editButtonClickListener.onEditButtonClick(gradeS, nameS, ageS, classInforS, idS);
                }
            }
        });

        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deleteListener != null) {
                    deleteListener.onDeleteClick(position);
                }
            }
        });

    }

    public static class StudentItemViewHolder extends RecyclerView.ViewHolder {
        public TextView tvLoginName;
        public TextView tvId;
        public TextView tvAge;
        public TextView tvClassName;
        public TextView delBtn;
        public TextView editBtn;
        public ImageView ivAvatar;

        public StudentItemViewHolder(View itemView) {
            super(itemView);
            tvLoginName = itemView.findViewById(R.id.tv_st_name);
            tvId = itemView.findViewById(R.id.tv_id);
            tvClassName = itemView.findViewById(R.id.tv_class_name);
            tvAge = itemView.findViewById(R.id.tv_age);
            editBtn = itemView.findViewById(R.id.edit);
            delBtn = itemView.findViewById(R.id.delete);
        }
    }
}
