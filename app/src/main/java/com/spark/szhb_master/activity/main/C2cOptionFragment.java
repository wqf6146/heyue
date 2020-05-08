package com.spark.szhb_master.activity.main;

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

public class C2cOptionFragment extends Fragment {

    protected View rootView;

    Unbinder unbinder;

    @BindView(R.id.tablayout)
    TabLayout tabLayout;

    @BindView(R.id.tvOrder)
    TextView tvOrder;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private List<Fragment> mTabFragments = new ArrayList<>();

    public static C2cOptionFragment newInstance(){
        C2cOptionFragment fragment = new C2cOptionFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_c2cfast, null);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);

        initTabLayout();
    }

    private List<String> tabs = new ArrayList<>();

    private void initTabLayout() {
        mTabFragments.add(C2CListFragment.getInstance(0));
        mTabFragments.add(C2CListFragment.getInstance(1));
        mTabFragments.add(C2CListFragment.getInstance(2));

        tabs.add("我买");
        tabs.add("我卖");
        tabs.add("委托");

        viewPager.setAdapter(new PagerAdapter(getChildFragmentManager(), mTabFragments, tabs));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
        initTabView();
    }

    private void initTabView() {
        for (int i = 0; i < tabs.size(); i++) {
            //获取tab
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            //给tab设置自定义布局
            TextView textView = new TextView(getContext());
            textView.setGravity(Gravity.CENTER);
            tab.setCustomView(textView);
            //填充数据
            textView.setText(String.valueOf(tabs.get(i)));
            //默认选择第一项
            if (i == 0) {
                textView.setSelected(true);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
                textView.setTextColor(getResources().getColor(R.color.white));
            }else{
                textView.setSelected(false);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
                textView.setTextColor(getResources().getColor(R.color.tab_font));
            }
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView textView = (TextView)tab.getCustomView();
                textView.setSelected(true);
                //设置选中后的字体大小
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
                textView.setTextColor(getResources().getColor(R.color.white));
                //关联Viewpager
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView textView = (TextView)tab.getCustomView();
                textView.setSelected(false);
                //恢复默认字体大小
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
                textView.setTextColor(getResources().getColor(R.color.tab_font));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @Nullable
    @Override
    public View getView() {
        return rootView;
    }
}
