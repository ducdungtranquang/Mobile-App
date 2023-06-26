package com.example.myloginapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ClassInfoAdapter extends RecyclerView.Adapter<ClassInfoAdapter.ClassInforHolderItem> {
    private List<ClassInfo> classInfos;
    private Context context;

    public interface OnDetailClickListener {
        void onDetailClick(int position);
    }

    private OnDetailClickListener onDetailClickListener;

    public void setOnDetailClickListener(OnDetailClickListener listener) {
        this.onDetailClickListener = listener;
    }
    public ClassInfoAdapter(List<ClassInfo> classInfos, Context c) {
        this.classInfos = classInfos;
        this.context = c;
    }

    @Override
    public int getItemCount() {
        return classInfos.size();
    }

    @Override
    public ClassInfoAdapter.ClassInforHolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_class, parent, false);

        return new ClassInfoAdapter.ClassInforHolderItem(itemView);
    }

    @Override
    public void onBindViewHolder(ClassInfoAdapter.ClassInforHolderItem holder, int position) {
        ClassInfo u = classInfos.get(position);
        holder.tvClassName.setText("Name: " + u.name);
        holder.tvTeacher.setText("Teacher: " + (u.teacher));
        holder.tvId.setText("ID Class: "+ u._id);

        holder.detailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDetailClickListener != null) {
                    onDetailClickListener.onDetailClick(position);
                }
            }
        });

    }

    public static class ClassInforHolderItem extends RecyclerView.ViewHolder {
        public TextView tvId;
        public TextView tvTeacher;
        public TextView tvClassName;
        public TextView detailBtn;
        public ImageView ivAvatar;


        public ClassInforHolderItem(View itemView) {
            super(itemView);
            detailBtn = (TextView) itemView.findViewById(R.id.detailbtn);
            tvId = (TextView) itemView.findViewById(R.id.tv_id);
            tvClassName = (TextView) itemView.findViewById(R.id.tv_class_name);
            tvTeacher = (TextView) itemView.findViewById(R.id.tv_teacher);
        }
        
    }
}
