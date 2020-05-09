package com.spark.szhb_master.activity.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.main.presenter.C2cBuyPresenterImpl;
import com.spark.szhb_master.activity.main.presenter.MainPresenter;
import com.spark.szhb_master.entity.C2cConfig;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import config.Injection;

public class C2cBuyFragment extends Fragment implements MainContract.BuyView {

    protected View rootView;

    private int mType;// 0资金账户 1合约全仓账户
    Unbinder unbinder;

    @BindView(R.id.rlBuy)
    RelativeLayout rlBuy;

    @BindView(R.id.edInput)
    EditText edInput;

    private MainContract.BuyPresenter presenter;

    private List<Fragment> mTabFragments = new ArrayList<>();

    private boolean bInit = false;

    private C2cConfig c2cConfig;
    public static C2cBuyFragment newInstance(){
        C2cBuyFragment fragment = new C2cBuyFragment();
        return fragment;
    }

    @Override
    public void hideLoadingPopup() {

    }

    @Override
    public void displayLoadingPopup() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_c2cbuy, null);
        return rootView;
    }

    @Override
    public void setPresenter(MainContract.BuyPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);

        rlBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!bInit){
                    ToastUtils.showToast("网络阻塞");
                    return;
                }
                String input = edInput.getText().toString();
                if (StringUtils.isEmpty(input)){
                    ToastUtils.showToast("请输入正确的信息");
                    return;
                }
                Integer integer = Integer.parseInt(input);
                if (integer >= c2cConfig.getMin_num() && integer < c2cConfig.getMax_num()){
                    HashMap hashMap = new HashMap();
                    hashMap.put("type",0);
                    hashMap.put("num",integer);
                    presenter.fastBuy(hashMap);
                }else{
                    ToastUtils.showToast("仅可购买数量为 " + enableBuy);
                }

            }
        });

        presenter = new C2cBuyPresenterImpl(Injection.provideTasksRepository(getActivity().getApplicationContext()), this);
    }

    private String enableBuy;

    public void setC2cConfig(C2cConfig c2cConfig) {
        bInit = true;
        this.c2cConfig = c2cConfig;
        enableBuy = c2cConfig.getMin_num() + " - " + c2cConfig.getMax_num();

        SpannableString ss = new SpannableString(enableBuy);
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(15,true);
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置hint
        edInput.setHint(ss);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @Nullable
    @Override
    public View getView() {
        return rootView;
    }


    @Override
    public void fastBuySuccess(String obj) {
        ToastUtils.showToast(obj);
    }

    @Override
    public void optBuySuccess(String obj) {
        ToastUtils.showToast(obj);
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(getParentFragment(),code,toastMessage);
    }
}
