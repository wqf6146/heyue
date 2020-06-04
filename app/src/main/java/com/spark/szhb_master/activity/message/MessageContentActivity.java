package com.spark.szhb_master.activity.message;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.MessageAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.MessageBean;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.NetCodeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import config.Injection;

/**
 * 平台消息
 */
public class MessageContentActivity extends BaseActivity  {

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvTime)
    TextView tvTime;

    @BindView(R.id.tvBody)
    TextView tvBody;

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_message_content;
    }

    @Override
    protected void initView() {
        String time = getIntent().getStringExtra("time");
        String title = getIntent().getStringExtra("title");
        String body = getIntent().getStringExtra("body");

        tvTime.setText(time);
        tvTitle.setText(title);
        tvBody.setText(body);

    }

    @Override
    protected void setListener() {
        super.setListener();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
