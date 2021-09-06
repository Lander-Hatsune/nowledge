package com.example.nowledge.entitydetail.main;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.nowledge.entitydetail.EntityCharacter;
import com.example.nowledge.entitydetail.EntityQuestion;
import com.example.nowledge.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */

class NewPagerAdapter extends FragmentManager{

}
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;
    String mCourse, mName;

    public SectionsPagerAdapter(Context context, FragmentManager fm, String name, String course) {
        super(fm);
        mContext = context;
        mCourse = course;
        mName = name;

    }

    @Override
    public Fragment getItem(int position) {
        Fragment fregment=null;
        Bundle bundle = new Bundle();
        bundle.putString("course", mCourse);
        bundle.putString("name", mName);
        switch(position){
            case 0:
                fregment = new EntityCharacter();
                fregment.setArguments(bundle);
                break;
            case 1:
                fregment = new EntityQuestion();
                fregment.setArguments(bundle);
                break;
        }
        return fregment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}