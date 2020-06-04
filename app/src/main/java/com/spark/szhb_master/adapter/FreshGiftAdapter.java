package com.spark.szhb_master.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.szhb_master.R;
import com.spark.szhb_master.entity.FreshGitBean;
import com.spark.szhb_master.entity.MessageBean;
import com.spark.szhb_master.utils.ToastUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/2/6.
 */

public class FreshGiftAdapter extends BaseQuickAdapter<FreshGitBean.Gift, BaseViewHolder> {
    private Context context;
    private doGetGiftCallback doGetGiftCallback;
    public FreshGiftAdapter(@LayoutRes int layoutResId, @Nullable List<FreshGitBean.Gift> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }


    public void setDoGetGiftCallback(FreshGiftAdapter.doGetGiftCallback doGetGiftCallback) {
        this.doGetGiftCallback = doGetGiftCallback;
    }

    @Override
    protected void convert(BaseViewHolder helper, FreshGitBean.Gift item) {
        helper.setText(R.id.tvTitle, item.getName()).setText(R.id.tvContent, item.getRemark())
                .setText(R.id.tvNum, item.getNum() + " USDT");
        if (item.getIs_received() == 1){
            helper.setText(R.id.tvStatus,"待领取");
        }else if (item.getIs_received() == 2){
            helper.setText(R.id.tvStatus,"已领取");
        }

        helper.getView(R.id.tvStatus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getIs_received() == 2){
                    ToastUtils.showToast("已领取");
                }else if (item.getIs_received() == 0){
                    ToastUtils.showToast("不可领取");
                }else if (item.getIs_received() == 1){
                   if (doGetGiftCallback!=null)
                       doGetGiftCallback.doGetGift(item.getId());
                }
            }
        });
    }

    public interface doGetGiftCallback {
        void doGetGift(int id);
    }
}
