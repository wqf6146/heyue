package com.spark.szhb_master.activity.signup;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geetest.sdk.GT3ConfigBean;
import com.geetest.sdk.GT3ErrorBean;
import com.geetest.sdk.GT3GeetestUtils;
import com.geetest.sdk.GT3Listener;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.login.LoginStepOneActivity;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.spark.szhb_master.widget.TimeCount;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

public class RegisterStepTwoActivity extends BaseActivity implements SignUpContract.CodeView{

    @BindView(R.id.ar_iv_close)
    ImageView ivClose;

    @BindView(R.id.ar_ed_input)
    EditText edInput;

    @BindView(R.id.ar_rl_nextstep)
    RelativeLayout rlNextStep;

    @BindView(R.id.ar_tv_gologin)
    TextView tvGoLogin;

    @BindView(R.id.etCode)
    EditText etCode;

    @BindView(R.id.ar_tv_sendcode)
    TextView tvSendCode;

    private String mAccount;
    private SignUpContract.SignCodePresenter presenter;
    private int mCode;
    private TimeCount timeCount;
//    private GT3GeetestUtilsBind gt3GeetestUtils;

    private String TAG = RegisterStepTwoActivity.class.toString();
    private GT3GeetestUtils gt3GeetestUtils;
    private GT3ConfigBean gt3ConfigBean;
    @Override
    public void setPresenter(SignUpContract.SignCodePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_register_steptwo;
    }

    @Override
    protected void initData() {
        super.initData();
        new SignCodePresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        mAccount = getIntent().getStringExtra("account");
        gt3GeetestUtils = new GT3GeetestUtils(this);
        timeCount = new TimeCount(90000, 1000, tvSendCode);
//        getCode();
    }

    @OnClick({R.id.ar_iv_close, R.id.ar_rl_nextstep, R.id.ar_tv_gologin,R.id.ar_tv_sendcode})
    @Override
    protected void setOnClickListener(View v) {
        switch (v.getId()){
            case R.id.ar_tv_sendcode:
//                getCode();
//                presenter.captch();
                customVerity();
                break;
            case R.id.ar_iv_close:
                finish();
                break;
            case R.id.ar_rl_nextstep:
                String pwd = edInput.getText().toString().trim();
                if (StringUtils.isNotEmpty(mAccount) && StringUtils.isNotEmpty(pwd) &&
                        StringUtils.isNotEmpty(etCode.getText().toString().trim())){
                    if (pwd.length()<8){
                        ToastUtils.showToast("密码不能少于8位");
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("account",mAccount);
                    bundle.putInt("code",Integer.parseInt(etCode.getText().toString().trim()));
                    bundle.putString("pwd",pwd);
                    showActivity(RegisterStepThreeActivity.class, bundle);
                }else{
                    ToastUtils.showToast(getString(R.string.incomplete_information));
                }
                break;
            case R.id.ar_tv_gologin:
                showActivity(LoginStepOneActivity.class,null);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gt3GeetestUtils.destory();
    }

    private void getCode() {
        HashMap map = new HashMap<>();
        map.put("account", mAccount);
        map.put("type",3);
        presenter.getCode(map);
    }

    private void customVerity() {
        // 配置bean文件，也可在oncreate初始化
        gt3ConfigBean = new GT3ConfigBean();
        // 设置验证模式，1：bind，2：unbind
        gt3ConfigBean.setPattern(1);
        // 设置点击灰色区域是否消失，默认不消失
        gt3ConfigBean.setCanceledOnTouchOutside(false);
        // 设置语言，如果为null则使用系统默认语言
        gt3ConfigBean.setLang(null);
        // 设置加载webview超时时间，单位毫秒，默认10000，仅且webview加载静态文件超时，不包括之前的http请求
        gt3ConfigBean.setTimeout(10000);
        // 设置webview请求超时(用户点选或滑动完成，前端请求后端接口)，单位毫秒，默认10000
        gt3ConfigBean.setWebviewTimeout(10000);
        // 设置自定义view
//        gt3ConfigBean.setLoadImageView(new TestLoadingView(this));
        // 设置回调监听
        gt3ConfigBean.setListener(new GT3Listener() {

            /**
             * 验证码加载完成
             * @param duration 加载时间和版本等信息，为json格式
             */
            @Override
            public void onDialogReady(String duration) {
                Log.e(TAG, "GT3BaseListener-->onDialogReady-->" + duration);
            }

            /**
             * 验证结果
             * @param result
             */
            @Override
            public void onDialogResult(String result) {
                Log.e(TAG, "GT3BaseListener-->onDialogResult-->" + result);
                // 开启api2逻辑
//                new RequestAPI2().execute(result);
                gt3GeetestUtils.showSuccessDialog();
            }

            /**
             * 统计信息，参考接入文档
             * @param result
             */
            @Override
            public void onStatistics(String result) {
                Log.e(TAG, "GT3BaseListener-->onStatistics-->" + result);
            }

            /**
             * 验证码被关闭
             * @param num 1 点击验证码的关闭按钮来关闭验证码, 2 点击屏幕关闭验证码, 3 点击返回键关闭验证码
             */
            @Override
            public void onClosed(int num) {
                Log.e(TAG, "GT3BaseListener-->onClosed-->" + num);
            }

            /**
             * 验证成功回调
             * @param result
             */
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "GT3BaseListener-->onSuccess-->" + result);
                getCode();
            }

            /**
             * 验证失败回调
             * @param errorBean 版本号，错误码，错误描述等信息
             */
            @Override
            public void onFailed(GT3ErrorBean errorBean) {
                Log.e(TAG, "GT3BaseListener-->onFailed-->" + errorBean.toString());
            }

            /**
             * api1回调
             */
            @Override
            public void onButtonClick() {
//                new RequestAPI1().execute();
                presenter.captch();
            }
        });
        gt3GeetestUtils.init(gt3ConfigBean);
        // 开启验证
        gt3GeetestUtils.startCustomFlow();
    }

    @Override
    public void codeFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this,code,toastMessage);
    }

    @Override
    public void codeSuccess(String obj) {
        try {
            timeCount.start();
            tvSendCode.setClickable(false);
            ToastUtils.showToast("发送成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void captchFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this,code,toastMessage);
    }

    @Override
    public void captchSuccess(JSONObject obj) {
        gt3ConfigBean.setApi1Json(obj);
        // 继续api验证
        gt3GeetestUtils.getGeetest();
    }
}
