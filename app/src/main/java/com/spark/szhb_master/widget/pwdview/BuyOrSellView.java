package com.spark.szhb_master.widget.pwdview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spark.szhb_master.R;
import com.spark.szhb_master.entity.C2C;
import com.spark.szhb_master.utils.ToastUtils;


/**
 * Description:
 * Data：7/10/2018-10:18 AM
 *
 * @author yanzhiwen
 */

public class BuyOrSellView extends LinearLayout {

    private CallbackListener callbackListener;

    private EditText edPrice,edNum;
    private TextView tvCancel,tvSubmit,tvText,tvPrice;

    private boolean bBuyOrSell = true;

    private C2C.C2CBean c2CBean;

    public BuyOrSellView(Context context, boolean bBuyOrSell, C2C.C2CBean cBean) {
        this(context, null,bBuyOrSell,cBean);
    }

    public BuyOrSellView(Context context, @Nullable AttributeSet attrs,boolean bBuyOrSell,C2C.C2CBean cBean) {
        this(context, attrs, 0,bBuyOrSell,cBean);
    }

    public BuyOrSellView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,boolean bBuyOrSell,C2C.C2CBean cBean) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.view_buyorsell, this);
        tvCancel = findViewById(R.id.tvCancel);
        tvSubmit = findViewById(R.id.tvSubmit);

//        edPrice = findViewById(R.id.edPrice);
        edNum = findViewById(R.id.edNum);

        tvText = findViewById(R.id.tvText);
        tvPrice = findViewById(R.id.tvPrice);

        tvSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackListener.submit(cBean, edNum.getText().toString());
            }
        });
        tvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackListener.dismiss();
            }
        });

        c2CBean = cBean;
        tvPrice.setText("价格" + cBean.getPrice());

        if (bBuyOrSell){
            tvText.setText("购买USDT");
            tvSubmit.setText("购买");
            tvSubmit.setBackground(getResources().getDrawable(R.drawable.bg_green_btn_shape));
        }else{
            tvText.setText("出售USDT");
            tvSubmit.setText("出售");
            tvSubmit.setBackground(getResources().getDrawable(R.drawable.bg_red_btn_shape));
        }

    }


    public void setCallbackListener(CallbackListener callbackListener) {
        this.callbackListener = callbackListener;
    }


    public interface CallbackListener {
        void submit(C2C.C2CBean item,  String num);
        void dismiss();
    }
}
