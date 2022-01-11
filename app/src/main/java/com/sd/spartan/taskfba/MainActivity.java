package com.sd.spartan.taskfba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sd.spartan.taskfba.fragment.SignInFrag;
import com.sd.spartan.taskfba.fragment.SignUpFrag;
import com.sd.spartan.taskfba.fragment.TaskListFragment;
import com.sd.spartan.taskfba.fragment.UserFrag;
import com.sd.spartan.taskfba.interfaces.OnIntent;

public class MainActivity extends AppCompatActivity implements OnIntent {

    private FirebaseUser mCurrentUserId;
    private FirebaseAuth mAuth;

    private boolean mExitBool = false ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser() ;

        if(mCurrentUserId != null){
            OnClick(3);
        }else{
            OnClick(2);
        }

    }

    @Override
    public void OnClick(int num) {
        mExitBool = false ;
        if(num == 1){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            SignUpFrag subFragment = new SignUpFrag();
            subFragment.newInstance(this);
            transaction.replace(R.id.frame_layout, subFragment,"sign_up");
            transaction.addToBackStack(null);
            transaction.commit();
        }else if(num == 2){
            mExitBool = true ;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            SignInFrag subFragment = new SignInFrag();
            subFragment.newInstance(this);
            transaction.replace(R.id.frame_layout, subFragment,"sign_in");
            transaction.addToBackStack(null);
            transaction.commit();
        }else if(num == 3){
            mExitBool = true ;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            UserFrag subFragment = new UserFrag();
            subFragment.newInstance(this);
            transaction.replace(R.id.frame_layout, subFragment,"user_all");
            transaction.addToBackStack(null);
            transaction.commit();
        }else if(num == 4){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            TaskListFragment taskListFragment = new TaskListFragment();
            transaction.replace(R.id.frame_layout, taskListFragment,"task_list");
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_logout){
           mAuth.signOut();
           OnClick(2);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if(mCurrentUserId != null && mExitBool){
            moveTaskToBack(true) ;
        }else if(mCurrentUserId == null && mExitBool){
            moveTaskToBack(true) ;
        }else if(mExitBool){
            moveTaskToBack(true) ;
        }else{
            mExitBool = true ;
            super.onBackPressed();
        }
    }
}