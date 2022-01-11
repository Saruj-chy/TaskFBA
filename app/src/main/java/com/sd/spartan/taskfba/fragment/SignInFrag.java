package com.sd.spartan.taskfba.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sd.spartan.taskfba.interfaces.OnIntent;
import com.sd.spartan.taskfba.R;


public class SignInFrag extends Fragment {
    private OnIntent mOnIntent;
    private TextInputEditText mEmailTET, mPasswordTET ;
    private Button mSignInBtn ;
    private TextView mSIgnUpTV;

    private FirebaseAuth mAuth;
    private String mCurrentUserId;
    private FirebaseUser mCurrentUSer;
    private ProgressDialog mLoadingBar;


    public SignInFrag() {
    }
    public void newInstance(OnIntent onIntent) {
        this.mOnIntent = onIntent ;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        Initialize(view) ;
        mAuth = FirebaseAuth.getInstance();
        mCurrentUSer = mAuth.getCurrentUser() ;


        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllowUserToLogn();
            }
        });


        mSIgnUpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnIntent.OnClick(1);
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
            mLoadingBar.setTitle("Sign In");
            mLoadingBar.setMessage("Please wait....");
            mLoadingBar.setCanceledOnTouchOutside(true);
            mLoadingBar.show();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                mCurrentUserId = mAuth.getCurrentUser().getUid() ;

                                mOnIntent.OnClick(3);
                                mLoadingBar.cancel();

                            } else{
                                Toast.makeText(getContext(), "Not Authorized", Toast.LENGTH_SHORT).show();
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

        mLoadingBar = new ProgressDialog(getContext());

    }
}