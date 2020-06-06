package com.spark.szhb_master.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.Html;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.szhb_master.R;
import com.spark.szhb_master.entity.BaseInfo;
import com.spark.szhb_master.entity.MessageBean;

import java.util.List;

/**
 * Created by Administrator on 2018/2/6.
 */

public class FreshTeacherAdapter extends BaseQuickAdapter<BaseInfo.NewcomerBean, BaseViewHolder> {
    private Context context;

    public FreshTeacherAdapter(@LayoutRes int layoutResId, @Nullable List<BaseInfo.NewcomerBean> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, BaseInfo.NewcomerBean item) {
        helper.setText(R.id.tvTitle, item.getName()).setText(R.id.tvDesc,item.getSynopsis());
    }
}
