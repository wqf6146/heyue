package com.spark.szhb_master.activity.Bind_bank;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spark.szhb_master.R;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

public class BindBankActivity extends BaseActivity implements BindBankContract.View{

    @BindView(R.id.ar_iv_close)
    ImageView ivClose;

    @BindView(R.id.edCode)
    EditText edCode;

    @BindView(R.id.tvConfirm)
    TextView tvConfirm;

    @BindView(R.id.rlhead)
    RelativeLayout rlhead;

    @BindView(R.id.etName)
    EditText etName;

    @BindView(R.id.etAccount)
    EditText etAccount;

    @BindView(R.id.etConfirmAccount)
    EditText etConfirmAccount;

    @BindView(R.id.etBankHang)
    EditText etBankHang;

    @BindView(R.id.etBranch)
    EditText etBranch;

    @BindView(R.id.tvSendcode)
    TextView tvSendcode;

    private CountDownTimer timer;
    private BindBankContract.Presenter presenter;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_bind_bank;
    }

    @Override
    protected void initView() {
        super.initView();
        setImmersionBar(rlhead);
    }

    @Override
    protected void initData() {
        super.initData();
        new BindBankPresenter(Injection.provideTasksRepository(activity), this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    private void fillCodeView(long time) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvSendcode.setText(getResources().getString(R.string.re_send) + "（" + millisUntilFinished / 1000 + "）");
            }

            @Override
            public void onFinish() {
                tvSendcode.setText(R.string.send_code);
                tvSendcode.setEnabled(true);
                timer.cancel();
                timer = null;
            }
        };
        timer.start();
    }

    @Override
    public void sendCodeSuccess(String obj) {
        ToastUtils.showToast(obj);
        fillCodeView(90 * 1000);
    }

    @OnClick({R.id.ar_iv_close,R.id.tvSendcode,R.id.tvConfirm})
    @Override
    protected void setOnClickListener(View v) {
        switch (v.getId()){
            case R.id.ar_iv_close:
                finish();
                break;

            case R.id.tvSendcode:
                presenter.sendCode();
                break;

            case R.id.tvConfirm:
                String name = etName.getText().toString().trim();
                String account = etAccount.getText().toString().trim();
                String confirmAccount = etConfirmAccount.getText().toString().trim();
                String bankHang = etBankHang.getText().toString().trim();
                String branch = etBranch.getText().toString().trim();
                String code = edCode.getText().toString().trim();

                if (StringUtils.isNotEmpty(name,account,confirmAccount,bankHang,branch,code)){
                    if (!confirmAccount.equals(account)){
                        ToastUtils.showToast("两次卡号输入不一致");
                        return;
                    }
                    HashMap hashMap = new HashMap<>();
                    hashMap.put("type",0);
                    hashMap.put("name",name);

                    hashMap.put("name",name);
                    hashMap.put("card",account);
                    hashMap.put("bank",bankHang);
                    hashMap.put("subbranch",branch);
                    hashMap.put("code",Integer.parseInt(code));

                    presenter.submit(hashMap);
                }else{
                    ToastUtils.showToast(getString(R.string.incomplete_information));
                }
                break;
        }
    }

    @Override
    public void submitSuccess(String obj) {
        setResult(RESULT_OK);
        ToastUtils.showToast("添加成功");
        finish();
    }

    @Override
    public void setPresenter(BindBankContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode((BaseActivity) activity, code, toastMessage);
    }

}

