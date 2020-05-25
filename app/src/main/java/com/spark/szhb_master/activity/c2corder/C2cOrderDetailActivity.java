package com.spark.szhb_master.activity.c2corder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.PagerAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.C2cOrderDetail;
import com.spark.szhb_master.utils.NetCodeUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

public class C2cOrderDetailActivity extends BaseActivity implements C2cOrderContract.DetailView {

    @BindView(R.id.rlTitle)
    RelativeLayout rlTitle;

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.tvOrderTitle)
    TextView tvOrderTitle;

    @BindView(R.id.tvOrderStatus)
    TextView tvOrderStatus;

    @BindView(R.id.tvOrderDetail)
    TextView tvOrderDetail;

    @BindView(R.id.tvOrderTotalPrice)
    TextView tvOrderTotalPrice;

    @BindView(R.id.tvOrderNum)
    TextView tvOrderNum;

    @BindView(R.id.tvOrderPrice)
    TextView tvOrderPrice;

    @BindView(R.id.tvOrderDate)
    TextView tvOrderDate;

    C2cOrderContract.DetailPresenter presenter;

    private int mId,mType;

    @Override
    protected void initView() {
        super.initView();
        setImmersionBar(rlTitle);
        mId = getIntent().getIntExtra("id",0);
        mType = getIntent().getIntExtra("type",0);
        new C2COrderDetailPresenter(Injection.provideTasksRepository( this),this);
    }


    @Override
    public void getOrderDetailSuccess(C2cOrderDetail c2cOrderDetail) {
        tvOrderTitle.setText(c2cOrderDetail.getType() == 0 ? "购买USDT" : "卖出USDT");
        tvOrderStatus.setText(mType == 0 ? "进行中" : mType == 1 ? "已完成"  : "已取消");

        switch (c2cOrderDetail.getStatus()){
            case 0:
                tvOrderDetail.setText("待我确认付款");
                break;
            case 1:
                tvOrderDetail.setText("待对方确认付款");
                break;
            case 2:
                tvOrderDetail.setText("待对方确认放币");
                break;
            case 3:
                tvOrderDetail.setText("待我确认放币");
                break;
            case 4:
                tvOrderDetail.setText("已放币");
                break;
            case 5:
                tvOrderDetail.setText("已撤销");
                break;
            case 6:
                tvOrderDetail.setText("问题单");
                break;
        }

        ;
        tvOrderTotalPrice.setText("￥ " + ((new BigDecimal(c2cOrderDetail.getNum()))
                .multiply( new BigDecimal(c2cOrderDetail.getPrice()) )).setScale(2, RoundingMode.UP).doubleValue());
        tvOrderNum.setText(c2cOrderDetail.getNum() + " USDT");
        tvOrderPrice.setText("￥ " + c2cOrderDetail.getPrice());
        tvOrderDate.setText(c2cOrderDetail.getCreated_at());
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this,code,toastMessage);
    }

    @Override
    protected void initData() {
        super.initData();
        HashMap hashMap = new HashMap();
        hashMap.put("id",mId);
        presenter.getOrderDetail(hashMap);
    }

    public void setPresenter(C2cOrderContract.DetailPresenter presenter) {
        this.presenter = presenter;
    }

    @OnClick({R.id.ivBack})
    @Override
    protected void setOnClickListener(View v) {
        switch (v.getId()){
            case R.id.ivBack:
                finish();
                break;
        }
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_c2cod;
    }

}
