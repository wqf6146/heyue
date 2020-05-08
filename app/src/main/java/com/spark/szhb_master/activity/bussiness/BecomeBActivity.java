package com.spark.szhb_master.activity.bussiness;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.account_pwd.AccountPwdContract;
import com.spark.szhb_master.activity.account_pwd.ResetAccountPwdPresenter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.spark.szhb_master.widget.TimeCount;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

public class BecomeBActivity extends BaseActivity implements BecomeBContract.View{

    @BindView(R.id.ar_iv_close)
    ImageView ivClose;

    @BindView(R.id.edPhone)
    EditText edPhone;

    @BindView(R.id.edEmail)
    EditText edEmail;

    @BindView(R.id.rlDone)
    RelativeLayout rlDone;


    @BindView(R.id.rlhead)
    RelativeLayout rlhead;


    private BecomeBContract.Presenter presenter;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_becomeb;
    }

    @Override
    protected void initView() {
        super.initView();
        setImmersionBar(rlhead);
    }

    @Override
    protected void initData() {
        super.initData();
        new BecomeBPresenter(Injection.provideTasksRepository(activity), this);
    }

    @OnClick({R.id.ar_iv_close,R.id.rlDone})
    @Override
    protected void setOnClickListener(View v) {
        switch (v.getId()){
            case R.id.ar_iv_close:
                finish();
                break;

            case R.id.rlDone:
                String phone = edPhone.getText().toString().trim();
                String email = edEmail.getText().toString().trim();
                if (StringUtils.isNotEmpty(phone) || StringUtils.isNotEmpty(email)){
                    HashMap hashMap = new HashMap<>();
                    hashMap.put("phone",phone);
                    hashMap.put("email",email);
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
        finish();
    }

    @Override
    public void setPresenter(BecomeBContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode((BaseActivity) activity, code, toastMessage);
    }

}
