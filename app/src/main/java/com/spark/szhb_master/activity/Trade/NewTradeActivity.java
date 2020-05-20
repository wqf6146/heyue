package com.spark.szhb_master.activity.Trade;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.entrust.CurrentTrustActivity;
import com.spark.szhb_master.activity.kline.KlineActivity;
import com.spark.szhb_master.activity.login.LoginStepOneActivity;
import com.spark.szhb_master.activity.main.MainActivity;
import com.spark.szhb_master.adapter.PagerAdapter;
import com.spark.szhb_master.adapter.SellAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.dialog.EntrustDialog;
import com.spark.szhb_master.dialog.TradeBuyOrSellConfirmDialog;
import com.spark.szhb_master.entity.AssetsInfo;
import com.spark.szhb_master.entity.ContratInfo;
import com.spark.szhb_master.entity.Exchange;
import com.spark.szhb_master.entity.Favorite;
import com.spark.szhb_master.entity.NewCurrency;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.entity.SymbolBean;
import com.spark.szhb_master.entity.SymbolListBean;
import com.spark.szhb_master.entity.SymbolStep;
import com.spark.szhb_master.entity.TcpEntity;
import com.spark.szhb_master.entity.TradeCOM;
import com.spark.szhb_master.factory.socket.NEWCMD;
import com.spark.szhb_master.serivce.SocketResponse;
import com.spark.szhb_master.ui.intercept.JudgeNestedScrollView;
import com.spark.szhb_master.utils.CommonUtils;
import com.spark.szhb_master.utils.DpPxUtils;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.LogUtils;
import com.spark.szhb_master.utils.MathUtils;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.PriceTextWatcher;
import com.spark.szhb_master.utils.ScreenUtil;
import com.spark.szhb_master.utils.StatusBarUtil;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.spark.szhb_master.widget.SymbolDropDownDialog;
import com.spark.szhb_master.widget.TipDialog;
import com.spark.szhb_master.widget.pwdview.PayPasswordView;
import com.xw.repo.BubbleSeekBar;

import org.angmarch.views.NiceSpinner;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

/**
 * author: wuzongjie
 * time  : 2018/4/17 0017 15:20
 * desc  : 这是我重写的交易界面，在原来上我不知道怎么改了
 */

public class NewTradeActivity extends BaseActivity implements TradeContract.View {
    public static final String TAG = NewTradeActivity.class.getSimpleName();

    @BindView(R.id.fldiap)
    FrameLayout fldiap;

//    @BindView(R.id.ivOpen)
//    ImageView ivOpen;

    @BindView(R.id.tvAll)
    TextView tvAll;
    @BindView(R.id.mRadioGroup)
    RadioGroup mRadioGroup;
    @BindView(R.id.limitSpinner)
    NiceSpinner limitSpinner; // 下拉

    @BindView(R.id.floatSpinner)
    NiceSpinner floatSpinner; // 下拉

    @BindView(R.id.recyclerSell)
    RecyclerView recyclerSell; // 卖出的
    @BindView(R.id.recyclerBuy)
    RecyclerView recyclerBuy; // 买进

