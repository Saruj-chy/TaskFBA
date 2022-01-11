package com.sd.spartan.taskfba.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import com.sd.spartan.taskfba.model.User;

import java.util.ArrayList;
import java.util.List;

public class TaskListFragment extends Fragment {
    private ProgressBar mProgress;
    private RecyclerView taskListRV ;
    private TextView mErrorMsgTV ;
    private List<Task> taskList ;
    TaskListAdapter taskListAdapter ;
    private DatabaseReference DBRef, taskRef;
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
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setTitle(R.string.app_name) ;
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBa();
//            }
//        });


        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid() ;
        DBRef = FirebaseDatabase.getInstance().getReference();
        taskRef = DBRef.child("Task").child(currentUserId);




        taskList = new ArrayList<>() ;
        taskListAdapter = new TaskListAdapter(getContext(), taskList ) ;
        taskListRV.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        taskListRV.setAdapter(taskListAdapter);
        taskListAdapter.notifyDataSetChanged();

        return view ;
    }

    @Override
    public void onStart() {
        super.onStart();

        taskList.clear();
        taskRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProgress.setVisibility(View.GONE);
                for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                    taskListRV.setVisibility(View.VISIBLE);
                    mProgress.setVisibility(View.GONE);

                    String title = dataSnap.child("title").getValue().toString() ;
                    String detail = dataSnap.child("detail").getValue().toString() ;

                    taskList.add(new Task(title, detail)) ;

                }
                if(taskList.size() >0){
                    taskListRV.setVisibility(View.VISIBLE);
                    mErrorMsgTV.setVisibility(View.GONE);
                }else{
                    taskListRV.setVisibility(View.GONE);
                    mErrorMsgTV.setVisibility(View.VISIBLE);
                }
                taskListAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void Initialize(View view) {
        taskListRV = view.findViewById(R.id.recycler_task_list) ;
        mErrorMsgTV = view.findViewById(R.id.text_error) ;
        mProgress = view.findViewById(R.id.progress) ;
    }
}