package com.spark.szhb_master.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.szhb_master.R;
import com.spark.szhb_master.entity.NewEntrust;
import com.spark.szhb_master.utils.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * 委托
 * author: wuzongjie
 * time  : 2018/4/17 0017 10:18
 * desc  : 当前委托的适配器
 */

public class TrustAdapter extends BaseQuickAdapter<NewEntrust.ListBean, BaseViewHolder> {
    private int type = 0; //0-当前持仓 1-当前委托
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
//        if (GlobalConstant.LIMIT_PRICE.equals(item.getType())) { // 限价
//            helper.setText(R.id.trust_price, MathUtils.getRundNumber(item.getPrice(), 2, null));
//            // 数量
//            helper.setText(R.id.trust_num, String.valueOf(BigDecimal.valueOf(item.getAmount())));
//        } else { // 市价
//            helper.setText(R.id.trust_price, String.valueOf(MyApplication.getApp().getString(R.string.text_best_prices)));
//            // 数量 如果是市价并买入情况就是--
//            if (GlobalConstant.BUY.equals(item.getDirection())) {
//                helper.setText(R.id.trust_num, String.valueOf("--"));
//            } else {
//                helper.setText(R.id.trust_num, MathUtils.getRundNumber(Double.valueOf(String.valueOf(BigDecimal.valueOf(item.getAmount()))), 2, null));
//            }
//        }
//        String symbol = item.getSymbol();
//        int i = symbol.indexOf("/");
        helper.setText(R.id.trust_num, String.valueOf(item.getNum()));
        helper.setText(R.id.trust_price, String.valueOf(item.getPrice()));
        helper.setText(R.id.trust_newprice, String.valueOf(item.getNew_price()));

        if (type == 0){
            helper.setText(R.id.trust_btn, "平仓");
        }else{
            helper.setText(R.id.trust_btn, "撤销");
        }
        helper.getView(R.id.trust_btn).setTag(item.getId());

        helper.addOnClickListener(R.id.trust_btn);
    }
}