    @BindView(R.id.tablayout)
    TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.scrollView)
    JudgeNestedScrollView scrollView;

    @BindView(R.id.llMarketBuy)
    LinearLayout llMarketBuy; // 买入
    @BindView(R.id.llMarketSell)
    LinearLayout llMarketSell; // 卖出
    @BindView(R.id.tvBestPriceBuy)
    TextView tvBestPriceBuy; // 最优价
    @BindView(R.id.llBuyPrice)
    LinearLayout llBuyPrice;  // 输入价格
    @BindView(R.id.tvBestPriceSell)
    TextView tvBestPriceSell;
    @BindView(R.id.llSellPrice)
    LinearLayout llSellPrice;
    @BindView(R.id.etBuyPrice)
    EditText etBuyPrice;
    @BindView(R.id.tvBuyAdd)
    TextView tvBuyAdd;
    @BindView(R.id.tvBuyReduce)
    TextView tvBuyReduce;
    @BindView(R.id.etSellPrice)
    EditText etSellPrice;
    @BindView(R.id.tvSellAdd)
    TextView tvSellAdd;
    @BindView(R.id.tvSellReduce)
    TextView tvSellReduce;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.tvBuyCanUse)
    TextView tvBuyCanUse;

    @BindView(R.id.etCount)
    EditText etBuyCount;
    @BindView(R.id.etSellCount)
    EditText etSellCount;
    @BindView(R.id.btnBuy)
    Button btnBuy;

    @BindView(R.id.btnBuyQc)
    Button btnBuyQc;

    @BindView(R.id.btnSellQc)
    Button btnSellQc;

    @BindView(R.id.btnSell)
    Button btnSale;
    @BindView(R.id.tvBuyTradeCount)
    TextView tvBuyTradeCount; // 交易额
    @BindView(R.id.tvSellTradeCount)
    TextView tvSellTradeCount;

    @BindView(R.id.tvBuySymbol)
    TextView tvBuySymbol;
    @BindView(R.id.tvSellSymbol)
    TextView tvSellSymbol;
    @BindView(R.id.llBuyTradeCount)
    LinearLayout llBuyTradeCount;
    @BindView(R.id.llSellTradeCount)
    LinearLayout llSellTradeCount;
    @BindView(R.id.buySeekBar)
    BubbleSeekBar buySeekBar;
    @BindView(R.id.sellSeekBar)
    BubbleSeekBar sellSeekBar;

    @BindView(R.id.rbBuy)
    RadioButton rbBuy;
    @BindView(R.id.rbSell)
    RadioButton rbSell;
    @BindView(R.id.tvPriceTag)
    TextView tvPriceTag;
    @BindView(R.id.tvCountTag)
    TextView tvCountTag;

    @BindView(R.id.tvBuyBalance)
    TextView tvBuyBalance;

    @BindView(R.id.tvSellBalance)
    TextView tvSellBalance;


    @BindView(R.id.tvLiquidation)
    TextView tvLiquidation;

    @BindView(R.id.llSymbolTitle)
    LinearLayout mLlSymbolTitle;

    @BindView(R.id.tvTitleSymbol)
    TextView mTvTitleSymbol;

    @BindView(R.id.tvTitleSymbolType)
    TextView mTvTitleSymbolType;

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.ivChart)
    ImageView ivChart;

    @BindView(R.id.rltitle)
    RelativeLayout rltitle;

    @BindView(R.id.llAll)
    LinearLayout mLlAll;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.edSellTakeProfit)
    EditText edSellTakeProfit;

    @BindView(R.id.edSellStopLoss)
    EditText edSellStopLoss;

    @BindView(R.id.edBuyTakeProfit)
    EditText edBuyTakeProfit;

    @BindView(R.id.edBuyStopLoss)
    EditText edBuyStopLoss;


    private DqccFragment dqccFragment;
    private DqwtFragment dqwtFragment;


    private NewCurrency currency;
    private List<Exchange> sellExchangeList = new ArrayList<>();
    private List<Exchange> buyExchangeList = new ArrayList<>();
    private SellAdapter sellAdapter; // 卖出适配器
    private SellAdapter buyAdapter; // 买入适配器

    private TradeContract.Presenter mPresenter;
    private int baseCoinScale = 2; // 价格
    private int coinScale = 2; // 数量
    private boolean isFace = false;
    private double doubleBuyCount, doubleSellCount, doubleBuyPrice, doubleSellPrice;
    private EntrustDialog dialog;
    private String price = "0"; // 买入的价格
    private String amout = "0"; // 卖出的价格
    private String type = GlobalConstant.LIMIT_PRICE;
    private String orderId;
    private double usdeBalance = -1;
    private TradeBuyOrSellConfirmDialog tradeBuyOrSellConfirmDialog;
    private double sellCountBalance = 0; // 可卖币的多少
    private double sellCountBalance_two = 0; // 可卖币的多少
    private double buyCountBalance = 0; // 可用的币
    private double buyCountBalance_two = 0; // 可用的币

    private boolean isFirst = true;
    private boolean isLoginStateOld;

    private boolean rcvEnable = false;

    private List<Fragment> mTabFragments;

    /**
     * 这个是从主界面来的，表示选择了某个币种
     */
    public void resetSymbol(NewCurrency currency) {
        this.currency = currency;
        if (this.currency != null) {
            etBuyCount.setText("");
            etSellCount.setText("");
//            getExchangeAndSymbolInfo();
            if (MyApplication.getApp().isLogin()) {
//                getWallet(1);
                if (usdeBalance != -1 && "USDT".equals(currency.getSymbol().substring(currency.getSymbol().indexOf("/") + 1, currency.getSymbol().length()))) { // 这里请求USDT多少，但是不用每次都请求故取消了
                    String strCanUse = String.valueOf(MathUtils.getRundNumber(usdeBalance, 2, null) + currency.getSymbol().substring(currency.getSymbol().indexOf("/") + 1, currency.getSymbol().length()));
                    tvBuyCanUse.setText(getString(R.string.text_can_used) + strCanUse);
                } else {
//                    getWallet(2);
                }
            }
            setViewText();
            setTCPBySymbol(currency);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mBackgroundRunnable);
    }

    public static String getTAG() {
        return TAG;
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_trade;
    }

    @Override
    protected void initView() {
        super.initView();

        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this, toolbar);

        if (mSymbolDialog == null) {
            mSymbolDialog = new SymbolDropDownDialog(this, fldiap, mTvTitleSymbolType);
            mSymbolDialog.setOnItemClickCallback(new SymbolDropDownDialog.OnItemClickCallback() {
                @Override
                public void onitemClick(NewCurrency currency) {
                    mSymbolDialog.dismiss();
                    reInit(currency);
                }
            });
        }

        toolbar.post(new Runnable() {
            @Override
            public void run() {
                dealWithViewPager();
            }
        });

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            int lastScrollY = 0;

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int[] location = new int[2];
                mLlAll.getLocationOnScreen(location);
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
                dqccFragment.beginRefreshing();
                dqwtFragment.beginRefreshing();
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
    private int mOffset = 0;
    private int mScrollY = 0;

    private void dealWithViewPager() {
        toolBarPositionY = toolbar.getHeight();
        ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
        params.height = ScreenUtil.getScreenHeightPx(getApplicationContext()) - toolBarPositionY - mLlAll.getHeight() + 1;
        mViewPager.setLayoutParams(params);
    }

    private List<String> mFloatList = new ArrayList<>();
    private List<String> getFloatList(){
        if (mSymbolConfig!=null){
            String config[] = mSymbolConfig.getDepth_config().split(",");
            mFloatList = GlobalConstant.getFloatSizeList(config);
        }
        return mFloatList;
    }

    @Override
    protected void initData() {
        isLoginStateOld = MyApplication.getApp().isLogin();
        limitSpinner.attachDataSource(new LinkedList<>(Arrays.asList(activity.getResources().getStringArray(R.array.text_type))));

        dialog = new EntrustDialog(activity);
        tradeBuyOrSellConfirmDialog = new TradeBuyOrSellConfirmDialog(activity);
        new TradePresenter(Injection.provideTasksRepository(activity.getApplicationContext()), this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currency = (NewCurrency) bundle.getSerializable("currency");
            mSymbolConfig = MyApplication.getApp().getSymbolConfig(currency);
            floatSpinner.attachDataSource(new LinkedList<>(getFloatList()));
            floatSpinner.setPadding(DpPxUtils.dip2px(this,5),DpPxUtils.dip2px(this,3)
                ,DpPxUtils.dip2px(this,5),DpPxUtils.dip2px(this,3));
            int type = bundle.getInt("type");
            if (type == 1) { // 买入
                mRadioGroup.check(R.id.rbBuy);
                rbBuy.setBackgroundResource(R.color.main_font_green);
                rbSell.setBackgroundResource(R.color.main_bg_dark_2);
                llMarketBuy.setVisibility(View.VISIBLE);
                llMarketSell.setVisibility(View.GONE);
            } else { //卖出
                mRadioGroup.check(R.id.rbSell);
                rbBuy.setBackgroundResource(R.color.main_bg_dark_2);
                rbSell.setBackgroundResource(R.color.main_font_red);
                llMarketBuy.setVisibility(View.GONE);
                llMarketSell.setVisibility(View.VISIBLE);
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(0);
                    Message message = new Message();
                    message.what = 0;
                    handler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        HandlerThread thread = new HandlerThread("MyHandlerThread");
        thread.start();//创建一个HandlerThread并启动它
        mHandler = new Handler(thread.getLooper());//使用HandlerThread的looper对象创建Handler，如果使用默认的构造方法，很有可能阻塞UI线程
        mHandler.post(mBackgroundRunnable);//将线程post到Handler中
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
//                    mRadioGroup.check(R.id.rbBuy);
                    LogUtils.i("handler");
                    initSellAndBuyView();
                    //tvBuyTradeCount.setText(getString(R.string.text_entrust) + " --");
                    //tvSellTradeCount.setText(getString(R.string.text_entrust) + " --");
                    setViewListener();
                    if (currency != null) {
                        initTabViewpager();
                        setViewText();
                        checkLogin();
                    }
                    break;
            }
        }
    };

    private List<String> tabs = new ArrayList<>();

    private void initTabViewpager() {
        mTabFragments = new ArrayList<>();

        dqccFragment= DqccFragment.getInstance(currency.getSymbol(), currency.getType());
        dqccFragment.setOnCallBackEvent(new DqccFragment.OnCallBackEvent() {
            @Override
            public void undersellContratSuccess() {
                getWallet();
            }

            @Override
            public void showEmpty(boolean isShow) {
//                rlEmpty.setVisibility(isShow ? View.VISIBLE : View.GONE);
            }
        });
        mTabFragments.add(dqccFragment);

        dqwtFragment = DqwtFragment.getInstance(currency.getSymbol(), currency.getType());
        dqwtFragment.setCallBackEvent(new DqwtFragment.OnCallBackEvent() {
            @Override
            public void undoLimitContratSuccess() {
                getWallet();
            }

            @Override
            public void showEmpty(boolean isShow) {
//                rlEmpty.setVisibility(isShow ? View.VISIBLE : View.GONE);
            }
        });
        mTabFragments.add(dqwtFragment);

        tabs.add("当前持仓");
        tabs.add("当前委托");

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


    /**
     * 根据登录状态显示
     */
    private void checkLogin() {
        if (MyApplication.getApp().isLogin()) {
            getPreInfo();
            getWallet();
            btnBuy.setText("做多" + strSymbol);
            btnSale.setText("做空" + strSymbol);
            dqccFragment.reInit(currency.getSymbol(),currency.getType());
            dqwtFragment.reInit(currency.getSymbol(),currency.getType());
        } else {
            btnBuy.setText("登录/注册");
            btnSale.setText("登录/注册");
        }
        isFirst = false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirst && (isLoginStateOld != MyApplication.getApp().isLogin())) { // 从该界面跳转到登录页，回来刷新数据
            checkLogin();
        }
        isLoginStateOld = MyApplication.getApp().isLogin();
    }


    private String strSymbol;

    private void setViewText() {
        if (currency != null) {
            String symbol = currency.getSymbol();

            mTvTitleSymbol.setText(symbol);
            mTvTitleSymbolType.setText("/" + currency.getType());

            tvPriceTag.setText(getString(R.string.text_price) + symbol);
            tvCountTag.setText(getString(R.string.text_number) + "(" + CommonUtils.getUnitBySymbol(symbol) + ")");
            isFace = addFace(symbol);
            String strClose = currency.getClose();
            if (mSymbolConfig!=null){
                adjustScale(etBuyPrice,Double.parseDouble(strClose));
                adjustScale(etSellPrice,Double.parseDouble(strClose));
                adjustScale(tvPrice,Double.parseDouble(strClose));
            }else{
                etBuyPrice.setText(strClose);
                etSellPrice.setText(strClose);
                tvPrice.setText(strClose);
            }

            tvPrice.setTextColor(Float.parseFloat(currency.getScale()) >= 0 ? ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_green) :
                    ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_red));

            strSymbol = symbol;

            btnBuy.setText(getString(R.string.text_buy_in) + strSymbol);
            btnSale.setText(getString(R.string.text_sale_out) + strSymbol);

            if (GlobalConstant.CNY.equals(CommonUtils.getUnitBySymbol(symbol))) {
                tvMoney.setText("≈" + MathUtils.getRundNumber(Float.parseFloat(currency.getClose()) * 1 * currency.getBaseUsdRate(),
                        2, null) + GlobalConstant.CNY);
            } else {
                tvMoney.setText("≈" + MathUtils.getRundNumber(Float.parseFloat(currency.getClose()) * MainActivity.rate * currency.getBaseUsdRate(),
                        2, null) + GlobalConstant.CNY);
            }
        }
    }

    private SymbolListBean.Symbol mSymbolConfig;
    private void adjustScale(TextView tv,Double value){
        if (mSymbolConfig == null)
            mSymbolConfig = MyApplication.getApp().getSymbolConfig(currency);

        if (mSymbolConfig != null)
            tv.setText(new BigDecimal(value).setScale(GlobalConstant.getFloatSize(mSymbolConfig), RoundingMode.UP).toString());
    }

    /**
     * 初始化卖出的盘口信息和买入的盘口信息
     */
    private void initSellAndBuyView() {
        sellExchangeList = new ArrayList<>();
        buyExchangeList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            sellExchangeList.add(new Exchange(5 - i, "--", "--"));
            buyExchangeList.add(new Exchange(i, "--", "--"));
        }
        recyclerSell.setLayoutManager(new LinearLayoutManager(activity));
        sellAdapter = new SellAdapter(sellExchangeList, 0);
        sellAdapter.setOnClickEvent(new SellAdapter.OnClickEvent() {
            @Override
            public void onClickEvent(String value) {
                if (type.equals(GlobalConstant.LIMIT_PRICE)) {
                    if (!"--".equals(value)) {
                        if (rbBuy.isChecked()) {
                            etBuyPrice.setText( value );
                        } else {
                            etSellPrice.setText(value);
                        }
                    }
                }
            }
        });
        recyclerSell.setAdapter(sellAdapter);
        sellAdapter.setSymbolConfig(mSymbolConfig);

        recyclerBuy.setLayoutManager(new LinearLayoutManager(activity));
        buyAdapter = new SellAdapter(buyExchangeList, 1);
        buyAdapter.setOnClickEvent(new SellAdapter.OnClickEvent() {
            @Override
            public void onClickEvent(String value) {
                if (type.equals(GlobalConstant.LIMIT_PRICE)) {
                    if (!"--".equals(value)) {
                        if (rbBuy.isChecked()) {
                            etBuyPrice.setText( value );
                        } else {
                            etSellPrice.setText(value);
                        }
                    }
                }
            }
        });
        recyclerBuy.setAdapter(buyAdapter);
        buyAdapter.setSymbolConfig(mSymbolConfig);
    }

    private SymbolDropDownDialog mSymbolDialog;
    private TipDialog liquidationDialog;

    @OnClick({R.id.tvPrice, R.id.tvLiquidation,
            R.id.llSymbolTitle, R.id.ivBack, R.id.ivChart, R.id.tvBuyAdd,
            R.id.tvBuyReduce, R.id.tvSellAdd, R.id.tvSellReduce, R.id.tvAll,
            R.id.btnBuy, R.id.btnSell, R.id.btnBuyQc, R.id.btnSellQc})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.llSymbolTitle:
                if (mSymbolDialog != null && !mSymbolDialog.isHasShow()) {
                    mSymbolDialog.show();
                } else if (mSymbolDialog != null && mSymbolDialog.isHasShow()) {
                    mSymbolDialog.dismiss();
                }
                break;
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvLiquidation:
                if (liquidationDialog == null) {
                    liquidationDialog = new TipDialog.Builder(this)
                            .setPicResources(R.mipmap.woo)
                            .setMessage("是否一键平仓?")
                            .setCanCancelOutside(false)
                            .setEnsureClickListener(new TipDialog.EnsureClickListener() {
                                @Override
                                public void onEnsureClick(TipDialog tipDialog) {
                                    tipDialog.dismiss();
                                    mPresenter.commitLiquidation();
                                }
                            }).build();
                }
                liquidationDialog.show();
                break;
            case R.id.tvPrice:
                if (rbBuy.isChecked()) {
                    etBuyPrice.setText(tvPrice.getText());
                } else {
                    etSellPrice.setText(tvPrice.getText());
                }
                break;
            case R.id.ivChart:
