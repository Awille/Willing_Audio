package com.example.wille.willing_audio.Adapter_And_Service;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/12/27.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    public final int COUNT = 3;
    private List<Fragment> mFragments;
    private String[] titles = new String[]{"Tab1", "Tab2", "Tab3"};
    private Context context;

    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments, Context context) {
        super(fm);
        this.context = context;
        mFragments=fragments;
    }
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }


}
