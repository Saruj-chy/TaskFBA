package com.sd.spartan.taskfba.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sd.spartan.taskfba.R;
import com.sd.spartan.taskfba.adapter.UserListAdapter;
import com.sd.spartan.taskfba.model.User;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserFrag extends Fragment {
    private ConstraintLayout mAdminCons, mAllUserCons ;
    private RecyclerView mUserAllRV ;
    private TextView mNameTV, mEmailTV, mAgeTV, mDOBTV;
    private ProgressBar mProgress ;

    private DatabaseReference userRef;
    private String currentUserId ;

    private List<User> userList ;
    UserListAdapter userListAdapter ;

    public UserFrag() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_user, container, false);

        Initialize(view) ;
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid() ;
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        userList = new ArrayList<>() ;
        userListAdapter = new UserListAdapter(getContext(), userList ) ;
        mUserAllRV.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mUserAllRV.setAdapter(userListAdapter);
        userListAdapter.notifyDataSetChanged();

        Log.e("TAG","currentUserId:"+ currentUserId ) ;




        return view ;
    }

    private void Initialize(View view) {
        mAdminCons = view.findViewById(R.id.constraint_admin) ;
        mAllUserCons = view.findViewById(R.id.constraint_all_user) ;
        mProgress = view.findViewById(R.id.progress) ;
        mNameTV = view.findViewById(R.id.text_name) ;
        mEmailTV = view.findViewById(R.id.text_email) ;
        mAgeTV = view.findViewById(R.id.text_age) ;
        mDOBTV = view.findViewById(R.id.text_dof) ;
        mUserAllRV = view.findViewById(R.id.recycler_all_user) ;
    }

    @Override
    public void onStart() {
        super.onStart();


        userList.clear();


        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProgress.setVisibility(View.GONE);
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String name = postSnapshot.child("name").getValue().toString() ;
                    String email = postSnapshot.child("email").getValue().toString() ;
                    String password = postSnapshot.child("password").getValue().toString() ;
                    String age = postSnapshot.child("age").getValue().toString() ;
                    String date_of_birth = postSnapshot.child("date_of_birth").getValue().toString() ;

                    if(postSnapshot.getKey().equalsIgnoreCase(currentUserId)){
                        mAdminCons.setVisibility(View.VISIBLE);
                        mNameTV.setText(name);
                        mEmailTV.setText(email);
                        mAgeTV.setText("Age:"+age);
                        mDOBTV.setText("Date of Birth:"+date_of_birth);
                    }else{
                        mAllUserCons.setVisibility(View.VISIBLE);
                        userList.add(new User(
                                name, email, password, age, date_of_birth
                        )) ;
                    }


                }
                Log.e("TAG","user list:"+ userList.size() ) ;
                userListAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });










    }


}