package com.spark.szhb_master.activity.wallet_coin;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spark.szhb_master.R;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.AssetsInfo;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

/**
 * 充币
 */
public class FundtransferActivity extends BaseActivity implements CoinContract.fundtransferView {

    @BindView(R.id.ar_iv_close)
    ImageView ivClose;

    @BindView(R.id.rlhead)
    RelativeLayout rlhead;

    @BindView(R.id.ivChange)
    ImageView ivChange;


    @BindView(R.id.tvTop)
    TextView tvTop;

    @BindView(R.id.etNum)
    EditText etNum;

    @BindView(R.id.tvUserableNum)
    TextView tvUserableNum;

    @BindView(R.id.tvAllChange)
    TextView tvAllChange;

    @BindView(R.id.llSubmit)
    LinearLayout llSubmit;

    @BindView(R.id.tvBottom)
    TextView tvBottom;

    private CoinContract.fundtransferPresenter fundtransferPresenter;

    @Override
    public void setPresenter(CoinContract.fundtransferPresenter presenter) {
        fundtransferPresenter = presenter;
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_huaz;
    }

    @Override
    protected void initView() {
        setImmersionBar(rlhead);
    }

    private Bitmap saveBitmap;

    @Override
    protected void initData() {
        super.initData();
        new FundtransferPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
    }

    private int type = 1; //类型0为合约转至资金，1为资金转至合约

    @OnClick({R.id.ar_iv_close,R.id.ivChange, R.id.tvAllChange,R.id.llSubmit})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.ar_iv_close:
                finish();
                break;
            case R.id.ivChange:
                if (type == 1){
                    type = 0;
                    tvTop.setText("合约全仓账户");
                    tvBottom.setText("资金账户");
                    if(mAssetsInfo!=null)
                        setPrice();
                }else{
                    type = 1;
                    tvTop.setText("资金账户");
                    tvBottom.setText("合约全仓账户");
                    if(mAssetsInfo!=null)
                        setPrice();
                }
                break;
            case R.id.tvAllChange:
                etNum.setText(tvUserableNum.getText());
                break;
            case R.id.llSubmit:

                if (StringUtils.isEmpty(etNum.getText().toString())){
                    ToastUtils.showToast("请输入数量");
                    return;
                }
                Double num = Double.parseDouble(etNum.getText().toString());
                HashMap hashMap = new HashMap();
                hashMap.put("type",type);
                hashMap.put("num",num);
                fundtransferPresenter.fundtransfer(hashMap);
                break;
        }
    }

    private AssetsInfo mAssetsInfo;
    @Override
    public void getWalletSuccess(AssetsInfo assetsInfo) {
        mAssetsInfo = assetsInfo;
        setPrice();
    }

    @Override
    public void fundtransferFaild(Integer code, String obj) {
        NetCodeUtils.checkedErrorCode(this,code,obj);
        fundtransferPresenter.getWallet();
    }

    @Override
    public void fundtransferSuccess(String obj) {
        ToastUtils.showToast(obj);
        finish();
    }

    private void setPrice(){
        if (type == 0){
            tvUserableNum.setText(String.format("%f",mAssetsInfo.getUsable()));
        }else{
            tvUserableNum.setText(String.format("%f",mAssetsInfo.getFrost()));
        }
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this,code,toastMessage);
    }

    @Override
    protected void loadData() {
        fundtransferPresenter.getWallet();
    }
}
