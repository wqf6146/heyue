package com.spark.szhb_master.activity.delegate;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.PagerAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PostDelegateActivity extends BaseActivity {

    @BindView(R.id.tablayout)
    TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.rlhead)
    RelativeLayout rlhead;

    @BindView(R.id.ar_iv_close)
    ImageView ivClose;

    private List<Fragment> mTabFragments = new ArrayList<>();
    private List<String> mTabTitlis = new ArrayList<>();
    private PostDelegateFragment mBuyFragment,mSellFragment;
    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_postdelegate;
    }

    @Override
    protected void initView() {
        super.initView();
        setImmersionBar(rlhead);
        initTabViewpager();

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initTabViewpager() {
        mBuyFragment= PostDelegateFragment.newInstance(0);
        mBuyFragment.setOnCallBackEvent(new PostDelegateFragment.OnCallBackEvent() {
            @Override
            public void submitSuccess(String obj) {
                ToastUtils.showToast(obj);
                finish();
            }
        });
        mTabFragments.add(mBuyFragment);

        mSellFragment = PostDelegateFragment.newInstance(1);
        mSellFragment.setOnCallBackEvent(new PostDelegateFragment.OnCallBackEvent() {
            @Override
            public void submitSuccess(String obj) {
                ToastUtils.showToast(obj);
                finish();
            }
        });
        mTabFragments.add(mSellFragment);

        mTabTitlis.add("购买USDT");
        mTabTitlis.add("出售USDT");

        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), mTabFragments, mTabTitlis));
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
    }

}
