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
import com.spark.szhb_master.entity.NewEntrust;
import com.spark.szhb_master.utils.DateUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

/**
 * 委托
 * author: wuzongjie
 * time  : 2018/4/17 0017 10:18
 * desc  : 当前委托的适配器
 */

public class C2cOrderAdapter extends BaseQuickAdapter<C2cOrder.ListBean, BaseViewHolder> {
    private int type = 0; //0- 1- 2
    private Context context;
    public C2cOrderAdapter(Context context, @Nullable List<C2cOrder.ListBean> data) {
        super(R.layout.item_c2corder, data);
        this.context = context;
    }

    public void setType(int type){
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, C2cOrder.ListBean item) {
        if (item.getType() == 0) {
            Drawable drawableLeft = context.getResources().getDrawable(
                    R.mipmap.ic_buy);

            ((TextView)helper.getView(R.id.tvSymbol)).setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                    null, null, null);
        }else {
            Drawable drawableLeft = context.getResources().getDrawable(
                    R.mipmap.ic_sell);

            ((TextView)helper.getView(R.id.tvSymbol)).setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                    null, null, null);
        }

        switch (item.getStatus()){
            case 0:
                helper.setText(R.id.tvOrderStatus,"待付款");
                break;
            case 1:
                helper.setText(R.id.tvOrderStatus,"待对方付款");
                break;
            case 2:
                helper.setText(R.id.tvOrderStatus,"待放币");
                break;
            case 3:
                helper.setText(R.id.tvOrderStatus,"待对方放币");
                break;

        }

        helper.setText(R.id.tvDate, item.getCreated_at());
        helper.setText(R.id.tvPrice, String.valueOf(item.getPrice()));
        helper.setText(R.id.tvNum, String.valueOf(item.getNum()));
        BigDecimal p = new BigDecimal(item.getPrice());
        BigDecimal n = new BigDecimal(item.getNum());
        helper.setText(R.id.tvTotalPrice, p.multiply(n).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
    }
}
