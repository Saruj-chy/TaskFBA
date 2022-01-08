package com.sd.spartan.taskfba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.sd.spartan.taskfba.fragment.SignInFrag;
import com.sd.spartan.taskfba.fragment.SignUpFrag;
import com.sd.spartan.taskfba.fragment.UserFrag;

public class HomeActivity extends AppCompatActivity implements OnIntent {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        UserFrag subFragment = new UserFrag();
        transaction.replace(R.id.frame_layout, subFragment,"user_all");
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void OnClick(int num) {

    }
}