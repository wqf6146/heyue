package com.spark.szhb_master.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.szhb_master.R;
import com.spark.szhb_master.entity.ExtAddressEntity;
import com.spark.szhb_master.entity.NewEntrust;
import com.spark.szhb_master.utils.DateUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 委托
 * author: wuzongjie
 * time  : 2018/4/17 0017 10:18
 * desc  : 当前委托的适配器
 */

public class ExtAddressAdapter extends BaseQuickAdapter<ExtAddressEntity.ListBean, BaseViewHolder> {

    public ExtAddressAdapter(@Nullable List<ExtAddressEntity.ListBean> data) {
        super(R.layout.item_extaddress, data);
    }



    @Override
    protected void convert(BaseViewHolder helper, ExtAddressEntity.ListBean item) {
        helper.setText(R.id.tvAddress,item.getAddress())
                .setText(R.id.tvRemark,item.getName())
                .setText(R.id.tvType,item.getType() == 0 ? "USDT-ERC20" : "USDT-Omni");
    }
}
