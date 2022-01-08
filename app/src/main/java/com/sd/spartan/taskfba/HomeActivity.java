package com.sd.spartan.taskfba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.sd.spartan.taskfba.fragment.SignInFrag;
import com.sd.spartan.taskfba.fragment.SignUpFrag;

public class HomeActivity extends AppCompatActivity implements OnIntent {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    }


    @Override
    public void OnClick(int num) {

    }
}