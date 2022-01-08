package com.sd.spartan.taskfba.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.sd.spartan.taskfba.fragment.SignInFrag;
import com.sd.spartan.taskfba.fragment.SignUpFrag;

public class PagerAdapter extends FragmentStateAdapter {
    private int mNumOfTabs;

    public PagerAdapter(FragmentActivity fa, int mNumOfTabs) {
        super(fa);
        this.mNumOfTabs = mNumOfTabs ;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new SignInFrag(null);
            case 1:
                return new SignUpFrag(null);

            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return mNumOfTabs;
    }
}
