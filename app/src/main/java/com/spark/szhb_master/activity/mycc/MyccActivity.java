package com.spark.szhb_master.activity.mycc;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.TabFragmentAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.AssetsInfo;
import com.spark.szhb_master.ui.MyViewPager;
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


    @BindView(R.id.llTab)
    LinearLayout llTab;

    @BindView(R.id.viewpager)
    MyViewPager mViewPager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.scrollView)
    JudgeNestedScrollView scrollView;

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.tvJingzhi)
    TextView tvJingzhi;

    @BindView(R.id.tvYingkui)
    TextView tvYingkui;

    @BindView(R.id.tvBzj)
    TextView tvBzj;

    @BindView(R.id.tvCcBzj)
    TextView tvCcBzj;

    @BindView(R.id.tvCc)
    TextView tvCc;

    @BindView(R.id.tvWt)
    TextView tvWt;

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

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
                llTab.getLocationOnScreen(location);
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
                getMyhave();
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
        params.height = ScreenUtil.getScreenHeightPx(getApplicationContext()) - toolBarPositionY - llTab.getHeight() + 1;
        mViewPager.setLayoutParams(params);
    }

    @Override
    protected void initData() {
        new MyccPresenter(Injection.provideTasksRepository(activity.getApplicationContext()), this);
    }

    @Override
    protected void loadData() {
        super.loadData();
        getMyhave();
    }

    private List<String> tabs = new ArrayList<>();
    private TabFragmentAdapter mTabFragmentAdapter;
    private void initTabViewpager() {
        mTabFragments = new ArrayList<>();

        ccFragment= CcFragment.getInstance();

        mTabFragments.add(ccFragment);

        wtFragment = WtFragment.getInstance();
        mTabFragments.add(wtFragment);

        tabs.add("持仓");
        tabs.add("计划委托");

        mViewPager.setAdapter(mTabFragmentAdapter = new TabFragmentAdapter(mTabFragments,tabs,getSupportFragmentManager(),
                this));
        mViewPager.setOffscreenPageLimit(2);

//        tabLayout.setTabMode(TabLayout.MODE_FIXED);
//        tabLayout.setupWithViewPager(mViewPager);

//        for (int i = 0; i < tabLayout.getTabCount(); i++){
//            TabLayout.Tab tab = tabLayout.getTabAt(i);
//            if (tab != null) {
//                tab.setCustomView(mTabFragmentAdapter.getTabView(i));
//            }
//        }
//        View left=tabLayout.getTabAt(0).getCustomView();
//        left.setBackground(getResources().getDrawable(R.drawable.bg_tabview_blue_left));
//        View right=tabLayout.getTabAt(1).getCustomView();
//        right.setBackground(getResources().getDrawable(R.drawable.bg_tabview_gray_right));

        tvCc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvCc.setBackground(getResources().getDrawable(R.drawable.bg_tabview_blue_left));
                tvWt.setBackground(getResources().getDrawable(R.drawable.bg_tabview_gray_right));
                mViewPager.setCurrentItem(0,true);
            }
        });

        tvWt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvCc.setBackground(getResources().getDrawable(R.drawable.bg_tabview_gray_left));
                tvWt.setBackground(getResources().getDrawable(R.drawable.bg_tabview_blue_right));

                mViewPager.setCurrentItem(1,true);
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
    private void getMyhave() {
        mPresenter.getMyHave();
    }


    private TipDialog limitTipDialog,marketTipDialog;

    private AssetsInfo mAssetsInfo;
    /**
     * 获取钱包成功
     */
    @Override
    public void getMyHaveSuccess(AssetsInfo assetsInfo) {
        mAssetsInfo = assetsInfo;

        tvJingzhi.setText(String.valueOf(assetsInfo.getSum()));
        tvYingkui.setText(String.valueOf(assetsInfo.getHarvest_num()));
        tvBzj.setText(String.valueOf(assetsInfo.getUsable()));
        tvCcBzj.setText(String.valueOf(assetsInfo.getUsable_convert()));
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
