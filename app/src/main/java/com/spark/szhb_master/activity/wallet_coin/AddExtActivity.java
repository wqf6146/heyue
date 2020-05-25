package com.spark.szhb_master.activity.wallet_coin;

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
import com.spark.szhb_master.entity.ExtAddressEntity;
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
public class AddExtActivity extends BaseActivity implements CoinContract.extractListView {

    @BindView(R.id.ar_iv_close)
    ImageView ivClose;

    @BindView(R.id.rlhead)
    RelativeLayout rlhead;

    @BindView(R.id.rlChain)
    RelativeLayout rlChain;

    @BindView(R.id.tvConfirm)
    TextView tvConfirm;

    @BindView(R.id.etExtAddress)
    EditText etExtAddress;

    @BindView(R.id.etRemark)
    EditText etRemark;

    @BindView(R.id.tvChainName)
    TextView tvChainName;

    private CoinContract.extractListPresenter presenter;

    private int chainType = 0; // 0为Erc20,1为omni
    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_addextaddress;
    }

    @Override
    protected void initView() {
        setImmersionBar(rlhead);
    }

    @Override
    protected void initData() {
        super.initData();
        new ExtractListPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
    }

    @OnClick({ R.id.ar_iv_close,R.id.rlChain,R.id.tvConfirm})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.ar_iv_close:
                finish();
                break;
            case R.id.tvConfirm: // 提币
                String extAddress = etExtAddress.getText().toString();
                String remark = etRemark.getText().toString();

                if (StringUtils.isEmpty(extAddress,remark)) {
                    ToastUtils.showToast(R.string.incomplete_information);
                    return;
                }

                HashMap hashMap = new HashMap();
                hashMap.put("name",remark);
                hashMap.put("address",extAddress);
                hashMap.put("type",chainType);
                presenter.addExtAddress(hashMap);

                break;
            case R.id.rlChain: // 全部
                actionSheetDialogNoTitle();
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
    public void setPresenter(CoinContract.extractListPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }


    @Override
    public void getExtListSuccess(ExtAddressEntity extAddressEntity) {

    }

    @Override
    public void addExtAddressSuccess(String obj) {
        ToastUtils.showToast(obj);
        finish();
    }

    @Override
    public void removeExtAddressSuccess(String obj) {

    }
}
