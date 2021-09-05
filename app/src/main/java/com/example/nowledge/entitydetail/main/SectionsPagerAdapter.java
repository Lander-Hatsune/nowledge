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
import com.example.nowledge.entitydetail.EntityRelation;
import com.example.nowledge.R;

import org.json.JSONArray;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;
    JSONArray mProperties = null;
    JSONArray mContents = null;
    JSONArray mQuestions = null;

    public SectionsPagerAdapter(Context context, FragmentManager fm, JSONArray properties, JSONArray contents, JSONArray questions) {
        super(fm);
        mContext = context;
        mProperties=properties;
        mContents=contents;
        mQuestions=questions;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fregment=null;
        switch(position){
            case 0:
                fregment=new EntityCharacter();
                Bundle bundle0=new Bundle();
                break;
            case 1:
                fregment=new EntityRelation();
                break;
            case 2:
                fregment=new EntityQuestion();
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