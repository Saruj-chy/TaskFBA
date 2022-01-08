package com.sd.spartan.taskfba.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sd.spartan.taskfba.HomeActivity;
import com.sd.spartan.taskfba.OnIntent;
import com.sd.spartan.taskfba.R;


public class SignInFrag extends Fragment {

    private OnIntent onIntent ;
    private TextInputEditText mEmailTET, mPasswordTET ;
    private Button mSignInBtn ;
    private TextView mSIgnUpTV;

    private FirebaseAuth mAuth;
    private String currentUserId;
    private FirebaseUser currentUSer ;
    private ProgressDialog loadingBar;


    public SignInFrag(OnIntent onIntent) {
        this.onIntent = onIntent ;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        Initialize(view) ;
        mAuth = FirebaseAuth.getInstance();
        currentUSer = mAuth.getCurrentUser() ;


        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllowUserToLogn();
            }
        });


        mSIgnUpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onIntent.OnClick(1);
            }
        });
        return view ;
    }
    private void AllowUserToLogn() {
        String email = mEmailTET.getText().toString();
        final String password = mPasswordTET.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(getContext(), "Please enter your email..", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(getContext(), "Please enter your password..", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Sign In");
            loadingBar.setMessage("Please wait....");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                currentUserId = mAuth.getCurrentUser().getUid() ;

                                startActivity(new Intent(getContext(), HomeActivity.class));


                            } else{
                                String message = task.getException().toString();
                                Toast.makeText(getContext(), "Error: "+message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }

    private void Initialize(View view) {
        mEmailTET = view.findViewById(R.id.edit_email);
        mPasswordTET = view.findViewById(R.id.edit_password);
        mSignInBtn = view.findViewById(R.id.btn_sign_in);
        mSIgnUpTV = view.findViewById(R.id.text_sign_up);

        loadingBar = new ProgressDialog(getContext());

    }
}