package com.spark.szhb_master.activity.mycc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CcFragment extends Fragment {

    protected View rootView;

    Unbinder unbinder;

    @BindView(R.id.ccTablayout)
    TabLayout mTablayout;

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    private List<Fragment> mTabFragments;

    public static CcFragment getInstance() {
        CcFragment fragment = new CcFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_mycc_cc, null);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);


        initTabViewpager();
    }

    private DqccFragment dqccFragment,lsccFragment;

    private void initTabViewpager() {
        mTabFragments = new ArrayList<>();

        dqccFragment= DqccFragment.getInstance(0);
        dqccFragment.setOnCallBackEvent(new DqccFragment.OnCallBackEvent() {
            @Override
            public void undersellContratSuccess() {
            }

            @Override
            public void showEmpty(boolean isShow) {
            }
        });
        mTabFragments.add(dqccFragment);

        lsccFragment = DqccFragment.getInstance(3);
        mTabFragments.add(lsccFragment);

        List<String> tabs = new ArrayList<>();
        tabs.add("持仓中");
        tabs.add("历史");

        mViewPager.setAdapter(new PagerAdapter(getChildFragmentManager(), mTabFragments, tabs));
        mViewPager.setOffscreenPageLimit(2);
        mTablayout.setTabMode(TabLayout.MODE_FIXED);
        mTablayout.setupWithViewPager(mViewPager);
        mTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition(),false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void refresh(){
        if (dqccFragment!=null && lsccFragment!=null){
            dqccFragment.doRefresh();
            lsccFragment.doRefresh();
        }
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
