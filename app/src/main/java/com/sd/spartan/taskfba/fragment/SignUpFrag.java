package com.sd.spartan.taskfba.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sd.spartan.taskfba.interfaces.OnIntent;
import com.sd.spartan.taskfba.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class SignUpFrag extends Fragment {

    private TextInputEditText mNameTET, mEmailTET, mPasswordTET, mConfirmPassTET, mAgeTET ;
    private Button mSignUpBtn ;
    private TextView mSigninTV, mBofTV;

    private String mEMail, mPassword, mName, mConfirmPass, mAge, mBOF, mCurrentId ;
    private Calendar mCalendar;
    private int mYear, month, mDay;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserRef;
    private OnIntent mOnIntent;
    private ProgressDialog mLoadingBar;



    public SignUpFrag() {
    }

    public void newInstance(OnIntent onIntent) {
        this.mOnIntent = onIntent ;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        Initialize(view) ;
        mAuth = FirebaseAuth.getInstance();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllTextInput() ;

                if(!mPassword.equalsIgnoreCase(mConfirmPass)){
                    Toast.makeText(getContext(), "Password not match", Toast.LENGTH_SHORT).show();
                }else if(mBOF.equalsIgnoreCase("") || mAge.equalsIgnoreCase("") || mName.equalsIgnoreCase("")) {
                    Toast.makeText(getContext(), "Please fill up all fields", Toast.LENGTH_SHORT).show();
                }else{
                    mLoadingBar.setTitle("Sign In");
                    mLoadingBar.setMessage("Please wait....");
                    mLoadingBar.setCanceledOnTouchOutside(true);
                    mLoadingBar.show();

                    mAuth.createUserWithEmailAndPassword(mEMail, mPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        mCurrentId = mAuth.getCurrentUser().getUid() ;
                                        Log.e("TAG", "mCurrentId: "+ mCurrentId ) ;

                                        SavedDataRDB() ;

                                    }
                                    else{
                                        String message = task.getException().toString();
                                        Toast.makeText(getContext(), "Error: "+message, Toast.LENGTH_SHORT).show();
                                        Log.e("TAG", "message: "+ message ) ;

                                    }
                                }
                            });
                }



            }
        });

        mBofTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
OnClickDateTime();
            }
        });

        mSigninTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mOnIntent.OnClick(2);
            }
        });

        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        month = mCalendar.get(Calendar.MONTH);
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);

        return view ;
    }

    private void getAllTextInput() {
        mName = mNameTET.getText().toString().trim() ;
        mEMail = mEmailTET.getText().toString().trim() ;
        mPassword = mPasswordTET.getText().toString().trim() ;
        mConfirmPass = mConfirmPassTET.getText().toString().trim() ;
        mAge = mAgeTET.getText().toString().trim() ;
    }

    private void SavedDataRDB() {
        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String,Object> profileMap = new HashMap<>();
                profileMap.put("name",mName);
                profileMap.put("email",mEMail);
                profileMap.put("password",mPassword);
                profileMap.put("age",mAge);
                profileMap.put("date_of_birth",mBOF);

                mUserRef.child(mCurrentId)
                        .setValue(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mLoadingBar.cancel();
                            mOnIntent.OnClick(3);
   }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void OnClickDateTime() {
        DatePickerDialog mDatePick = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Calendar selectCalendars = Calendar.getInstance();
                selectCalendars.set(year, month, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String dateFormat = simpleDateFormat.format(selectCalendars.getTime());
                mBofTV.setText(dateFormat);
                mBOF = dateFormat ;
            }
        } , mYear,month, mDay);
        mDatePick.show();
    }

    private void Initialize(View view) {
        mNameTET = view.findViewById(R.id.edit_name);
        mEmailTET = view.findViewById(R.id.edit_email);
        mPasswordTET = view.findViewById(R.id.edit_password);
        mConfirmPassTET = view.findViewById(R.id.edit_confirm_password);
        mAgeTET = view.findViewById(R.id.edit_age);
        mBofTV = view.findViewById(R.id.text_bof);
        mSignUpBtn = view.findViewById(R.id.btn_sign_up);
        mSigninTV = view.findViewById(R.id.text_sign_in);

        mLoadingBar = new ProgressDialog(getContext());

    }
}