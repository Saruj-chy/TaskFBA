package com.sd.spartan.taskfba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.sd.spartan.taskfba.adapter.PagerAdapter;
import com.sd.spartan.taskfba.fragment.SignInFrag;
import com.sd.spartan.taskfba.fragment.SignUpFrag;

public class MainActivity extends AppCompatActivity implements OnIntent {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Initialize() ;

        OnClick(2);




    }
    private void Initialize() {
    }


    @Override
    public void OnClick(int num) {
        if(num == 1){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            SignUpFrag subFragment = new SignUpFrag(this);
            transaction.replace(R.id.frame_layout, subFragment,"signup");
            transaction.addToBackStack(null);
            transaction.commit();
        }else if(num == 2){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            SignInFrag subFragment = new SignInFrag(this);
            transaction.replace(R.id.frame_layout, subFragment,"signin");
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}