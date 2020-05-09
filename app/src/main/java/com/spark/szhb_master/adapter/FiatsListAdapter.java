package com.spark.szhb_master.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.szhb_master.R;
import com.spark.szhb_master.dialog.ImgDialog;
import com.spark.szhb_master.entity.C2C;
import com.spark.szhb_master.entity.Fiats;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.MathUtils;
import com.spark.szhb_master.utils.ToastUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * c2c列表
 * Created by Administrator on 2018/2/28.
 */

public class FiatsListAdapter extends BaseQuickAdapter<Fiats.FiatsBean, BaseViewHolder> {
    private Context context;
    private ImgDialog imgDialog;
    private int type;
    private CallBackEvent callBackEvent;

    public void setCallBackEvent(CallBackEvent callBackEvent) {
        this.callBackEvent = callBackEvent;
    }

    public FiatsListAdapter(Context context, int layoutResId, int type, @Nullable List<Fiats.FiatsBean> data) {
        super(layoutResId, data);
        this.context = context;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, final Fiats.FiatsBean item) {

        BigDecimal num = new BigDecimal(item.getNum());
        BigDecimal price = new BigDecimal(item.getPrice());
        double totalprice = num.multiply(price).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();

        helper.setText(R.id.tvOrdernum, "订单号 " + item.getId())
                .setText(R.id.tvTotalPrice, "￥" + totalprice)
                .setText(R.id.tvPrice, String.valueOf(item.getPrice()))
                .setText(R.id.tvNum, String.valueOf(item.getNum()))
                .setText(R.id.tvLimit, item.getMin_num() + " - " + item.getMax_num())
                .setText(R.id.tvDoneNum, String.valueOf(item.getVolume()));

        helper.getView(R.id.ivCopy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("ordernum", String.valueOf(item.getId()));
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                ToastUtils.showToast("复制成功");
            }
        });

        helper.getView(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBackEvent!=null)
                    callBackEvent.onClickCallback(item);
            }
        });

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

    }

    public interface CallBackEvent {
        void onClickCallback(Fiats.FiatsBean item);
    }


}
