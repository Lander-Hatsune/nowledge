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
    String mProperties = "";
    String mContents = "";
    String mQuestions = "";
    String mcourse= "";

    public SectionsPagerAdapter(Context context, FragmentManager fm, JSONArray properties, JSONArray contents, JSONArray questions,String course) {
        super(fm);
        mContext = context;
        try{
            mProperties=properties.toString();
        }catch (NullPointerException e){}
        try{
            mContents=contents.toString();
        }catch (NullPointerException e){}
        try{
            mQuestions=questions.toString();
        }catch (NullPointerException e){}
        mcourse=course;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fregment=null;
        switch(position){
            case 0:
                fregment=new EntityCharacter();
                Bundle bundle0=new Bundle();
                bundle0.putString("character",mProperties);
                fregment.setArguments(bundle0);
                break;
            case 1:
                fregment=new EntityRelation();
                Bundle bundle1=new Bundle();
                bundle1.putString("content",mContents);
                bundle1.putString("course",mcourse);
                fregment.setArguments(bundle1);
                break;
            case 2:
                fregment=new EntityQuestion();
                Bundle bundle2=new Bundle();
                bundle2.putString("character",mQuestions);
                fregment.setArguments(bundle2);
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
        // Show 3 total pages.
        return 3;
    }
}