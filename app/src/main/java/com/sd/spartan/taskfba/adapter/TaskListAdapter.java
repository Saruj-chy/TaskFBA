package com.sd.spartan.taskfba.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sd.spartan.taskfba.R;
import com.sd.spartan.taskfba.model.Task;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TaskListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context mCtx;
    private final List<Task> mTaskList;


    public TaskListAdapter(Context mCtx, List<Task> mTaskList) {
        this.mCtx = mCtx;
        this.mTaskList = mTaskList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.user_display_layout, null);
        return new TaskListViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Task task = mTaskList.get(position);
        ((TaskListViewHolder) holder).bind(task);
    }

    @Override
    public int getItemCount() {
        return mTaskList.size();
    }

    static class TaskListViewHolder extends RecyclerView.ViewHolder {
        private TextView mNameTV, mEmailTV;
        private CircleImageView mImageView;
        public TaskListViewHolder(View itemView) {
            super(itemView);

            mNameTV = itemView.findViewById(R.id.text_name_layout);
            mEmailTV = itemView.findViewById(R.id.text_email_layout);
            mImageView = itemView.findViewById(R.id.users_profile_image);
        }

        public void bind(Task user) {

            mNameTV.setText(user.getTitle());
            mEmailTV.setText(user.getDetail());
            mImageView.setVisibility(View.GONE);

        }
    }
}