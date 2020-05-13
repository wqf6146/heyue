package com.spark.szhb_master.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.szhb_master.R;
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

public class TrustAdapter extends BaseQuickAdapter<NewEntrust.ListBean, BaseViewHolder> {
    private int type = 0; //0-当前持仓 1-当前委托 2历史委托
    public TrustAdapter(@Nullable List<NewEntrust.ListBean> data) {
        super(R.layout.item_trust, data);
    }

    public void setType(int type){
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, NewEntrust.ListBean item) {
        if (item.getType() == 0) {
            helper.setText(R.id.trust_type, "多").setBackgroundRes(R.id.trust_type,R.drawable.bg_green_corner);
        } else {
            helper.setText(R.id.trust_type, "空").setBackgroundRes(R.id.trust_type,R.drawable.bg_red_corner);
        }
        if (type == 0){
            helper.setText(R.id.trust_ones,"持仓数量");
            helper.setText(R.id.trust_twos,"持仓价格");
        }else{
            helper.setText(R.id.trust_ones,"委托数量");
            helper.setText(R.id.trust_twos,"委托价格");
        }
        helper.setText(R.id.tv_symbol, item.getMark());
        String time = DateUtils.getFormatTime(null, new Date( new Long(new Long(item.getCreated_at()) * 1000)));
        helper.setText(R.id.trust_time, time);
        helper.setText(R.id.trust_num, item.getNum() + "手");
        helper.setText(R.id.trust_price, String.valueOf(item.getPrice()));
        helper.setText(R.id.trust_newprice, String.valueOf(item.getNew_price()));

//        NumberFormat nf = NumberFormat.getNumberInstance();
//        nf.setMaximumFractionDigits(2);

        String shouyi = new BigDecimal(new Double(item.getHarvest_num())).setScale(4,BigDecimal.ROUND_HALF_UP).toString();
//        String shouyilv = new BigDecimal(new Double(item.getIncome_rate())).setScale(4,BigDecimal.ROUND_HALF_UP) + "%";

        helper.setText(R.id.trust_shouyi, shouyi);
        helper.setText(R.id.trust_shouyilv, item.getIncome_rate());


        String guoyef = new BigDecimal(new Double(item.getOvernight_fee())).setScale(4,BigDecimal.ROUND_HALF_UP).toString();
        String shouxufei = new BigDecimal(new Double(item.getFee())).setScale(4,BigDecimal.ROUND_HALF_UP).toString();
        String bzj = new BigDecimal(new Double(item.getConsume_num())).setScale(4,BigDecimal.ROUND_HALF_UP).toString();
        helper.setText(R.id.trust_guoyef, guoyef);
        helper.setText(R.id.trust_shouxufei, shouxufei);
        helper.setText(R.id.trust_zhiyingjia, String.valueOf(item.getHarvest_price()));
        helper.setText(R.id.trust_zhisunjia, String.valueOf(item.getLoss_price()));
        helper.setText(R.id.trust_baozhengjin, bzj);

        helper.getView(R.id.trust_btn).setTag(item.getId());
        helper.addOnClickListener(R.id.trust_btn);
        if (type == 0){
            helper.setText(R.id.trust_btn, "平仓");
        }else if (type == 1){
            helper.setText(R.id.trust_btn, "撤销");
        }else {
            helper.setVisible(R.id.trust_btn, false);
        }



    }
}
