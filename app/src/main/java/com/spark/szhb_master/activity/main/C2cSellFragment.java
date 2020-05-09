package com.spark.szhb_master.activity.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.main.presenter.C2cSellPresenterImpl;
import com.spark.szhb_master.entity.C2cConfig;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import config.Injection;

public class C2cSellFragment extends Fragment implements MainContract.SellView {

    protected View rootView;

    private int mType;// 0资金账户 1合约全仓账户
    Unbinder unbinder;

    @BindView(R.id.rlSell)
    RelativeLayout rlSell;

    @BindView(R.id.edInput)
    EditText edInput;

    @BindView(R.id.tvCanSellNum)
    TextView tvCanSellNum;

    @BindView(R.id.tvMax)
    TextView tvMax;

    private MainContract.SellPresenter presenter;

    private List<Fragment> mTabFragments = new ArrayList<>();

    private C2cConfig c2cConfig;
    public static C2cSellFragment newInstance(){
        C2cSellFragment fragment = new C2cSellFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_c2csell, null);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);

        rlSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!bInit){
                    ToastUtils.showToast("网络阻塞");
                }
                String input = edInput.getText().toString();
                if (TextUtils.isEmpty(input)){
                    ToastUtils.showToast("请输入正确的信息");
                }
                Double num = Double.parseDouble(input);
                if (num > c2cConfig.getMin_num() && num < c2cConfig.getMax_num()){
                    HashMap hashMap = new HashMap();
                    hashMap.put("type",1);
                    hashMap.put("num",num);
                    presenter.fastSell(hashMap);
                }else{
                    ToastUtils.showToast("请输入正确的信息");
                }
            }
        });

        tvMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c2cConfig!=null){
                    edInput.setText(String.valueOf(c2cConfig.getMax_num()));
                }
            }
        });


        presenter = new C2cSellPresenterImpl(Injection.provideTasksRepository(getActivity().getApplicationContext()), this);
    }

    private boolean bInit = false;
    public void setC2cConfig(C2cConfig c2cConfig) {
        bInit = true;
        this.c2cConfig = c2cConfig;
        edInput.setHint(c2cConfig.getMin_num() + " - " + c2cConfig.getMax_num());

//        tvCanSellNum.setText("预估可卖：" + );
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
    public void optBuySuccess(String obj) {
        ToastUtils.showToast(obj);
    }

    @Override
    public void fastBuySuccess(String obj) {
        ToastUtils.showToast(obj);
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(getParentFragment(),code,toastMessage);
    }

    @Override
    public void displayLoadingPopup() {

    }

    @Override
    public void hideLoadingPopup() {

    }

    @Override
    public void setPresenter(MainContract.SellPresenter presenter) {
        this.presenter = presenter;
    }
}