//                Bundle bundle = new Bundle();
//                bundle.putString("symbol", currency.getSymbol());
//                bundle.putSerializable("currency", currency);
//                bundle.putInt("tcpstatus", 1);
//                stopTcp();
//                showActivity(KlineActivity.class, bundle, 1);
                finish();
                break;
//            case R.id.tvLogin: // 点击登录
//                showActivity(LoginActivity.class, null, LoginActivity.RETURN_LOGIN);
//                break;
            case R.id.tvBuyAdd:
            case R.id.tvBuyReduce:
                try {
                    if (v.getId() == R.id.tvBuyAdd) {
                        BigDecimal src = new BigDecimal(etBuyPrice.getText().toString());
                        etBuyPrice.setText(String.valueOf(src.add(new BigDecimal("0.01")).doubleValue()));
                    } else {
                        double num = Double.valueOf(etBuyPrice.getText().toString());
                        if ((num - 0.01) > 0) {
                            BigDecimal src = new BigDecimal(etBuyPrice.getText().toString());
                            etBuyPrice.setText(String.valueOf(src.subtract(new BigDecimal("0.01")).doubleValue()));

                            //etBuyPrice.setText(String.valueOf(num - 0.01));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tvSellAdd:
            case R.id.tvSellReduce:
                try {

                    if (v.getId() == R.id.tvSellAdd) {
                        BigDecimal src = new BigDecimal(etSellPrice.getText().toString());
                        etSellPrice.setText(String.valueOf(src.add(new BigDecimal("0.01")).doubleValue()));
                    } else {
                        double num1 = Double.valueOf(etSellPrice.getText().toString());
                        if ((num1 - 0.01) > 0) {
                            //etSellPrice.setText(String.valueOf(num1 - 0.01));

                            BigDecimal src = new BigDecimal(etSellPrice.getText().toString());
                            etSellPrice.setText(String.valueOf(src.subtract(new BigDecimal("0.01")).doubleValue()));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tvAll: // 查看当前委托的全部
                if (MyApplication.getApp().isLogin() && currency != null) { // 如果登录了就可以进入
                    Bundle bundle = new Bundle();
                    bundle.putString("currency", currency.getSymbol());
                    bundle.putString("type", currency.getType());
                    showActivity(CurrentTrustActivity.class, bundle);
                } else if (!MyApplication.getApp().isLogin()) {
                    showActivity(LoginStepOneActivity.class, null, LoginStepOneActivity.RETURN_LOGIN);
                }
                break;
            case R.id.btnBuy: // 买入
                if (MyApplication.getApp().isLogin()) {
                    buyOrSell(0, 0);
                } else {
                    showActivity(LoginStepOneActivity.class, null, LoginStepOneActivity.RETURN_LOGIN);
                }
                break;
            case R.id.btnSell: // 卖出
                if (MyApplication.getApp().isLogin()) {
                    buyOrSell(1, 0);
                } else {
                    showActivity(LoginStepOneActivity.class, null, LoginStepOneActivity.RETURN_LOGIN);
                }
                break;
            case R.id.btnBuyQc:
                if (MyApplication.getApp().isLogin()) {
                    buyOrSell(0, 1);
                } else {
                    showActivity(LoginStepOneActivity.class, null, LoginStepOneActivity.RETURN_LOGIN);
                }
                break;
            case R.id.btnSellQc:
                if (MyApplication.getApp().isLogin()) {
                    buyOrSell(1, 1);
                } else {
                    showActivity(LoginStepOneActivity.class, null, LoginStepOneActivity.RETURN_LOGIN);
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
            if (currency != null)
                setTCPBySymbol(currency);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private boolean tcpStatus = false;

    private void stopTcp() {
        if (tcpStatus) {
            tcpStatus = false;
            stop(currency.getSymbol(), currency.getType(),mSymbolConfig.getDepth_default());
        }
    }

    private int from = 0;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    loadData();
                    break;
                case 1:
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        int type = bundle.getInt("type");
                        String symbol = bundle.getString("symbol");
                        showPageFragment(symbol, type - 1); // 当type=1 交易fragment就显示买入，对应的page就是0 （即type -1），当type=2，同理
                    }
                    break;
            }

        }
    }

    @Override
    public void commitLiquidationSuccess(String msg) {
        ToastUtils.showToast("一键平仓成功");
        ((DqccFragment) mTabFragments.get(0)).beginRefreshing();
    }

    @Override
    public void setPresenter(TradeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private Boolean mCanFly = true;

    /**
     * 价格，数量监听
     */
    private class MyTextWatcher implements TextWatcher {
        private int intType;
        private double exchangeRate = 1;

        public MyTextWatcher(int intType) {
            this.intType = intType;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (TextUtils.isEmpty(charSequence)){
                tvBuyTradeCount.setText("--");
                tvSellTradeCount.setText("--");
                return;
            }
            if (currency == null || mContartInfo==null) return;

            if (intType == 0 || intType == 2) { // 卖出价格
                if (StringUtils.isNotEmpty(etBuyPrice.getText().toString().trim())) {
                    doubleBuyPrice = MathUtils.getDoubleTransferString(etBuyPrice.getText().toString().trim());
                } else {
                    doubleBuyPrice = 0.0;
                }

                String strBuyCount = etBuyCount.getText().toString().trim();
                if (StringUtils.isNotEmpty(strBuyCount)) {
                    doubleBuyCount = MathUtils.getDoubleTransferString(strBuyCount);
                } else {
                    doubleBuyCount = 0.0;
                }

                if (doubleBuyCount!=0 && doubleBuyPrice!=0){
                    CalCanPlaceOrderNum(doubleBuyPrice);
                    if (doubleBuyCount > mCanPlaceOrderNum) {
                        ToastUtils.showToast("可用量不足");
                        mCanFly = false;
                    } else {
                        mCanFly = true;
                    }

                    if (type.equals(GlobalConstant.LIMIT_PRICE)) { // 限价
                        tvBuyTradeCount.setText(getTradeNum(doubleBuyPrice, doubleBuyCount) + "USDT");
                    } else {
                        tvBuyTradeCount.setText(getString(R.string.text_entrust) + " --");
                    }
                }

            } else if (intType == 1 || intType == 3) { // 买入价格
                if (StringUtils.isNotEmpty(etSellPrice.getText().toString().trim())) {
                    doubleSellPrice = MathUtils.getDoubleTransferString(etSellPrice.getText().toString().trim());
                } else {
                    doubleSellPrice = 0.0;
                }

                String strSellCount = etSellCount.getText().toString().trim();
                if (StringUtils.isNotEmpty(strSellCount)) {
                    doubleSellCount = MathUtils.getDoubleTransferString(strSellCount);
                } else {
                    doubleSellCount = 0.0;
                }

                if (doubleSellCount!=0 && doubleSellPrice!=0){
                    CalCanPlaceOrderNum(doubleSellPrice);
                    if (doubleSellCount > mCanPlaceOrderNum) {
                        ToastUtils.showToast("可用量不足");
                        mCanFly = false;
                    } else {
                        mCanFly = true;
                    }

                    if (type.equals(GlobalConstant.LIMIT_PRICE)) {
                        tvSellTradeCount.setText(getTradeNum(doubleSellPrice, doubleSellCount) + " USDT");
                    } else {
                        tvSellTradeCount.setText(getString(R.string.text_entrust) + " " + String.valueOf("--"));
                    }
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    private PriceTextWatcher etBuyPriceWatcher,etSellPriceWatcher,
            edBuyTakeProfitWatcher,edBuyStopLossWatcher,
            edSellTakeProfitWatcher,edSellStopLossWatcher;


    //合约余额/(挂单价格*杠杆(60/120/200)/隐藏杠杆) 后取整
    private int mCanPlaceOrderNum;
    private void CalCanPlaceOrderNum(double price){
        //mAssetsInfo.getFrost() / (price * Integer.parseInt(currency.getType())/ mContartInfo.getHidden_leverage() );
        if (mAssetsInfo==null || mContartInfo==null) return;
        BigDecimal bUsable = new BigDecimal(mAssetsInfo.getUsable());
        BigDecimal bPrice = new BigDecimal(price);
        BigDecimal bHiddenL = new BigDecimal(mContartInfo.getHidden_leverage());
        mCanPlaceOrderNum = bUsable.divide( bPrice.multiply(new BigDecimal(currency.getType())).divide(bHiddenL,5),5).intValue();
    }

    //保证金=挂单价格*挂单手数*杠杆(60/120/200)/隐藏杠杆   可挂单手数 = 合约余额/交易额后取整
    //      价格✖数量✖倍数(60/120/200)/隐藏杠杆
    private double getTradeNum(double orderprice,double ordernum){
        BigDecimal m = new BigDecimal(orderprice * ordernum * Integer.parseInt(currency.getType())).setScale(6,RoundingMode.UP);
        return m.divide(new BigDecimal(mContartInfo.getHidden_leverage()),9,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private void setViewListener() {
        tradeBuyOrSellConfirmDialog.getTvConfirm().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                commitEntrust();
                tradeBuyOrSellConfirmDialog.dismiss();
            }
        });

        dialog.setOnCancelOrder(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.cancelEntrust(orderId);
                dialog.dismiss();
            }
        });

        limitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setViewByType(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        setViewByType(0);

        floatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String buyP = etBuyPrice.getText().toString();
                String sellP = etSellPrice.getText().toString();
                etBuyPrice.setText("");
                etSellPrice.setText("");
                SymbolListBean.Symbol symbol = sellAdapter.getSymbolConfig();
                String newdepth = GlobalConstant.getFloatStrName(mFloatList.get(i));
                changeDepthTcp(currency.getSymbol(),currency.getType(),mSymbolConfig.getDepth_default(),newdepth);
                symbol.setDepth_default(newdepth);
                mSymbolConfig = symbol;

                addTextWatcher();

                if (rbBuy.isChecked()) {
                    if (StringUtils.isNotEmpty(buyP) && !buyP.equals("--")){
                        adjustScale(etBuyPrice,Double.parseDouble(buyP));
                    }
                } else {
                    if (StringUtils.isNotEmpty(sellP) && !sellP.equals("--")){
                        adjustScale(etSellPrice,Double.parseDouble(sellP));
                    }
                }
                sellAdapter.setSymbolConfig(symbol);
                buyAdapter.setSymbolConfig(symbol);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        addTextWatcher();

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rbBuy: // 点击买入
                        rbBuy.setBackgroundResource(R.color.main_font_green);
                        rbSell.setBackgroundResource(R.color.main_bg_dark_2);
                        llMarketBuy.setVisibility(View.VISIBLE);
                        llMarketSell.setVisibility(View.GONE);
                        break;
                    case R.id.rbSell: // 点击卖出
                        rbBuy.setBackgroundResource(R.color.main_bg_dark_2);
                        rbSell.setBackgroundResource(R.color.main_font_red);
                        llMarketBuy.setVisibility(View.GONE);
                        llMarketSell.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    private void addTextWatcher(){

        if (etBuyPriceWatcher!=null)
            etBuyPrice.removeTextChangedListener(etBuyPriceWatcher);
        if (etSellPriceWatcher!=null)
            etSellPrice.removeTextChangedListener(etSellPriceWatcher);
        if (edBuyTakeProfitWatcher!=null)
            edBuyTakeProfit.removeTextChangedListener(edBuyTakeProfitWatcher);
        if (edBuyStopLossWatcher!=null)
            edBuyStopLoss.removeTextChangedListener(edBuyTakeProfitWatcher);
        if (edSellTakeProfitWatcher!=null)
            edSellTakeProfit.removeTextChangedListener(edSellTakeProfitWatcher);
        if (edSellStopLossWatcher!=null)
            edSellStopLoss.removeTextChangedListener(edSellStopLossWatcher);
        if (mSymbolConfig != null){
            int size = GlobalConstant.getFloatSize(mSymbolConfig);
            etBuyPriceWatcher = new PriceTextWatcher(etBuyPrice,size).addTextChangedListener(new MyTextWatcher(0));
            etSellPriceWatcher = new PriceTextWatcher(etSellPrice,size).addTextChangedListener(new MyTextWatcher(1));
            edBuyTakeProfitWatcher = new PriceTextWatcher(edBuyTakeProfit,size);
            edBuyStopLossWatcher = new PriceTextWatcher(edBuyStopLoss,size);
            edSellTakeProfitWatcher = new PriceTextWatcher(edSellTakeProfit,size);
            edSellStopLossWatcher = new PriceTextWatcher(edSellStopLoss,size);
        }else{
            etBuyPriceWatcher = new PriceTextWatcher(etBuyPrice).addTextChangedListener(new MyTextWatcher(0));
            etSellPriceWatcher = new PriceTextWatcher(etSellPrice).addTextChangedListener(new MyTextWatcher(1));
            edBuyTakeProfitWatcher = new PriceTextWatcher(edBuyTakeProfit);
            edBuyStopLossWatcher = new PriceTextWatcher(edBuyStopLoss);
            edSellTakeProfitWatcher = new PriceTextWatcher(edSellTakeProfit);
            edSellStopLossWatcher = new PriceTextWatcher(edSellStopLoss);
        }
        etBuyPrice.addTextChangedListener(etBuyPriceWatcher);
        etSellPrice.addTextChangedListener(etSellPriceWatcher);
        etBuyCount.addTextChangedListener(new MyTextWatcher(2));
        etSellCount.addTextChangedListener(new MyTextWatcher(3));


        edBuyTakeProfit.addTextChangedListener(edBuyTakeProfitWatcher);
        edBuyStopLoss.addTextChangedListener(edBuyStopLossWatcher);
        edSellTakeProfit.addTextChangedListener(edSellTakeProfitWatcher);
        edSellStopLoss.addTextChangedListener(edSellStopLossWatcher);
    }

    public class MyProgressChangedListener implements BubbleSeekBar.OnProgressChangedListener {
        private int intType; // 0-买，1-卖

        public MyProgressChangedListener(int intTyepe) {
            this.intType = intTyepe;
        }

        @Override
        public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int i, float v) {

        }

        @Override
        public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float v) {
            if (currency == null)
                return;
            if (intType == 0) {
                if (type.equals("LIMIT_PRICE")) {
                    try {
                        double price = Double.valueOf(etBuyPrice.getText().toString());
                        if (price == 0) price = 1;
                        double d = buyCountBalance_two / price;
                        etBuyCount.setText(MathUtils.getRundNumber(d * progress / 1000, 4, null));
                    } catch (Exception e) {
                        etBuyCount.setText(MathUtils.getRundNumber(progress * progress / 1000, 4, null));
                    }
                } else {
                    etBuyCount.setText((Double.valueOf(MathUtils.getRundNumber(buyCountBalance, 2, null)) * progress) / 1000 + "");
//                    String.valueOf(MathUtils.getRundNumber(buyCountBalance, 2, null)
//                    etBuyCount.setText(MathUtils.getRundNumber(buyCountBalance * progress / 1000, 4, null));
//                    etBuyCount.setText(MathUtils.getRundNumber(Double.valueOf(usdeBalance) * progress / 1000, 4, null));
                }
            } else {
                if (type.equals("LIMIT_PRICE")) {
                    try {
                        double price = Double.valueOf(etSellPrice.getText().toString());
                        if (price == 0) price = 1;
//                        double d = sellCountBalance / price;
                        double d = sellCountBalance_two;
                        etSellCount.setText(MathUtils.getRundNumber(d * progress / 1000, 4, null));
                    } catch (Exception e) {
                        etSellCount.setText(MathUtils.getRundNumber(progress * progress / 1000, 4, null));
                    }
                } else {
//                    etSellCount.setText((int) (sellCountBalance * progress / 1000));
                    etSellCount.setText(MathUtils.getRundNumber(sellCountBalance * progress / 1000, 4, null));
                }
//                etSellCount.setText(MathUtils.getRundNumber(sellCountBalance * progress / 1000, 4, null));
            }
        }

        @Override
        public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int i, float v) {

        }
    }

    /**
     * 根据市价还是限价显示view
     */
    private void setViewByType(int intType) {
        type = (intType == 1 ? GlobalConstant.LIMIT_PRICE : GlobalConstant.MARKET_PRICE);
        llBuyPrice.setVisibility(intType == 1 ? View.VISIBLE : View.GONE);
        llSellPrice.setVisibility(intType == 1 ? View.VISIBLE : View.GONE);
        tvBestPriceSell.setVisibility(intType == 1 ? View.GONE : View.VISIBLE);
        tvBestPriceBuy.setVisibility(intType == 1 ? View.GONE : View.VISIBLE);
        llBuyTradeCount.setVisibility(intType == 1 ? View.VISIBLE : View.INVISIBLE);
        llSellTradeCount.setVisibility(intType == 1 ? View.VISIBLE : View.INVISIBLE);

        String price = tvPrice.getText().toString();
        if (StringUtils.isNotEmpty(price) && !price.equals("--"))
            adjustScale(etBuyPrice,Double.parseDouble(price));
//        if (currency != null) {
            //String symbol = (intType == 0 ? currency.getSymbol().substring(0, currency.getSymbol().indexOf("/")) : currency.getSymbol().substring(currency.getSymbol().indexOf("/") + 1, currency.getSymbol().length()));
//            tvBuySymbol.setText(currency.getSymbol());
//            tvSellSymbol.setText(currency.getSymbol());
//        }
//        etBuyCount.setHint(intType == 0 ? getString(R.string.text_number) : getString(R.string.text_entrust));
    }

    /**
     * 判断是否有收藏的数据
     *
     * @param symbol
     * @return
     */
    private boolean addFace(String symbol) {
        for (Favorite favorite : MainActivity.mFavorte) {
            if (symbol.equals(favorite.getSymbol())) return true;
        }
        return false;
    }


    /**
     * 获取钱包
     *
     * @param
     */
    private void getWallet() {
        mPresenter.getWallet();
    }

    /**
     * 获取
     */
    private void getPreInfo() {
        HashMap<String, String> map = new HashMap<>();
        map.put("mark", currency.getSymbol());
        map.put("leverage", currency.getType());
        mPresenter.getContratInfo(map);


//        mPresenter.getExchange(map);
//        mPresenter.getSymbolInfo(map);
    }

    private ContratInfo mContartInfo;

    @Override
    public void getContratInfoSuccess(ContratInfo info) {
        mContartInfo = info;
    }

    private void openPayPasswordDialog(final boolean lOrm) {
        PayPasswordView payPasswordView = new PayPasswordView(this);
        payPasswordView.setbTwice(false);
        payPasswordView.setTipTv("请输入资金密码");

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        payPasswordView.setPasswordFullListener(new PayPasswordView.PasswordFullListener() {
            @Override
            public void passwordFull(String password) {
                bottomSheetDialog.dismiss();
                if (lOrm)
                    commitLimitOrder(Integer.parseInt(password));
                else
                    commitMarketOrder(Integer.parseInt(password));

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



    private TipDialog limitTipDialog,marketTipDialog;
    @Override
    public void commitLimitOrderSuccess(String message) {

        if (limitTipDialog == null){
            limitTipDialog = new TipDialog.Builder(this)
                    .setPicResources(R.mipmap.nike)
                    .setMessage("委托提交成功")
                    .setEnsureText("我知道了")
                    .setCanCancelOutside(true)
                    .setCancelIsShow(false)
                    .setEnsureClickListener(new TipDialog.EnsureClickListener() {
                        @Override
                        public void onEnsureClick(TipDialog tipDialog) {
                            tipDialog.dismiss();
                        }
                    }).build();
        }
        limitTipDialog.show();

        getWallet();
        ((DqwtFragment)mTabFragments.get(1)).beginRefreshing();
        ((DqccFragment)mTabFragments.get(0)).beginRefreshing();
    }

    @Override
    public void commitMarketOrderSuccess(String message) {
        if (marketTipDialog == null){
            marketTipDialog = new TipDialog.Builder(this)
                    .setPicResources(R.mipmap.nike)
                    .setMessage("委托成交成功")
                    .setCanCancelOutside(false)
                    .setEnsureText("我知道了")
                    .setCancelIsShow(false)
                    .setEnsureClickListener(new TipDialog.EnsureClickListener() {
                        @Override
                        public void onEnsureClick(TipDialog tipDialog) {
                            tipDialog.dismiss();
                        }
                    }).build();
        }
        marketTipDialog.show();

        getWallet();
        ((DqwtFragment)mTabFragments.get(1)).beginRefreshing();
        ((DqccFragment)mTabFragments.get(0)).beginRefreshing();
    }

    /**
     * 提交限价委托
     */
    private void commitLimitOrder(int num_type) {
        HashMap map = new HashMap<>();
        map.put("mark", currency.getSymbol());
        map.put("price", Double.parseDouble(price));
        map.put("num", Double.parseDouble(amout));
        int type = rbBuy.isChecked() ? 0 : 1;
        map.put("type", type);
        map.put("leverage", currency.getType());
        map.put("num_type",num_type);
        if (!StringUtils.isEmpty(lossPrice)){
            map.put("loss_price",Double.parseDouble(lossPrice));
        }
        if (!StringUtils.isEmpty(harvestPrice)){
            map.put("harvest_price",Double.parseDouble(harvestPrice));
        }
        mPresenter.commitLimitOrder(map);
    }
    private void commitMarketOrder(int num_type) {
        HashMap map = new HashMap<>();
        map.put("mark", currency.getSymbol());
        map.put("num", Double.parseDouble(amout));
        int type = rbBuy.isChecked() ? 0 : 1;
        map.put("type", type);
        map.put("leverage", currency.getType());
        map.put("num_type",num_type);

        if (!StringUtils.isEmpty(lossPrice)){
            map.put("loss_price",Double.parseDouble(lossPrice));
        }
        if (!StringUtils.isEmpty(harvestPrice)){
            map.put("harvest_price",Double.parseDouble(harvestPrice));
        }
        mPresenter.commitMarketOrder(map);
    }

    @Override
    public void addCollectSuccess(String message) {
        ToastUtils.showToast(getString(R.string.text_add_success));
        isFace = true;
//        ivCollect.setImageDrawable(getResources().getDrawable(R.mipmap.icon_collect_hover));
//        ((MainActivity) activity).find();
        setResult(RESULT_OK);
    }

    @Override
    public void deleteCollectSuccess(String message) {
        ToastUtils.showToast(getString(R.string.text_cancel_success));
        isFace = false;
//        ivCollect.setImageDrawable(getResources().getDrawable(R.mipmap.icon_collect_normal));
//        ((MainActivity) activity).find();
        setResult(RESULT_OK);
    }

    private SafeSetting safeSetting;

    @Override
    public void safeSettingSuccess(SafeSetting obj) {
//        if (obj == null)
//            return;
//        safeSetting = obj;
//        if (safeSetting.getRealVerified() == 0) {
//            ShiMingDialog shiMingDialog = new ShiMingDialog(this);
//            String name = safeSetting.getRealNameRejectReason();
//            if (safeSetting.getRealVerified() == 0) {
//                if (safeSetting.getRealAuditing() == 1) {
//                    shiMingDialog.setEntrust(7, name, 1);
//                } else {
//                    if (safeSetting.getRealNameRejectReason() != null)
//                        shiMingDialog.setEntrust(8, name, 1);
//                    else
//                        shiMingDialog.setEntrust(9, name, 1);
//                }
//            } else {
//                shiMingDialog.setEntrust(6, name, 1);
//            }
//            shiMingDialog.show();
//        } else {
//            buyOrSell(from);
//        }
    }

    /**
     * 获取精度
     *
     * @param
     * @param
     */
    @Override
    public void getSymbolInfoResult(final int mCoinScale, final int mBaseCoinScale) {
        coinScale = mCoinScale;// 数量
        baseCoinScale = mBaseCoinScale; // 价格
        if (sellAdapter == null || buyAdapter == null) return;
        sellAdapter.setText(new SellAdapter.myText() {
            @Override
            public int one() {
                return mCoinScale;
            }

            @Override
            public int two() {
                return mBaseCoinScale;
            }
        });
        sellAdapter.notifyDataSetChanged();
        buyAdapter.setText(new SellAdapter.myText() {
            @Override
            public int one() {
                return mCoinScale;
            }

            @Override
            public int two() {
                return mBaseCoinScale;
            }
        });
        buyAdapter.notifyDataSetChanged();
    }

    private AssetsInfo mAssetsInfo;
    /**
     * 获取钱包成功
     */
    @Override
    public void getWalletSuccess(AssetsInfo assetsInfo) {
        mAssetsInfo = assetsInfo;
        String balance = new BigDecimal(new Double(assetsInfo.getUsable())).setScale(2,BigDecimal.ROUND_HALF_UP)
                + " USDT";
        tvBuyBalance.setText(balance);
        tvSellBalance.setText(balance);

    }

    /**
     * 盘口的数据
     */
    private TradeCOM mTradeCOM;
    @Override
    public void getExchangeSuccess(TradeCOM tradeCOM) {
        mTradeCOM = tradeCOM;
    }

    /**
     * 取消某个委托成功
     */
    @Override
    public void cancelSuccess(String success) {
        if (MyApplication.getApp().isLogin() && this.currency != null) {
            getWallet(); // 应该还需要刷新下钱包的接口
        }
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
//        if (isGetWallet) {
//            getWalletSuccess(null, 3);
//        }
    }

    private JSONObject buildGetBodyJson(String value, String type) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("value", value);
            obj.put("type", type);
            return obj;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 这里我开始订阅某个币盘口的信息
     */
    private void startTCP(String symbol, String type,String depth) {
        tcpStatus = true;
        String st = "market." + symbol + "_" + type + ".depth." + depth;
        MyApplication.getApp().startTcp(new TcpEntity(st,NEWCMD.SUBSCRIBE_SYMBOL_DEPTH));

        String sthead = "market." + symbol + "_" + type + ".detail";
        MyApplication.getApp().startTcp(new TcpEntity(sthead,NEWCMD.SUBSCRIBE_SYMBOL_DETAIL));
    }

    private void changeDepthTcp(String symbol, String type,String oldDepth,String newDepth){
        String st = "market." + symbol + "_" + type + ".depth." + oldDepth;
        MyApplication.getApp().stopTcp(new TcpEntity(st,NEWCMD.SUBSCRIBE_SYMBOL_DEPTH));

        String stnew = "market." + symbol + "_" + type + ".depth." + newDepth;
        MyApplication.getApp().startTcp(new TcpEntity(stnew,NEWCMD.SUBSCRIBE_SYMBOL_DEPTH));
    }

    /**
     * 这里我取消某个币盘口的信息
     */
    private void stop(String symbol, String type,String depth) {

        String st = "market." + symbol + "_" + type + ".depth." + depth;
        MyApplication.getApp().stopTcp(new TcpEntity(st,NEWCMD.SUBSCRIBE_SYMBOL_DEPTH));

        String sthead = "market." + symbol + "_" + type + ".detail";
        MyApplication.getApp().stopTcp(new TcpEntity(sthead,NEWCMD.SUBSCRIBE_SYMBOL_DETAIL));
    }

    public void showPageFragment(String symbol, int pageNo) {
        if (currency != null) {
            try {
                if (GlobalConstant.LIMIT_PRICE.equals(type))
//                    tvBuySymbol.setText(symbol);
//                tvSellSymbol.setText(symbol);
                btnBuy.setText(String.valueOf(getString(R.string.text_buy_in) + symbol));
                btnSale.setText(String.valueOf(getString(R.string.text_sale_out) + symbol));
//                getExchangeAndSymbolInfo();
                setTCPBySymbol(currency);
                if (pageNo == 0) { // 买入
                    mRadioGroup.check(R.id.rbBuy);
                } else { //卖出
                    mRadioGroup.check(R.id.rbSell);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void reInit(NewCurrency currency){

        sellExchangeList.clear();
        buyExchangeList.clear();
        for (int i = 0; i < 6; i++) {
            sellExchangeList.add(new Exchange(5 - i, "--", "--"));
            buyExchangeList.add(new Exchange(i, "--", "--"));
        }

        stop(this.currency.getSymbol(), this.currency.getType(),mSymbolConfig.getDepth_default());

        mSymbolConfig = MyApplication.getApp().getSymbolConfig(currency);
        floatSpinner.attachDataSource(new LinkedList<>(getFloatList()));

        addTextWatcher();

        sellAdapter.setSymbolConfig(mSymbolConfig);
        buyAdapter.setSymbolConfig(mSymbolConfig);


        startTCP(currency.getSymbol(), currency.getType(),mSymbolConfig.getDepth_default());
        this.currency = currency;


        setViewText();
        checkLogin();
    }


    /**
     * 代表选择了哪个币种，需要重新订阅 应该先取消原来的再订阅现在的
     */
    private void setTCPBySymbol(NewCurrency currency) {
        startTCP(currency.getSymbol(), currency.getType(),mSymbolConfig.getDepth_default());
    }

    /**
     * 买入或者卖出
     *
     * @param intType 0-买入，1-卖出
     */
    private TipDialog tolowTipDialog;
    private String lossPrice,harvestPrice;
    private void buyOrSell(int intType,int num_type) {
        if (!MyApplication.getApp().isLogin()) {
            ToastUtils.showToast(getString(R.string.text_login_first));
            return;
        }
        if (currency == null) return;
        String total = "";
        String lastUnit = currency.getSymbol();
        String fistUnit = currency.getType();
        switch (type) {
            case GlobalConstant.LIMIT_PRICE:
                price = (intType == 0 ? etBuyPrice.getText().toString().trim() : etSellPrice.getText().toString().trim());
                amout = (intType == 0 ? etBuyCount.getText().toString().trim() : etSellCount.getText().toString().trim());
                break;
            case GlobalConstant.MARKET_PRICE:
                price = "0.0";
                amout = (intType == 0 ? etBuyCount.getText().toString().trim() : etSellCount.getText().toString().trim());
                break;
        }

        lossPrice = (intType == 0 ? edBuyStopLoss.getText().toString().trim() : edSellStopLoss.getText().toString().trim());
        harvestPrice = (intType == 0 ? edBuyTakeProfit.getText().toString().trim() : edSellTakeProfit.getText().toString().trim());

        if (num_type == 1){
            amout = "0";
        }
        if (StringUtils.isEmpty(amout, price) && num_type == 0) {
            ToastUtils.showToast(getString(R.string.incomplete_information));
        } else if (Double.parseDouble(amout) == 0 && num_type == 0) {
            ToastUtils.showToast("委托数量不能为0");
        } else {

            if (mCanFly){
                if (type == GlobalConstant.LIMIT_PRICE)
                    commitLimitOrder(num_type);
                else
                    commitMarketOrder(num_type);
            }else{
                if (tolowTipDialog == null){
                    tolowTipDialog = new TipDialog.Builder(this)
                            .setMessage("合约账户可用保证金不足，您可前往划转保证金或调整当前委托数量")
                            .setEnsureText("前往划转")
                            .setCancelText("取消")
                            .setCanCancelOutside(true)
                            .setEnsureClickListener(new TipDialog.EnsureClickListener() {
                                @Override
                                public void onEnsureClick(TipDialog tipDialog) {
                                    tipDialog.dismiss();
                                }
                            }).build();
                }
                tolowTipDialog.show();
            }

        }
    }
    private Handler mHandler;
    private boolean mRunning = true;

    Runnable mBackgroundRunnable = new Runnable() {

        @Override
        public void run() {
            while(mRunning){
                try {
                    rcvEnable = true;
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    };



    /**
     * 盘口信息的返回 和 当前委托的返回
     *
     * @param response SocketResponse
     */
    private List<NewCurrency> mCurrencyListAll = new ArrayList<>();
    private Gson gson = new Gson();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(SocketResponse response) {
        NEWCMD cmd = response.getCmd();
        if (cmd == null) return;

        if (cmd == NEWCMD.SUBSCRIBE_SYMBOL_DEPTH && rcvEnable){
            rcvEnable = false;
            setResponse(response);
        }else if (cmd == NEWCMD.SUBSCRIBE_SYMBOL_DETAIL){
            SymbolBean symbolBean = gson.fromJson(response.getResponse(), SymbolBean.class);
            setCurrentcy(symbolBean);

            if (MyApplication.getApp().isLogin()){
                if (dqccFragment!=null && dqwtFragment!=null){
                    dqccFragment.refresh();
                    dqwtFragment.refresh();
                }

            }

        }else if (cmd == NEWCMD.SUBSCRIBE_SIDE_TRADE){
            try {
                List<NewCurrency> newDatas = gson.fromJson(response.getResponse(), new TypeToken<List<NewCurrency>>() {}.getType());
                if (newDatas == null) return;
                if (mCurrencyListAll.size() == 0){
                    mCurrencyListAll.addAll(newDatas);
                }else{
                    for (NewCurrency localdata : mCurrencyListAll) {
                        for (NewCurrency newdata : newDatas){
                            if (localdata.equals(newdata)) {
                                NewCurrency.shallowClone(localdata, newdata);
                                break;
                            }
                        }
                    }
                }

                if (mSymbolDialog!= null && mSymbolDialog.isHasShow()){
                    mSymbolDialog.dataLoaded(mCurrencyListAll);
                }

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }


    private void setCurrentcy(SymbolBean symbolBean) {
        if (mSymbolConfig != null){
            adjustScale(tvPrice,symbolBean.getClose());
        }else{
            tvPrice.setText(String.valueOf(symbolBean.getClose()));
        }

        tvPrice.setTextColor( symbolBean.getScale() >= 0 ? ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_green) :
                ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_red));
        tvMoney.setText(GlobalConstant.CNY + symbolBean.getConvert());

    }

    private void setResponse(SocketResponse response) {
        String res = response.getResponse();
        if (StringUtils.isEmpty(res)) return; // 如果返回为空不处理
        SymbolStep items = gson.fromJson(res, SymbolStep.class);
        if (items == null) return;
        if (!response.getRemark().equals(this.currency.getSymbol())) { // 这里加了层判断，如果推送来的币不是当前选择的币则不处理
            return;
        }

        Double max = 0.0d;
        this.sellExchangeList.clear();
        for (int i = 0; i < 5; i++) {
            sellExchangeList.add(new Exchange(5 - i, "--", "--"));
        }

        List<List<Double>> sells = items.getAsks();
        if (sells.size() >= 5) {
            for (int i = 0; i < 5; i++) {
                Double amount = sells.get(i).get(1);
                if (amount > max){
                    max = amount;
                }
                sellExchangeList.set(4 - i, new Exchange(i + 1, sells.get(i).get(0), amount));
            }
        } else {
            for (int i = 0; i < sells.size(); i++) {
                Double amount = sells.get(i).get(1);
                if (amount > max){
                    max = amount;
                }
                sellExchangeList.set(4 - i, new Exchange(i + 1, sells.get(i).get(0), amount));
            }
        }



        this.buyExchangeList.clear();
        for (int i = 0; i < 5; i++) {
            buyExchangeList.add(new Exchange(5 - i, "--", "--"));
        }
        List<List<Double>> buys = items.getBids();
        if (buys.size() >= 5) {
            for (int i = 0; i < 5; i++) {
                Double amount = buys.get(i).get(1);
                if (amount > max){
                    max = amount;
                }
                buyExchangeList.set(i, new Exchange(i, buys.get(i).get(0), amount));
            }
        } else {
            for (int i = 0; i < buys.size(); i++) {
                Double amount = buys.get(i).get(1);
                if (amount > max){
                    max = amount;
                }
                buyExchangeList.set(i, new Exchange(i, buys.get(i).get(0), amount));
            }
        }

        if (sellAdapter!=null && buyAdapter!=null){
            sellAdapter.setTag(1/max);
            sellAdapter.notifyDataSetChanged();

            buyAdapter.setTag(1/max);
            buyAdapter.notifyDataSetChanged();
        }

    }
}
