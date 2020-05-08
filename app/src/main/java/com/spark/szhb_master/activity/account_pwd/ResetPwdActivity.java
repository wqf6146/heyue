package com.spark.szhb_master.activity.account_pwd;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.forgot_pwd.ForgotPwdContract;
import com.spark.szhb_master.activity.forgot_pwd.ForgotPwdStepThreeActivity;
import com.spark.szhb_master.activity.forgot_pwd.PhoneForgotPresenter;
import com.spark.szhb_master.activity.login.LoginStepOneActivity;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.factory.UrlFactory;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.spark.szhb_master.widget.TimeCount;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

public class ResetPwdActivity extends BaseActivity implements AccountPwdContract.ResetView{

    @BindView(R.id.ar_iv_close)
    ImageView ivClose;

    @BindView(R.id.edCode)
    EditText edCode;

    @BindView(R.id.edPassword)
    EditText edPassword;

    @BindView(R.id.tvSendcode)
    TextView tvSendcode;

    @BindView(R.id.rlDone)
    RelativeLayout rlDone;

    @BindView(R.id.tvPhone)
    TextView tvPhone;

    @BindView(R.id.rlhead)
    RelativeLayout rlhead;


    private TimeCount timeCount;
    private String mAccount;
    private boolean isEmail = false;

    private AccountPwdContract.ResetPresenter presenter;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_resetpwd;
    }

    @Override
    protected void initView() {
        super.initView();
        setImmersionBar(rlhead);
    }

    @Override
    protected void initData() {
        super.initData();
        new ResetAccountPwdPresenter(Injection.provideTasksRepository(activity), this);
        timeCount = new TimeCount(90000, 1000, tvSendcode);
        mAccount = MyApplication.getApp().getCurrentUser().getPhone();
        tvPhone.setText(String.format("验证码已发送至%s的手机",mAccount));
        getCode();
    }

    @OnClick({R.id.ar_iv_close, R.id.tvSendcode,R.id.rlDone})
    @Override
    protected void setOnClickListener(View v) {
        switch (v.getId()){
            case R.id.ar_iv_close:
                finish();
                break;
            case R.id.tvSendcode:
                getCode();
                break;
            case R.id.rlDone:
                String code = edCode.getText().toString().trim();
                String pwd = edPassword.getText().toString().trim();
                if (StringUtils.isNotEmpty(pwd) || StringUtils.isNotEmpty(code)){
                    HashMap hashMap = new HashMap<>();
                    hashMap.put("new_password",pwd);
                    hashMap.put("code",Integer.parseInt(code));
                    presenter.resetAccountPwd(hashMap);
                }else{
                    ToastUtils.showToast(getString(R.string.incomplete_information));
                }
                break;
        }
    }

    @Override
    public void getCodeSuccess(String obj) {
        try {
            timeCount.start();
            tvSendcode.setEnabled(false);
            ToastUtils.showToast("发送成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPresenter(AccountPwdContract.ResetPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void resetAccountPwdCodeSuccess(String obj) {
        ToastUtils.showToast(obj);
    }

    @Override
    public void resetAccountPwdSuccess(String obj) {
        ToastUtils.showToast(obj);
        finish();
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode((BaseActivity) activity, code, toastMessage);
    }

    private void getCode() {
        if (isEmail) {
//            String email = edInput.getText().toString().trim();
//            if (StringUtils.isEmpty(email) || !StringUtils.isEmail(email)) {
//                ToastUtils.showToast(R.string.email_diff);
//            } else {
//                presenter.captch();
//            }
        } else {
            presenter.getCode();

        }
    }

}
