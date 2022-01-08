package com.sd.spartan.taskfba.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sd.spartan.taskfba.MainActivity;
import com.sd.spartan.taskfba.OnIntent;
import com.sd.spartan.taskfba.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class SignUpFrag extends Fragment {

    private TextInputEditText mNameTET, mEmailTET, mPasswordTET, mConfirmPassTET, mAgeTET ;
    private Button mSignUpBtn ;
    private TextView mSigninTV, mBofTV;

    private String mEMail, mPassword, mName, mConfirmPass, mAge, mBOF, mCurrentId ;
    private Calendar calendar;
    private int year, month, day, secID ;

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    OnIntent onIntent ;



    public SignUpFrag(OnIntent onIntent) {
        this.onIntent = onIntent ;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        Initialize(view) ;
        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllTextInput() ;

                if(!mPassword.equalsIgnoreCase(mConfirmPass)){
                    Toast.makeText(getContext(), "Password not match", Toast.LENGTH_SHORT).show();
                }else if(mBOF.equalsIgnoreCase("") || mAge.equalsIgnoreCase("") || mName.equalsIgnoreCase("")) {
                    Toast.makeText(getContext(), "Please fill up all fields", Toast.LENGTH_SHORT).show();
                }else{
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

                onIntent.OnClick(2);
            }
        });

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

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
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String,Object> profileMap = new HashMap<>();
                profileMap.put("name",mName);
                profileMap.put("email",mEMail);
                profileMap.put("password",mPassword);
                profileMap.put("age",mAge);
                profileMap.put("date_of_birth",mBOF);

                userRef.child(mCurrentId)
                        .updateChildren(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.e("TAG", "task:"+task) ;
//                            Intent intent = new Intent(ChatProfileActivity.this,MainActivity.class);
//                            startActivity(intent);
//                            finish();
//                            progressDialog.dismiss();
//                            Toast.makeText(ChatProfileActivity.this,"Profile settings has been updated",Toast.LENGTH_LONG).show();
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
        long maxTime = System.currentTimeMillis();
        long minTime = System.currentTimeMillis() - (3*24*60*60*1000) ;
        Log.e("date_format", "  maxTime:  "  + maxTime ) ;
        Log.e("date_format", "  minTime:  "  + minTime ) ;

        DatePickerDialog datepick = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Calendar selectCalendars = Calendar.getInstance();
                selectCalendars.set(year, month, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String dateFormat = simpleDateFormat.format(selectCalendars.getTime());
                Log.e("date_format", "  dateString:  "  + dateFormat ) ;
                mBofTV.setText(dateFormat);

                mBOF = dateFormat ;


            }
        } ,year,month,day);
        datepick.show();
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
    }
}