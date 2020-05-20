package com.spark.szhb_master.activity.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.delegate.PostDelegateActivity;
import com.spark.szhb_master.activity.login.LoginStepOneActivity;
import com.spark.szhb_master.activity.main.presenter.C2CPresenterImpl;
import com.spark.szhb_master.adapter.PagerAdapter;
import com.spark.szhb_master.base.BaseNestingTransFragment;
import com.spark.szhb_master.entity.C2cConfig;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

/**
 * Created by Administrator on 2018/1/29.
 */

public class C2CFragment extends BaseNestingTransFragment implements MainContract.C2CView {

    public static final String TAG = C2CFragment.class.getSimpleName();

    @BindView(R.id.llTitle)
    LinearLayout llTitle;

    @BindView(R.id.viewPager)
    ViewPager vp;
    @BindView(R.id.tvBuy)
    TextView tvBuy;
    @BindView(R.id.tvSell)
    TextView tvSell;

    @BindView(R.id.llBuyAndSell)
    LinearLayout llBuyAndSell;

    @BindView(R.id.ivAdd)
    ImageView ivAdd;

    private MainContract.C2CPresenter presenter;

    private PagerAdapter adapter;
    private ArrayList<String> tabs = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();


    private final String mPageName = "C2CFragment";

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            immersionBar.setTitleBar(getActivity(), llTitle);
            isSetTitle = true;
        }
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onPageStart(mPageName);


    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_c2c;
    }

    @Override
    protected void initView() {
        ivBack.setVisibility(View.GONE);
        tvGoto.setVisibility(View.GONE);
        tvTitle.setVisibility(View.GONE);
        llBuyAndSell.setVisibility(View.VISIBLE);
        tvBuy.setSelected(true);
        tvSell.setSelected(false);

        initFragment();
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new C2CPresenterImpl(Injection.provideTasksRepository(getActivity().getApplicationContext()), this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            recoveryFragments();
        }

    }

    @Override
    protected void loadData() {
        super.loadData();
        presenter.getC2cConfig();
    }

    @OnClick({R.id.tvBuy, R.id.tvSell, R.id.ivAdd})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        if (v.getId() == R.id.ivReleseAd || v.getId() == R.id.ivGoOrder) {
            if (!MyApplication.getApp().isLogin()) {
                showActivity(LoginStepOneActivity.class, null, LoginStepOneActivity.RETURN_LOGIN);
                return;
            }
        }
        switch (v.getId()) {
            case R.id.tvBuy:
                clickTabBuy();
                break;
            case R.id.tvSell:
                clickTabSell();
                break;
            case R.id.ivAdd:
                showActivity(PostDelegateActivity.class,null);
                break;
        }

    }

    private C2cConfig c2cConfig;
    @Override
    public void getC2cConfigSuccess(C2cConfig c2cConfig) {
        this.c2cConfig = c2cConfig;
        c2cFastFragment.setC2cConfig(c2cConfig);
    }

    /**
     * 卖出
     */
    private void clickTabSell() {
        tvBuy.setSelected(false);
        tvSell.setSelected(true);
        vp.setCurrentItem(1);
    }

    /**
     * 买入
     */
    private void clickTabBuy() {
        tvBuy.setSelected(true);
        tvSell.setSelected(false);
        vp.setCurrentItem(0);
    }


    @Override
    public void setPresenter(MainContract.C2CPresenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void doPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(getmActivity(), code, toastMessage);
    }


    public static String getTAG() {
        return TAG;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        outState.putSerializable("coinInfos", (Serializable) coinInfos);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void addFragments() {

    }

    @Override
    protected void recoveryFragments() {
//        if (saveCoinInfos == null || saveCoinInfos.size() == 0) return;
//        coinInfos.clear();
//        coinInfos.addAll(saveCoinInfos);
//        tabs.clear();
//        fragments.clear();
//        for (int i = 0; i < coinInfos.size(); i++) {
//            tabs.add(coinInfos.get(i).getUnit());
//            fragments.add((C2CListFragment) getChildFragmentManager().findFragmentByTag(makeFragmentName(vp.getId(), i)));
//        }
//        if (fragments.size() > 4) headTab.setTabMode(TabLayout.MODE_SCROLLABLE);
//        else headTab.setTabMode(TabLayout.MODE_FIXED);
//        if (adapter == null) {
//            vp.setAdapter(adapter = new PagerAdapter(getChildFragmentManager(), fragments, tabs));
//            headTab.setupWithViewPager(vp);
//            vp.setOffscreenPageLimit(fragments.size() - 1);
//        } else adapter.notifyDataSetChanged();
//        isNeedLoad = false;
    }

    private C2cFastFragment c2cFastFragment;
    private C2cOptionFragment c2cOptionFragment;

    private void initFragment(){
        c2cFastFragment = C2cFastFragment.newInstance();
        c2cOptionFragment = C2cOptionFragment.newInstance();
        fragments.add(c2cOptionFragment);
        fragments.add(c2cFastFragment);

        vp.setAdapter(adapter = new PagerAdapter(getChildFragmentManager(), fragments, tabs));
    }

    @Override
    protected String getmTag() {
        return TAG;
    }
}
