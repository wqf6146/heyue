package com.spark.szhb_master.activity.mycc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WtFragment extends Fragment {

    protected View rootView;

    Unbinder unbinder;

    @BindView(R.id.wtTablayout)
    TabLayout mTablayout;

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    public static WtFragment getInstance() {
        WtFragment fragment = new WtFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_mycc_wt, null);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);


        initTabViewpager();
        
    }
    private List<Fragment> mTabFragments;
    private DqwtFragment dqwtFragment,lswtFragment;
    private List<String> tabs = new ArrayList<>();
    private void initTabViewpager() {
        mTabFragments = new ArrayList<>();

        dqwtFragment= DqwtFragment.getInstance(1);
        dqwtFragment.setCallBackEvent(new DqwtFragment.OnCallBackEvent() {
            @Override
            public void undoLimitContratSuccess() {

            }

            @Override
            public void showEmpty(boolean isShow) {

            }
        });
        mTabFragments.add(dqwtFragment);

        lswtFragment = DqwtFragment.getInstance(2);
        mTabFragments.add(lswtFragment);


        tabs.add("委托中");
        tabs.add("历史");

        mViewPager.setAdapter(new PagerAdapter(getChildFragmentManager(), mTabFragments, tabs));
        mViewPager.setOffscreenPageLimit(2);
        mTablayout.setTabMode(TabLayout.MODE_FIXED);
        mTablayout.setupWithViewPager(mViewPager);
//        mTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                mViewPager.setCurrentItem(tab.getPosition(),false);
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
        initTabView();
    }

    private void initTabView() {
        for (int i = 0; i < tabs.size(); i++) {
            //获取tab
            TabLayout.Tab tab = mTablayout.getTabAt(i);
            //给tab设置自定义布局
            TextView textView = new TextView(getContext());
            textView.setGravity(Gravity.CENTER);
            tab.setCustomView(textView);
            //填充数据
            textView.setText(String.valueOf(tabs.get(i)));
            //默认选择第一项
            if (i == 0){
                textView.setSelected(true);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
                textView.setTextColor(getResources().getColor(R.color.white));
            }else{
                textView.setSelected(false);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
                textView.setTextColor(getResources().getColor(R.color.tab_font));
            }
        }

        mTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView textView = (TextView)tab.getCustomView();
                textView.setSelected(true);
                //设置选中后的字体大小
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
                textView.setTextColor(getResources().getColor(R.color.white));
                //关联Viewpager
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView textView = (TextView)tab.getCustomView();
                textView.setSelected(false);
                //恢复默认字体大小
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
                textView.setTextColor(getResources().getColor(R.color.tab_font));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public void refresh(){
        if (dqwtFragment!=null && lswtFragment!=null){
            dqwtFragment.doRefresh();
            lswtFragment.doRefresh();
        }
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

}
