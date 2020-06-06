package com.spark.szhb_master.activity.aboutus;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.FreshTeacherAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.BaseInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class FreshTeacherActivity extends BaseActivity {
    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.recycleview)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    FreshTeacherAdapter adapter;
    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_freshteacher;
    }


    @Override
    protected void initView() {
        super.initView();

    }

    @Override
    protected void initData() {
        super.initData();

        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableRefresh(false);
        BaseInfo baseInfo = MyApplication.getApp().getBaseInfo();
        List<BaseInfo.NewcomerBean> newcomer = baseInfo.getNewcomer();

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new FreshTeacherAdapter(R.layout.item_freshteacher, newcomer, this);
        adapter.bindToRecyclerView(recyclerView);
    }

    @OnClick({R.id.ivBack})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
        }
    }
}
