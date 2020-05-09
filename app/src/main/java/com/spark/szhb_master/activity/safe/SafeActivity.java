package com.spark.szhb_master.activity.safe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.BottomSheetDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.account_pwd.ResetPwdActivity;
import com.spark.szhb_master.activity.edit_login_pwd.EditLoginPwdActivity;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.spark.szhb_master.widget.pwdview.PayPasswordView;

import java.util.HashMap;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import butterknife.OnClick;
import config.Injection;

public class SafeActivity extends BaseActivity implements SafeContract.View {


    private SafeContract.Presenter pwdPresenter;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SafeActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_safe;
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);

    }

    @Override
    protected void initData() {
        super.initData();

        setTitle(getString(R.string.my_setting));
        tvGoto.setVisibility(View.INVISIBLE);
        new SafePresenter(Injection.provideTasksRepository(getApplicationContext()), SafeActivity.this);
    }

    @Override
    protected void loadData() {
        super.loadData();
        //presenter.safeSetting();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @OnClick({R.id.lldlmm,R.id.llzjmm})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.lldlmm:
                showActivity(ResetPwdActivity.class, null);
                break;
            case R.id.llzjmm:
                openPayPasswordDialog();
                break;
        }
    }


    private void openPayPasswordDialog() {
        PayPasswordView payPasswordView = new PayPasswordView(this);
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        payPasswordView.setPasswordFullListener(new PayPasswordView.PasswordFullListener() {
            @Override
            public void passwordFull(String password) {
                bottomSheetDialog.dismiss();
                tvTitle.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showYZmPopWindow(password);
                    }
                },300);

            }

            @Override
            public void dismiss() {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.setContentView(payPasswordView);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.show();
    }

    private PopupWindow popWnd;

    private void showYZmPopWindow(final String pwd) {
        //配对PopWindow
        View contentView = LayoutInflater.from(SafeActivity.this).inflate(R.layout.yzm_dialog, null);
        popWnd = new PopupWindow(SafeActivity.this);
        popWnd.setContentView(contentView);
        popWnd.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popWnd.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popWnd.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        popWnd.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        popWnd.setTouchable(true);
        popWnd.setFocusable(true);
        popWnd.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        darkenBackground(0.4f);

        final TextView confirm = contentView.findViewById(R.id.tvGccConfirm);
        final TextView google_send = contentView.findViewById(R.id.google_send);
        TextView google_phone = contentView.findViewById(R.id.google_phone);
        final EditText google_code = contentView.findViewById(R.id.google_code);
//        final EditText google_yzm = contentView.findViewById(R.id.google_yzm);
        ImageView img_close = contentView.findViewById(R.id.img_close);

//        String maskNumber = mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
        google_phone.setText("短信验证码");

        google_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pwdPresenter.sendCode();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneCode = google_code.getText().toString();//手机验证码
                HashMap map = new HashMap<>();
                map.put("pwd", Integer.parseInt(pwd));
                map.put("code", "111111");//Integer.parseInt(phoneCode)
                pwdPresenter.updateZjmmPwd(map);//
            }
        });
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popWnd.dismiss();
            }
        });


        View rootview = LayoutInflater.from(SafeActivity.this).inflate(R.layout.activity_safe, null);
        popWnd.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
        popWnd.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                darkenBackground(1f);
            }
        });

    }


    /**
     * 改变背景颜色
     */
    private void darkenBackground(Float bgcolor) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgcolor;

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }


    @Override
    public void updateZjmmPwdSuccess(String obj) {
        ToastUtils.showToast(obj);
        popWnd.dismiss();
    }

    @Override
    public void sendCodeSuccess(String obj) {
        ToastUtils.showToast(obj);
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode((BaseActivity) activity, code, toastMessage);
    }


    @Override
    public void setPresenter(SafeContract.Presenter presenter) {
        pwdPresenter = presenter;
    }
}
