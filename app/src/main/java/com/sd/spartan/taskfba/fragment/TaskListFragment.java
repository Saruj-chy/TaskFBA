package com.sd.spartan.taskfba.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sd.spartan.taskfba.R;
import com.sd.spartan.taskfba.adapter.TaskListAdapter;
import com.sd.spartan.taskfba.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskListFragment extends Fragment {
    private ProgressBar mProgress;
    private RecyclerView mTaskListRV;
    private TextView mErrorMsgTV ;
    private List<Task> mTaskList;
    private TaskListAdapter mTaskListAdapter;
    private DatabaseReference DBRef, mTaskRef;
    private String currentUserId ;


    public TaskListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        Initialize(view) ;
        Toolbar mToolbar = view.findViewById(R.id.appbar_task_list) ;
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.app_name) ;

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid() ;
        DBRef = FirebaseDatabase.getInstance().getReference();
        mTaskRef = DBRef.child("Task").child(currentUserId);


        mTaskList = new ArrayList<>() ;
        mTaskListAdapter = new TaskListAdapter(getContext(), mTaskList) ;
        mTaskListRV.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mTaskListRV.setAdapter(mTaskListAdapter);
        mTaskListAdapter.notifyDataSetChanged();

        return view ;
    }

    @Override
    public void onStart() {
        super.onStart();

        mTaskList.clear();
        mTaskRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProgress.setVisibility(View.GONE);
                for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                    mTaskListRV.setVisibility(View.VISIBLE);
                    mProgress.setVisibility(View.GONE);

                    String title = dataSnap.child("title").getValue().toString() ;
                    String detail = dataSnap.child("detail").getValue().toString() ;

                    mTaskList.add(new Task(title, detail)) ;

                }
                if(mTaskList.size() >0){
                    mTaskListRV.setVisibility(View.VISIBLE);
                    mErrorMsgTV.setVisibility(View.GONE);
                }else{
                    mTaskListRV.setVisibility(View.GONE);
                    mErrorMsgTV.setVisibility(View.VISIBLE);
                }
                mTaskListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void Initialize(View view) {
        mTaskListRV = view.findViewById(R.id.recycler_task_list) ;
        mErrorMsgTV = view.findViewById(R.id.text_error) ;
        mProgress = view.findViewById(R.id.progress) ;
    }
}