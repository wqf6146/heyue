package com.spark.szhb_master.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.szhb_master.R;
import com.spark.szhb_master.entity.MessageBean;
import com.spark.szhb_master.utils.StringUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/2/6.
 */

public class MessageAdapter extends BaseQuickAdapter<MessageBean.Message, BaseViewHolder> {
    private Context context;

    public MessageAdapter(@LayoutRes int layoutResId, @Nullable List<MessageBean.Message> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageBean.Message item) {
        helper.setText(R.id.tvTitle, item.getTitle()).setText(R.id.tvContent, Html.fromHtml(item.getBody()))
                .setText(R.id.tvTime, item.getCreated_at());
    }
}
