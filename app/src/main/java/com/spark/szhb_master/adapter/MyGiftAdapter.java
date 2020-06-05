package com.spark.szhb_master.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.szhb_master.R;
import com.spark.szhb_master.entity.C2cOrder;
import com.spark.szhb_master.entity.FreshGitBean;

import java.math.BigDecimal;
import java.util.List;

/**
 * 委托
 * author: wuzongjie
 * time  : 2018/4/17 0017 10:18
 * desc  : 当前委托的适配器
 */

public class MyGiftAdapter extends BaseQuickAdapter<FreshGitBean.Gift, BaseViewHolder> {
    private int type = 0; //0- 1
    private Context context;
    private doUseGiftCallback doUseGiftCallback;

    public void setDoUseGiftCallback(MyGiftAdapter.doUseGiftCallback doUseGiftCallback) {
        this.doUseGiftCallback = doUseGiftCallback;
    }

    public MyGiftAdapter(Context context, @Nullable List<FreshGitBean.Gift> data) {
        super(R.layout.item_mygift, data);
        this.context = context;
    }

    public void setType(int type){
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, FreshGitBean.Gift item) {
        helper.setText(R.id.tvNum,String.valueOf(item.getNum()))
                .setText(R.id.tvTitle,item.getName())
                .setText(R.id.tvDesc,item.getRemark());

        if (type == 0){
//            helper.getView(R.id.tvStatus).setVisibility(View.VISIBLE);
            helper.setText(R.id.tvTime,item.getCreated_at() + " 到期")
                    .setText(R.id.tvStatus,"去使用");

            helper.getView(R.id.tvStatus).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (doUseGiftCallback!=null)
                        doUseGiftCallback.doUseGift(item.getId());
                }
            });
        }else {
            helper.setText(R.id.tvTime,"使用时间：" + item.getUsage_at())
                    .setText(R.id.tvStatus,"已使用");;
//            helper.getView(R.id.tvStatus).setVisibility(View.GONE);
        }
    }


    public interface doUseGiftCallback {
        void doUseGift(int id);
    }
}
