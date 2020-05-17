package com.spark.szhb_master.activity.wallet_coin;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.spark.szhb_master.R;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

/**
 * 提币
 */
public class ExtractActivity extends BaseActivity implements CoinContract.extractView {

    @BindView(R.id.ar_iv_close)
    ImageView ivClose;

    @BindView(R.id.rlhead)
    RelativeLayout rlhead;

    @BindView(R.id.tvRecord)
    TextView tvRecord;

    @BindView(R.id.rlChain)
    RelativeLayout rlChain;

    @BindView(R.id.etAddress)
    EditText etAddress;

    @BindView(R.id.etNum)
    EditText etNum;

    @BindView(R.id.llSubmit)
    LinearLayout llSubmit;

    @BindView(R.id.tvChainName)
    TextView tvChainName;

    private int chainType = 0; // 0为Erc20,1为omni

    private CoinContract.extractPresenter presenter;


    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_extract;
    }

    @Override
    protected void initView() {
        setImmersionBar(rlhead);
    }

    @Override
    protected void initData() {
        super.initData();
        new ExtractPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
    }

    @OnClick({ R.id.ar_iv_close,R.id.rlChain,R.id.llSubmit,R.id.tvRecord})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.ar_iv_close:
                finish();
                break;
            case R.id.llSubmit: // 提币
                String address = etAddress.getText().toString();
                String num = etNum.getText().toString();

                if (StringUtils.isEmpty(address,num)) {
                    ToastUtils.showToast(R.string.incomplete_information);
                    return;
                }
                if (Double.parseDouble(num) <= 0){
                    ToastUtils.showToast("请输入正确的提币数量");
                }
                HashMap hashMap = new HashMap();
                hashMap.put("address",address);
                hashMap.put("num",Integer.parseInt(num));
                hashMap.put("type",chainType);
                presenter.extract(hashMap);

                break;
            case R.id.rlChain: // 全部
                actionSheetDialogNoTitle();
                break;
            case R.id.tvRecord:
                Bundle bundle = new Bundle();
                bundle.putInt("type",1);
                showActivity(DetailListActivity.class,bundle);
                break;
        }
    }

    /**
     */
    private void actionSheetDialogNoTitle() {
        final String[] stringItems = {"USDT-ERC20","USDT-Omni"};
        final ActionSheetDialog dialog = new ActionSheetDialog(activity, stringItems, null);
        dialog.isTitleShow(false).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        chainType = 0;
                        tvChainName.setText("USDT-ERC20");
                        break;
                    case 1:
                        chainType = 1;
                        tvChainName.setText("USDT-Omni");
                        break;
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    public void setPresenter(CoinContract.extractPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }


    @Override
    public void extractSuccess(String obj) {
        ToastUtils.showToast(obj);
        finish();
    }
}
