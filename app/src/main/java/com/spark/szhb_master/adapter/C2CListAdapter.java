package com.spark.szhb_master.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.view.WindowManager;

import com.amulyakhare.textdrawable.TextDrawable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.szhb_master.R;
import com.spark.szhb_master.dialog.ImgDialog;
import com.spark.szhb_master.entity.C2C;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.MathUtils;
import com.spark.szhb_master.utils.VersionCompareUtil;
import com.spark.szhb_master.widget.pwdview.BuyOrSellView;

import java.util.Arrays;
import java.util.List;

/**
 * c2c列表
 * Created by Administrator on 2018/2/28.
 */

public class C2CListAdapter extends BaseQuickAdapter<C2C.C2CBean, BaseViewHolder> {
    private Context context;
    private ImgDialog imgDialog;
    private int type;

    public C2CListAdapter(Context context, int layoutResId, int type, @Nullable List<C2C.C2CBean> data) {
        super(layoutResId, data);
        this.context = context;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, final C2C.C2CBean item) {
        if (type == 2){

        }else{
            helper.setText(R.id.tvName, item.getName()).setText(R.id.tvLimit, String.valueOf(item.getTrade_num()))
                    .setText(R.id.tvPrice, MathUtils.getRundNumber(item.getPrice(), 2, null) +" "+ GlobalConstant.CNY)
                    .setText(R.id.tvTotalNum, "总数量 " + item.getNum())
                    .setText(R.id.tvLimit,"交易限额 " + item.getMin_num() + " - " + item.getMax_num())
                    .setText(R.id.tvSubmit, (type == 1 ?
                            context.getString(R.string.text_sale_out) : context.getString(R.string.text_buy_in)));

            helper.setBackgroundRes(R.id.tvSubmit,type == 1 ?
                    R.drawable.shape_bg_red_hover : R.drawable.shape_bg_green_hover);

//        if (StringUtils.isNotEmpty(item.getAvatar()))
//            Glide.with(context).load(item.getAvatar()).into((ImageView) helper.getView(R.id.ivHeader));
//        else
//            Glide.with(context).load(R.mipmap.icon_default_header_grey).into((ImageView) helper.getView(R.id.ivHeader));

            helper.setGone(R.id.ivZhifubao, false);
            helper.setGone(R.id.ivWeChat, false);

//        List<String> pays = Arrays.asList(item.getPayMode().split(","));
//        if (pays.contains("支付宝")) helper.setVisible(R.id.ivZhifubao, true);
//        else helper.setGone(R.id.ivZhifubao, false);
//        if (pays.contains("微信")) helper.setVisible(R.id.ivWeChat, true);
//        else helper.setGone(R.id.ivWeChat, false);
//        if (pays.contains("银联") || pays.contains("银行卡")) helper.setVisible(R.id.ivUnionPay, true);
//        else helper.setGone(R.id.ivUnionPay, false);

//        imgDialog = new ImgDialog(context);
//        helper.setOnClickListener(R.id.ivHeader, new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (imgDialog != null) {
////                    imgDialog.setEntrust(item.getMemberName(),item.getTransactions()+"",item.getAdvertiseId());
//                    imgDialog.setEntrust(item.getMemberName(), item.getTransactions() + "", item.getAdvertiseId(), item.getPhoneVerified(), item.getEmailVerified());
//                    imgDialog.show();
//                }
//            }
//        });
//
//        helper.setOnClickListener(R.id.tvName, new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (imgDialog != null) {
////                    imgDialog.setEntrust(item.getMemberName(),item.getTransactions()+"",item.getAdvertiseId());
//                    imgDialog.setEntrust(item.getMemberName(), item.getTransactions() + "", item.getAdvertiseId(), item.getPhoneVerified(), item.getEmailVerified());
//                    imgDialog.show();
//                }
//            }
//        });
        }

    }


}
