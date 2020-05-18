package com.spark.szhb_master.activity.mycc;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.PagerAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.AssetsInfo;
import com.spark.szhb_master.ui.intercept.JudgeNestedScrollView;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.ScreenUtil;
import com.spark.szhb_master.utils.StatusBarUtil;
import com.spark.szhb_master.widget.TipDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import config.Injection;


public class MyccActivity extends BaseActivity implements MyccContract.View {
    public static final String TAG = MyccActivity.class.getSimpleName();


    @BindView(R.id.tablayout)
    TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.scrollView)
    JudgeNestedScrollView scrollView;

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private CcFragment ccFragment;
    private WtFragment wtFragment;

    private MyccContract.Presenter mPresenter;

    private List<Fragment> mTabFragments;

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static String getTAG() {
        return TAG;
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_mycc;
    }

    @Override
    protected void initView() {
        super.initView();

        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this, toolbar);

        toolbar.post(new Runnable() {
            @Override
            public void run() {
                dealWithViewPager();
            }
        });

        initTabViewpager();

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            int lastScrollY = 0;

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int[] location = new int[2];
                tabLayout.getLocationOnScreen(location);
                int yPosition = location[1];
                if (yPosition < toolBarPositionY) {
                    scrollView.setNeedScroll(false);
                } else {
                    scrollView.setNeedScroll(true);

                }
                lastScrollY = scrollY;
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ccFragment.refresh();
                wtFragment.refresh();
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                }, 200);
            }
        });
    }

    int toolBarPositionY = 0;

    private void dealWithViewPager() {
        toolBarPositionY = toolbar.getHeight();
        ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
        params.height = ScreenUtil.getScreenHeightPx(getApplicationContext()) - toolBarPositionY - tabLayout.getHeight() + 1;
        mViewPager.setLayoutParams(params);
    }

    @Override
    protected void initData() {
        new MyccPresenter(Injection.provideTasksRepository(activity.getApplicationContext()), this);
    }

    private List<String> tabs = new ArrayList<>();

    private void initTabViewpager() {
        mTabFragments = new ArrayList<>();

        ccFragment= CcFragment.getInstance();

        mTabFragments.add(ccFragment);

        wtFragment = WtFragment.getInstance();
        mTabFragments.add(wtFragment);

        tabs.add("持仓");
        tabs.add("计划委托");

        mViewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), mTabFragments, tabs));
        mViewPager.setOffscreenPageLimit(2);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition(),false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private TipDialog liquidationDialog;

    @Override
    public void setPresenter(MyccContract.Presenter presenter) {
        this.mPresenter = presenter;
    }


    /**
     * 获取钱包
     *
     * @param
     */
    private void getWallet() {
        mPresenter.getWallet();
    }


    private TipDialog limitTipDialog,marketTipDialog;

    private AssetsInfo mAssetsInfo;
    /**
     * 获取钱包成功
     */
    @Override
    public void getWalletSuccess(AssetsInfo assetsInfo) {
        mAssetsInfo = assetsInfo;

    }

    /**
     * 所有数据请求失败的地方
     *
     * @param e
     * @param msg
     */
    @Override
    public void doPostFail(int e, String msg) {
        NetCodeUtils.checkedErrorCode(this, e, msg);
    }
}
