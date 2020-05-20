package com.spark.szhb_master.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.mycc.MyccActivity;

import java.util.List;

public class TabFragmentAdapter extends FragmentPagerAdapter {

    private List<String> mTitles;
    private Context mContext;
    private List<Fragment> mFragments;

    public TabFragmentAdapter(List<Fragment> fragments, List<String> titles, FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
        this.mFragments = fragments;
        this.mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_tabview, null);
        TextView textView=view.findViewById(R.id.tvItem);
        textView.setText(mTitles.get(position));
        return view;
    }
}