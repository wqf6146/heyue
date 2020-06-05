package com.spark.szhb_master.activity.mygift;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.PagerAdapter;
import com.spark.szhb_master.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MyGiftActivity extends BaseActivity {

    @BindView(R.id.rlhead)
    RelativeLayout rlhead;

    @BindView(R.id.tablayout)
    TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @Override
    protected void initView() {
        super.initView();
        initTabLayout();
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @OnClick({R.id.ar_iv_close})
    @Override
    protected void setOnClickListener(View v) {
        switch (v.getId()){
            case R.id.ar_iv_close:
                finish();
                break;
        }
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_mygift;
    }

    private List<Fragment> mTabFragments = new ArrayList<>();

    private void initTabLayout() {
        mTabFragments.add(MyGiftListFragment.getInstance(0));
        mTabFragments.add(MyGiftListFragment.getInstance(1));

        tabs.add("待使用");
        tabs.add("历史");

        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), mTabFragments, tabs));
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
        initTabView();
    }

    private List<String> tabs = new ArrayList<>();
    private void initTabView() {
        for (int i = 0; i < tabs.size(); i++) {
            //获取tab
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            //给tab设置自定义布局
            TextView textView = new TextView(this);
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
                viewPager.setCurrentItem(tab.getPosition(),false);
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
}
