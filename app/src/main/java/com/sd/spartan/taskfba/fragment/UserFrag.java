package com.sd.spartan.taskfba.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sd.spartan.taskfba.interfaces.OnIntent;
import com.sd.spartan.taskfba.R;
import com.sd.spartan.taskfba.adapter.UserListAdapter;
import com.sd.spartan.taskfba.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserFrag extends Fragment {
    private ConstraintLayout mAdminCons, mAllUserCons ;
    private RecyclerView mUserAllRV ;
    private TextView mNameTV, mEmailTV, mAgeTV, mDOBTV, mErrorMsgTV;
    private ProgressBar mProgress ;
    private FloatingActionButton mFabBtn ;
    private SwitchCompat switchCompat ;

    private DatabaseReference DBRef, mUserRef, mTaskRef;
    private String mCurrentUserId;

    private List<User> mUserList;
    UserListAdapter mUserListAdapter;
    private OnIntent mOnIntent;

    private MaterialAlertDialogBuilder materialAlertDialogBuilder ;
    private AlertDialog mAlertDialog;


    public UserFrag() {
    }
    public void newInstance(OnIntent onIntent) {
        this.mOnIntent = onIntent ;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_user, container, false);

        Toolbar mToolbar = view.findViewById(R.id.appbar_user) ;
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.app_name) ;

        Initialize(view) ;
        mCurrentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid() ;
        DBRef = FirebaseDatabase.getInstance().getReference();
        mUserRef = DBRef.child("Users");
        mTaskRef = DBRef.child("Task").child(mCurrentUserId);

        mUserList = new ArrayList<>() ;
        mUserListAdapter = new UserListAdapter(getContext(), mUserList) ;
        mUserAllRV.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mUserAllRV.setAdapter(mUserListAdapter);
        mUserListAdapter.notifyDataSetChanged();


        mFabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnPopUpClick() ;
            }
        });

        mAdminCons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mOnIntent.OnClick(4);

            }
        });

        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(switchCompat.isChecked()){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
            }
        });


        return view ;
    }

    private void OnPopUpClick() {
        View mView = getLayoutInflater().inflate(R.layout.popup_add,  null);
        TextInputEditText titleTET = mView.findViewById(R.id.edit_title_layout) ;
        TextInputEditText detailTET = mView.findViewById(R.id.edit_detail_layout) ;

        Button submitBtn = mView.findViewById(R.id.btn_combine) ;
        ProgressBar progressBar = mView.findViewById(R.id.progress) ;



        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleTET.getText().toString().trim() ;
                String details = detailTET.getText().toString().trim() ;
                if(title.equalsIgnoreCase("") || details.equalsIgnoreCase("")){
                    Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    submitBtn.setVisibility(View.GONE);


                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("title",title);
                    hashMap.put("detail",details);

                    final String pushId = FirebaseDatabase.getInstance().getReference().push().getKey();
                    mTaskRef.child(pushId).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mAlertDialog.cancel();
                            }
                        }
                    });

                }



            }
        });


        materialAlertDialogBuilder.setView(mView);
        mAlertDialog = materialAlertDialogBuilder.create();
        mAlertDialog.show();
        mAlertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void Initialize(View view) {
        mAdminCons = view.findViewById(R.id.constraint_admin) ;
        mAllUserCons = view.findViewById(R.id.constraint_all_user) ;
        mProgress = view.findViewById(R.id.progress) ;
        mFabBtn = view.findViewById(R.id.fab_add) ;
        mNameTV = view.findViewById(R.id.text_name) ;
        mEmailTV = view.findViewById(R.id.text_email) ;
        mAgeTV = view.findViewById(R.id.text_age) ;
        mDOBTV = view.findViewById(R.id.text_dof) ;
        mErrorMsgTV = view.findViewById(R.id.text_error) ;
        mUserAllRV = view.findViewById(R.id.recycler_all_user) ;
        switchCompat = view.findViewById(R.id.swich_theme) ;

        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialog_rounded) ;

    }

    @Override
    public void onStart() {
        super.onStart();

        mUserList.clear();
        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProgress.setVisibility(View.GONE);
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String name = postSnapshot.child("name").getValue().toString() ;
                    String email = postSnapshot.child("email").getValue().toString() ;
                    String password = postSnapshot.child("password").getValue().toString() ;
                    String age = postSnapshot.child("age").getValue().toString() ;
                    String date_of_birth = postSnapshot.child("date_of_birth").getValue().toString() ;

                    if(postSnapshot.getKey().equalsIgnoreCase(mCurrentUserId)){
                        mAdminCons.setVisibility(View.VISIBLE);
                        mNameTV.setText(name);
                        mEmailTV.setText(email);
                        mAgeTV.setText("Age:"+age);
                        mDOBTV.setText("Date of Birth:"+date_of_birth);
                    }else{
                        mAllUserCons.setVisibility(View.VISIBLE);
                        mUserList.add(new User(
                                name, email, password, age, date_of_birth
                        )) ;
                    }


                }
                mAllUserCons.setVisibility(View.VISIBLE);
                if(mUserList.size() >0){
                    mUserAllRV.setVisibility(View.VISIBLE);
                    mErrorMsgTV.setVisibility(View.GONE);
                }else{
                    mUserAllRV.setVisibility(View.GONE);
                    mErrorMsgTV.setVisibility(View.VISIBLE);
                }
                mUserListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


}