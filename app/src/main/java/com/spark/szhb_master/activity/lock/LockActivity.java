package com.spark.szhb_master.activity.lock;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.login.LoginActivity;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.activity.login.LoginStepOneActivity;
import com.spark.szhb_master.base.ActivityManage;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.User;
import com.spark.szhb_master.utils.SharedPreferenceInstance;
import com.spark.szhb_master.ui.CircleImageView;
import com.spark.szhb_master.ui.lock.LockPatternView;
import com.spark.szhb_master.utils.EncryUtils;
import com.spark.szhb_master.utils.DialogUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class LockActivity extends BaseActivity {
    public static final int RETURN_LOCK = 1;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.ivHeader)
    CircleImageView ivHeader;
    @BindView(R.id.tvNickName)
    TextView tvNickName;
    @BindView(R.id.tvMessage)
    TextView tvMessage;
    @BindView(R.id.gesture_tip_layout)
    LinearLayout gestureTipLayout;
    @BindView(R.id.lockView)
    LockPatternView lockView;
    @BindView(R.id.tvForgot)
    TextView tvForgot;
    private List<LockPatternView.Cell> cells;

    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, LockActivity.class);
        activity.startActivityForResult(intent, RETURN_LOCK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LoginStepOneActivity.RETURN_LOGIN) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_lock;
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
        isNeedChecke = false;
        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.showDefaultDialog(LockActivity.this, "提示", "忘记手势密码，需要您重新登录！", "取消", "去登录", null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferenceInstance.getInstance().saveIsNeedShowLock(false);
                        startActivityForResult(new Intent(LockActivity.this, LoginStepOneActivity.class), LoginStepOneActivity.RETURN_LOGIN);
                    }
                });
            }
        });
        lockView.setOnPatternListener(new LockPatternView.OnPatternListener() {
            @Override
            public void onPatternStart() {
                lockView.removePostClearPatternRunnable();
                lockView.setPattern(LockPatternView.DisplayMode.DEFAULT);
            }

            @Override
            public void onPatternComplete(List<LockPatternView.Cell> cells) {
                patternComplete(cells);
            }
        });
    }

    private void patternComplete(List<LockPatternView.Cell> cells) {
        if (cells.size() < 4) {
            lockView.setPattern(LockPatternView.DisplayMode.DEFAULT);
            tvMessage.setText("至少连接4个点，请重新输入");
        }
        if (LockActivity.this.cells != null) {
            if (isSame(LockActivity.this.cells, cells)) {
                tvMessage.setText("密码正确！");
                tvMessage.setTextColor(Color.parseColor("#FFFFFF"));
                SharedPreferenceInstance.getInstance().saveIsNeedShowLock(false);
                finish();
            } else {
                lockView.setPattern(LockPatternView.DisplayMode.ERROR);
                lockView.postClearPatternRunnable(500);
                tvMessage.setText("密码错误！");
                tvMessage.setTextColor(Color.parseColor("#FC3F3C"));
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.shake);
                tvMessage.startAnimation(animation);
            }
        }
    }

    private boolean isSame(List<LockPatternView.Cell> cells, List<LockPatternView.Cell> cells1) {
        if (cells.size() != cells1.size()) return false;
        for (int i = 0, len = cells.size(); i < len; i++) {
            if (cells.get(i).getIndex() != cells1.get(i).getIndex()) return false;
        }
        return true;
    }

    @Override
    protected void initData() {
        super.initData();
        String password = EncryUtils.getInstance().decryptString(SharedPreferenceInstance.getInstance().getLockPwd(), "spark");
        cells = new ArrayList<>(password.length());
        for (int i = 0, len = password.length(); i < len; i++) {
            LockPatternView.Cell cell = lockView.new Cell(0, 0, 0, 0, Integer.parseInt(password.charAt(i) + ""));
            cells.add(cell);
        }
        User user = MyApplication.getApp().getCurrentUser();
        String url = user.getAvatar();
        String username = user.getNick_name();
        if (StringUtils.isEmpty(url))
            Glide.with(getApplicationContext()).load(R.mipmap.icon_default_header_grey).into(ivHeader);
        else Glide.with(getApplicationContext()).load(url).into(ivHeader);
        tvNickName.setText(StringUtils.isEmpty(username) ? getResources().getString(R.string.app_name) : username);
    }


    @Override
    protected void loadData() {

    }

    private long lastPressTime = 0;

    @Override
    public void onBackPressed() {
        long now = System.currentTimeMillis();
        if (lastPressTime == 0 || now - lastPressTime > 2 * 1000) {
            ToastUtils.showToast("再按一次退出");
            lastPressTime = now;
        } else if (now - lastPressTime < 2 * 1000) {
            ActivityManage.finishAll();
        }

    }
}
