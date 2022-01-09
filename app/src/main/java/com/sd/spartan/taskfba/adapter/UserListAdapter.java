package com.sd.spartan.taskfba.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sd.spartan.taskfba.R;
import com.sd.spartan.taskfba.model.User;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context mCtx;
    private final List<User> mUserList;


    public UserListAdapter(Context mCtx, List<User> mUserList) {
        this.mCtx = mCtx;
        this.mUserList = mUserList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.user_display_layout, null);
        return new UserListViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        User divisionModel = mUserList.get(position);
        ((UserListViewHolder) holder).bind(divisionModel);
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    static class UserListViewHolder extends RecyclerView.ViewHolder {
        private TextView mNameTV, mEmailTV;
        public UserListViewHolder(View itemView) {
            super(itemView);

            mNameTV = itemView.findViewById(R.id.text_name_layout);
            mEmailTV = itemView.findViewById(R.id.text_email_layout);
        }

        public void bind(User user) {

            mNameTV.setText(user.getName());
            mEmailTV.setText(user.getEmail());

//            mNameTV.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(mCtx, DistrictActivity.class) ;
//                    intent.putExtra(AppConstants.DIVISION_ID, divisionModel.getDivision_id()) ;
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
//                    mCtx.startActivity(intent);
//                }
//            });

        }
    }
}