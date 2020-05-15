package com.spark.szhb_master.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.entity.NewCurrency;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 行情adapter
 * author: wuzongjie
 * time  : 2018/4/16 0016 18:18
 */

public class HeyueAdapter extends BaseQuickAdapter<NewCurrency, BaseViewHolder> {
    private int type;
    private boolean isLoad;

    public boolean isLoad() {
        return isLoad;
    }

    public void setLoad(boolean load) {
        isLoad = load;
    }

    public HeyueAdapter(@Nullable List<NewCurrency> data, int type) {
        super(R.layout.item_market, data);
        this.type = type;
    }


    @Override
    protected void convert(BaseViewHolder helper, NewCurrency item) {

        boolean tol = Float.parseFloat(item.getScale()) > 0 ? true : false;

        helper.setText(R.id.tvBuySymbol, item.getSymbol());
        if (item.getSymbol().equals("BTC")){
            helper.setText(R.id.tvSecSymbol, "/" + item.getType()).setVisible(R.id.tvSecSymbol,true);
        }else{
            helper.setVisible(R.id.tvSecSymbol,false);
        }

        helper.setText(R.id.tvPrice, new BigDecimal(Double.parseDouble(item.getClose()))
                .setScale(MyApplication.getApp().getSymbolSize(item.getSymbol()), RoundingMode.UP).toString());

        if (tol){
            helper.setText(R.id.item_home_chg, "+"+ item.getScale() + "%");
            helper.getView(R.id.item_home_chg).setEnabled(true);
        }else{
            helper.setText(R.id.item_home_chg, item.getScale() + "%");
            helper.getView(R.id.item_home_chg).setEnabled(false);
        }
    }
}