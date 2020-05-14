package com.spark.szhb_master.activity.kline;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.fujianlian.klinechart.DataHelper;
import com.github.fujianlian.klinechart.KLineChartAdapter;
import com.github.fujianlian.klinechart.KLineChartView;
import com.github.fujianlian.klinechart.KLineEntity;
import com.github.fujianlian.klinechart.draw.Status;
import com.github.fujianlian.klinechart.formatter.DateFormatter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.Trade.NewTradeActivity;
import com.spark.szhb_master.activity.Trade.TradeActivity;
import com.spark.szhb_master.activity.main.MainActivity;
import com.spark.szhb_master.activity.mychart.DataParse;
import com.spark.szhb_master.activity.mychart.KLineBean;
import com.spark.szhb_master.adapter.MyPagerAdapter;
import com.spark.szhb_master.adapter.PagerAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.ChartBean;
import com.spark.szhb_master.entity.Currency;
import com.spark.szhb_master.entity.Favorite;
import com.spark.szhb_master.entity.NewCurrency;
import com.spark.szhb_master.entity.SymbolBean;
import com.spark.szhb_master.entity.TcpEntity;
import com.spark.szhb_master.factory.socket.NEWCMD;
import com.spark.szhb_master.serivce.SocketResponse;
import com.spark.szhb_master.ui.CustomViewPager;
import com.spark.szhb_master.ui.MyViewPager;
import com.spark.szhb_master.ui.intercept.MyScrollView;
import com.spark.szhb_master.utils.DpPxUtils;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.ToastUtils;
import com.spark.szhb_master.widget.SymbolDropDownDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

public class KlineActivity extends BaseActivity implements KlineContract.View, View.OnClickListener {

    @BindView(R.id.fldiap)
    FrameLayout fldiap;


    @BindView(R.id.kDataText)
    TextView mDataText;
    @BindView(R.id.kDataOne)
    TextView mDataOne;
    @BindView(R.id.kCount)
    TextView kCount;
    @BindView(R.id.kUp)
    TextView kUp;
    @BindView(R.id.kLow)
    TextView kLow;
//    @BindView(R.id.kLandDataText)
//    TextView kLandDataText;
//    @BindView(R.id.kLandDataOne)
//    TextView kLandDataOne;
//    @BindView(R.id.kLandCount)
//    TextView kLandCount;
//    @BindView(R.id.kLandUp)
//    TextView kLandUp;
//    @BindView(R.id.kLandLow)
//    `TextView kLandLow;`

    @BindView(R.id.scrollView)
    MyScrollView scrollView;

    @BindView(R.id.tvFitenMis)
    TextView tvFitenMis;

    @BindView(R.id.tvOneHours)
    TextView tvOneHours;

    @BindView(R.id.rltitle)
    RelativeLayout rlTitle;

//    @BindView(R.id.kLandRange)
//    TextView kLandRange;
    @BindView(R.id.kRange)
    TextView kRange;
    @BindView(R.id.viewPager)
    MyViewPager viewPager;
//    @BindView(R.id.tv_collect)
//    TextView mTvCollect; // 收藏的意思
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.rlMore)
    RelativeLayout rlMore;
    @BindView(R.id.rlIndex)
    RelativeLayout rlIndex;
    @BindView(R.id.llAllTab)
    LinearLayout llAllTab;
    @BindView(R.id.llVertical)
    LinearLayout llVertical;
    @BindView(R.id.llState)
    LinearLayout llState;
    @BindView(R.id.tvBuy)
    TextView tvBuy;
    @BindView(R.id.tvSell)
    TextView tvSell;
    @BindView(R.id.vpDepth)
    CustomViewPager depthPager;
    @BindView(R.id.llDepthTab)
    TabLayout depthTab;


    @BindView(R.id.llSymbolTitle)
    LinearLayout mLlSymbolTitle;

    @BindView(R.id.tvTitleSymbol)
    TextView mTvTitleSymbol;

    @BindView(R.id.flroot)
    FrameLayout flroot;

    @BindView(R.id.tvTitleSymbolType)
    TextView mTvTitleSymbolType;

    private KLineChartView kChartView;

    private ArrayList<TextView> textViews;
    private ArrayList<View> klineViews;
    private TextView selectedTextView;
    private KLineChartAdapter kChartAdapter;
    private int type = 3;
    private String symbol = "";
    private String resolution;
    private KlineContract.Presenter presenter;
    private ArrayList<KLineBean> kLineDatas;     // K线图数据
    private SymbolBean mCurrency;
    private boolean isStart = false;
    private Date startDate;
    private Date endDate;
    private ProgressBar mProgressBar;
    private boolean isFace = false;
    private boolean isPopClick;
    private TextView mTvPopFen,mTvPop1Fen,mTvPop5Fen,mTvPop30Fen,mTvPop4Hour,mTvPopDay,mTvPopMonth;
    private TextView maView;
    private TextView bollView;
    private TextView macdView;
    private TextView kdjView;
    private TextView rsiView;
    private TextView wrView;
    private TextView hideChildView;
    private TextView hideMainView;
    private int childType = -1;
    private boolean isVertical;
    private boolean isFirstLoad = true;
    private List<Fragment> fragments = new ArrayList<>();
    private DepthFragment mDepthFragment;
    private VolumeFragment mVolumeFragment;
    private PagerAdapter adapter;
    private List<String> tabs;

//    private String[] titleArray;
    private boolean isInit = true;
    private NewCurrency currency;
    private String symbolType;
    private DataParse kData = new DataParse();

    private String typeLists[] = {"1min","1min", "5min", "15min", "30min", "1hour","4hour","1day", "1month"};
    private int tcpstatus = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isVertical) {
                finish();
            } else {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 切换横竖屏
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewPager.getLayoutParams();
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) { // 横屏
            changeSystemBar(true);
            isVertical = false;
            llState.setVisibility(View.GONE);
            rlTitle.setVisibility(View.GONE);

            llVertical.setVisibility(View.GONE);
            ivBack.setVisibility(View.GONE);
            depthTab.setVisibility(View.GONE);
            depthPager.setVisibility(View.GONE);
            params.height = LinearLayout.LayoutParams.MATCH_PARENT;
            viewPager.setLayoutParams(params);

            if (type == GlobalConstant.TAG_30_MINUTE) {
                isPopClick = false;
            }
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            changeSystemBar(false);
            isVertical = true;
            llState.setVisibility(View.VISIBLE);
            rlTitle.setVisibility(View.VISIBLE);

            llVertical.setVisibility(View.VISIBLE);
            ivBack.setVisibility(View.VISIBLE);
            depthTab.setVisibility(View.VISIBLE);
            depthPager.setVisibility(View.VISIBLE);
            params.height = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 340, getResources().getDisplayMetrics()));
            viewPager.setLayoutParams(params);

            if (type == GlobalConstant.TAG_30_MINUTE) {
                isPopClick = true;
            }
        }
        for (int i = 0; i < klineViews.size(); i++) {
            View view = klineViews.get(i);
            KLineChartView kChartView = view.findViewById(R.id.kLineChartView);
            if (i != 0) {
                if (isVertical) {
                    kChartView.setGridRows(4);
                    kChartView.setGridColumns(4);
                } else {
                    kChartView.setGridRows(3);
                    kChartView.setGridColumns(8);
                }
            }
        }
        setPagerView();
    }

    private void changeSystemBar(boolean isShow) {
        if (isShow) { //全屏
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        } else { //非全屏
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_kline;
    }

    private SymbolDropDownDialog mSymbolDialog;

    @Override
    protected void initView() {
        super.initView();
//        setSetTitleAndBack(false, true);
        setImmersionBar(rlTitle);

        if (mSymbolDialog == null){
            mSymbolDialog = new SymbolDropDownDialog(this,fldiap,mTvTitleSymbolType);
            mSymbolDialog.setOnItemClickCallback(new SymbolDropDownDialog.OnItemClickCallback() {
                @Override
                public void onitemClick(NewCurrency currency) {
                    mSymbolDialog.dismiss();
                    reInit(currency);
                }
            });
        }
    }

    private void reInit(NewCurrency currency) {

        String st = "market." + symbol + "_" + symbolType + ".klist." + typeLists[type];
        MyApplication.getApp().stopTcp(new TcpEntity(st,NEWCMD.SUBSCRIBE_SYMBOL_KLIST));

        String sthead = "market." + symbol + "_" + symbolType + ".detail";
        MyApplication.getApp().stopTcp(new TcpEntity(sthead,NEWCMD.SUBSCRIBE_SYMBOL_DETAIL));


        kChartAdapter.clearData();
        symbol = currency.getSymbol();
        this.currency = currency;
        symbolType = currency.getType();
        mTvTitleSymbol.setText(symbol);
        mTvTitleSymbolType.setText(symbolType);

        tvBuy.setText(getString(R.string.text_buy_in) + symbol);
        tvSell.setText(getString(R.string.text_sale_out) + symbol);

        mDepthFragment.reInit(symbol,symbolType);
        mVolumeFragment.reInit(symbol,symbolType);


        startDetailTCP();
        startKlistTcp(type);
    }

    @Override
    protected void initData() {
        super.initData();
        isVertical = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
        new KlinePresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        textViews = new ArrayList<>();
        klineViews = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            symbol = bundle.getString("symbol");
            currency = (NewCurrency) bundle.getSerializable("currency");
            tcpstatus = bundle.getInt("tcpstatus",0);
            symbolType = currency.getType();
            mTvTitleSymbol.setText(symbol);
            mTvTitleSymbolType.setText(symbolType);
            isFace = addFace(symbol);
            if (symbol != null) {
                tvBuy.setText(String.valueOf(getString(R.string.text_buy_in) + symbol));
                tvSell.setText(String.valueOf(getString(R.string.text_sale_out) + symbol));
            }
        }
        startDetailTCP();
        startKlistTcp(type);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isInit) {
            isInit = false;
            List<String> titles = Arrays.asList(typeLists);
            if (titles != null) {
                initPopWindow();
                initViewpager(titles);
                initDepthData();
            }
            selectedTextView = tvFitenMis;
            type = (int) selectedTextView.getTag();
            selectedTextView.setSelected(true);
        }
    }


    @OnClick({R.id.ivBack,R.id.llSymbolTitle, R.id.rlFullScreen,
            R.id.tvSell, R.id.tvBuy, R.id.rlMore, R.id.rlIndex})
    @Override
    protected void setOnClickListener(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                return;
            case R.id.llSymbolTitle:
                if (mSymbolDialog != null && !mSymbolDialog.isHasShow()){
                    mSymbolDialog.show();
                }else if (mSymbolDialog != null && mSymbolDialog.isHasShow()){
                    mSymbolDialog.dismiss();
                }
                return;
            case R.id.rlFullScreen:
                if (isVertical) {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                }
                return;
            case R.id.tvSell:
            case R.id.tvBuy:
                Bundle bundle = new Bundle();
                bundle.putString("symbol", symbol);
                bundle.putString("comefrom", "comefrom");
                bundle.putSerializable("currency", currency);
                if (view.getId() == R.id.tvBuy) {
                    bundle.putInt("type", 1);
                } else {
                    bundle.putInt("type", 2);
                }
                Intent intent = new Intent(this, TradeActivity.class);
                intent.putExtras(bundle);
//                setResult(RESULT_OK, intent);
                showActivity(NewTradeActivity.class,bundle,0);
                return;
//            case R.id.tv_collect:
//                MainActivity.isAgain = true;
//                deleteOrCollect();
//                return;
            case R.id.rlMore:
//                rlMore.setBackground(getDrawable(R.drawable.shape_bg_kline_tab_hover));
//                rlIndex.setBackground(getDrawable(R.drawable.shape_bg_kline_tab_normal));
                moreTabLayout.setVisibility(View.VISIBLE);
                indexLayout.setVisibility(View.GONE);
                break;
            case R.id.rlIndex:
//                rlMore.setBackground(getDrawable(R.drawable.shape_bg_kline_tab_normal));
//                rlIndex.setBackground(getDrawable(R.drawable.shape_bg_kline_tab_hover));
                moreTabLayout.setVisibility(View.GONE);
                indexLayout.setVisibility(View.VISIBLE);
                break;
        }
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            popupWindow.showAsDropDown(llAllTab);
        }
    }

    /**
     * 添加收藏或取消收藏
     */
    private void deleteOrCollect() {
        if (!MyApplication.getApp().isLogin()) {
            ToastUtils.showToast(getString(R.string.text_login_first));
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("symbol", symbol);
        if (isFace) { // 删除
            presenter.doDelete(map);
        } else {
            presenter.doCollect(map);
        }

    }

    private PopupWindow popupWindow;
    private LinearLayout moreTabLayout;
    private LinearLayout indexLayout;

    /**
     * 初始化popwindow
     *
     * @param count
     */
    private void initPopWindow() {
        View contentView = LayoutInflater.from(activity).inflate(R.layout.layout_kline_popwindow, null);
        initPopChidView(contentView);

        popupWindow = new PopupWindow(activity);
        popupWindow.setContentView(contentView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
    }


    /**
     * 初始化popwindow里的控件
     *
     * @param contentView
     */
    private void initPopChidView(View contentView) {
        moreTabLayout = contentView.findViewById(R.id.tabPop);
        indexLayout = contentView.findViewById(R.id.llIndex);

        mTvPopFen = contentView.findViewById(R.id.tvPopFen);
        mTvPopFen.setTag(0);
        mTvPopFen.setSelected(true);
        mTvPopFen.setOnClickListener(this);
        textViews.add(mTvPopFen);

        mTvPop1Fen = contentView.findViewById(R.id.tvPop1Fen);
        mTvPop1Fen.setSelected(true);
        mTvPop1Fen.setTag(1);
        mTvPop1Fen.setOnClickListener(this);
        textViews.add(mTvPop1Fen);

        mTvPop5Fen = contentView.findViewById(R.id.tvPop5Fen);
        mTvPop5Fen.setSelected(true);
        mTvPop5Fen.setTag(2);
        mTvPop5Fen.setOnClickListener(this);
        textViews.add(mTvPop5Fen);

        tvFitenMis.setTag(3);
        tvFitenMis.setOnClickListener(this);
        textViews.add(tvFitenMis);


        mTvPop30Fen = contentView.findViewById(R.id.tvPop30Fen);
        mTvPop30Fen.setSelected(true);
        mTvPop30Fen.setTag(4);
        mTvPop30Fen.setOnClickListener(this);
        textViews.add(mTvPop30Fen);

        tvOneHours.setTag(5);
        tvOneHours.setOnClickListener(this);
        textViews.add(tvOneHours);

        mTvPop4Hour = contentView.findViewById(R.id.tvPop4Hour);
        mTvPop4Hour.setSelected(true);
        mTvPop4Hour.setTag(6);
        mTvPop4Hour.setOnClickListener(this);
        textViews.add(mTvPop4Hour);

        mTvPopDay = contentView.findViewById(R.id.tvPopDay);
        mTvPopDay.setSelected(true);
        mTvPopDay.setTag(7);
        mTvPopDay.setOnClickListener(this);
        textViews.add(mTvPopDay);

        mTvPopMonth = contentView.findViewById(R.id.tvPopMonth);
        mTvPopMonth.setSelected(true);
        mTvPopMonth.setTag(8);
        mTvPopMonth.setOnClickListener(this);
        textViews.add(mTvPopMonth);

        maView = contentView.findViewById(R.id.tvMA);
        maView.setSelected(true);
        maView.setOnClickListener(this);
        bollView = contentView.findViewById(R.id.tvBOLL);
        bollView.setOnClickListener(this);
        macdView = contentView.findViewById(R.id.tvMACD);
        kdjView = contentView.findViewById(R.id.tvKDJ);
        rsiView = contentView.findViewById(R.id.tvRSI);
        wrView = contentView.findViewById(R.id.tvWR);
        wrView.setOnClickListener(this);
        hideMainView = contentView.findViewById(R.id.tvMainHide);
        hideMainView.setOnClickListener(this);
        macdView = contentView.findViewById(R.id.tvMACD);
        macdView.setOnClickListener(this);
        kdjView = contentView.findViewById(R.id.tvKDJ);
        kdjView.setOnClickListener(this);
        rsiView = contentView.findViewById(R.id.tvRSI);
        rsiView.setOnClickListener(this);
        hideChildView = contentView.findViewById(R.id.tvChildHide);
        hideChildView.setSelected(true);
        hideChildView.setOnClickListener(this);
    }

    /**
     * socket 推送过来的信息
     */
    private Gson gson = new Gson();
    private List<NewCurrency> mCurrencyListAll = new ArrayList<>();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSocketMessage(SocketResponse response) {
        NEWCMD cmd = response.getCmd();
        if (cmd == NEWCMD.SUBSCRIBE_SYMBOL_KLIST) {    // 如果是盘口返回的信息
            try {
                List<ChartBean> currencyList = gson.fromJson(response.getResponse(), new TypeToken<List<ChartBean>>() {}.getType());
                try {
                    if (currencyList != null && currencyList.size() > 0) {
                        ArrayList<KLineEntity> kLineEntities = new ArrayList<>();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd HH:mm");
                        for (int i = 0; i < currencyList.size(); i++) {
                            KLineEntity lineEntity = new KLineEntity();
                            ChartBean chartBean = currencyList.get(i);
                            lineEntity.setDate(simpleDateFormat.format(new Date(new Long(chartBean.getId()) * 1000) ));
                            lineEntity.setOpen((float)chartBean.getOpen());
                            lineEntity.setClose((float)chartBean.getClose());
                            lineEntity.setHigh((float)chartBean.getHigh());
                            lineEntity.setLow((float)chartBean.getLow());
                            lineEntity.setVolume(chartBean.getVol());
                            kLineEntities.add(lineEntity);
                        }
                        DataHelper.calculate(kLineEntities);
                        kChartAdapter.addFooterData(kLineEntities);
                        kChartAdapter.notifyDataSetChanged();
//                        kChartView.startAnimation();
                        kChartView.refreshComplete();
                    }
                } catch (Exception e) {
                    ToastUtils.showToast(getString(R.string.parse_error));
                }

            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.showToast("解析出错");
            }
        }else if (cmd == NEWCMD.SUBSCRIBE_SYMBOL_DETAIL){
            SymbolBean symbolBean = gson.fromJson(response.getResponse(), SymbolBean.class);
            setCurrentcy(symbolBean);
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


    /**
     * 头部显示内容
     *
     * @param
     */
    private void setCurrentcy(SymbolBean symbolBean) {
        try {
            mCurrency = symbolBean;
            if (mCurrency != null) {
                String strUp = String.valueOf(mCurrency.getHigh());
                String strLow = String.valueOf(mCurrency.getLow());
                String strCount = String.valueOf(mCurrency.getVol());
                String strDataOne = String.valueOf(mCurrency.getClose());
                kUp.setText(strUp);
                kLow.setText(strLow);
                kCount.setText(strCount);
                mDataOne.setText(strDataOne);
                mDataText.setText("≈ " + mCurrency.getConvert() + " USDT");
                mDataOne.setTextColor(mCurrency.getScale() < 0 ? getResources().getColor(R.color.main_font_red) : getResources().getColor(R.color.main_font_green));
                kRange.setBackground(mCurrency.getScale() < 0 ? getResources().getDrawable(R.drawable.bg_kl_corner_red) : getResources().getDrawable(R.drawable.bg_kl_corner_green));
                kRange.setText((mCurrency.getScale()  < 0 ? "-" : "+") + mCurrency.getScale() +"%");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean addFace(String symbol) {
        for (Favorite favorite : MainActivity.mFavorte) {
            if (symbol.equals(favorite.getSymbol())) return true;
        }
        return false;
    }

    private void startDetailTCP() {
        String detail = "market." + symbol + "_" + symbolType + ".detail";
        MyApplication.getApp().startTcp(new TcpEntity(detail,NEWCMD.SUBSCRIBE_SYMBOL_DETAIL));
    }

    private void startKlistTcp(int type){
        String klist = "market." + symbol + "_" + symbolType + ".klist." + typeLists[type];
        MyApplication.getApp().startTcp(new TcpEntity(klist,NEWCMD.SUBSCRIBE_SYMBOL_KLIST));
    }

    private void stopKlistTcp(int type){
        String klist = "market." + symbol + "_" + symbolType + ".klist." + typeLists[type];
        MyApplication.getApp().stopTcp(new TcpEntity(klist,NEWCMD.SUBSCRIBE_SYMBOL_KLIST));
    }

    /**
     * 初始化viewpager
     *
     * @param titles
     */
    private void initViewpager(List<String> titles) {
        for (int i = 0; i < titles.size(); i++) {
            View view = LayoutInflater.from(activity).inflate(R.layout.layout_kchartview, null);
                KLineChartView kChartView = view.findViewById(R.id.kLineChartView);
                initKchartView(kChartView);
                kChartView.setVisibility(View.VISIBLE);
                kChartView.setAdapter(new KLineChartAdapter());
            klineViews.add(view);
        }
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(klineViews);
        viewPager.setAdapter(myPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setPagerView();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(type);
        setPagerView();
    }

    /**
     * 设置kchartview
     *
     * @param kChartView
     */
    private void initKchartView(final KLineChartView kChartView) {
        kChartView.setCandleSolid(true);
        kChartView.setGridRows(4);
        kChartView.setGridColumns(4);
        kChartView.setDateTimeFormatter(new DateFormatter());
        kChartView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL){
                    scrollView.requestDisallowInterceptTouchEvent(false);
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    scrollView.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
    }

    @BindView(R.id.tvMore)
    TextView tvMore;

    @BindView(R.id.tvIndex)
    TextView tvIndex;

    /**
     * viewpager和textview的点击事件
     */
    private void setPagerView() {
        for (int j = 0; j < textViews.size(); j++) {
            textViews.get(j).setSelected(false);
            int tag = (int) textViews.get(j).getTag();
            if (tag == type) {
                if (isPopClick) {
                    tvMore.setText(selectedTextView.getText());
                    tvMore.setSelected(true);
                    textViews.get(j).setSelected(true);
//                    tvMore.setBackground(getDrawable(R.drawable.shape_bg_kline_tab_normal));
                } else {
                    tvMore.setText(getString(R.string.more));
                    tvMore.setSelected(false);
//                    tvMore.setBackground(getDrawable(R.drawable.shape_bg_kline_tab_normal));
//                    tvIndex.setBackground(getDrawable(R.drawable.shape_bg_kline_tab_normal));
                    textViews.get(j).setSelected(true);
                }
                View view = klineViews.get(j);
                kChartView = view.findViewById(R.id.kLineChartView);
                if ( maView.isSelected()){
                    kChartView.changeMainDrawType(Status.MA);
                }else if (bollView.isSelected()){
                    kChartView.changeMainDrawType(Status.BOLL);
                }
                kChartView.setChildDraw(childType);
                kChartAdapter = (KLineChartAdapter) kChartView.getAdapter();
            } else if (!isPopClick) {
                tvMore.setSelected(false);
            }
        }
    }


    @Override
    public void setPresenter(KlineContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void KDataSuccess(JSONArray obj) {
    }

    @Override
    public void allCurrencySuccess(List<Currency> obj) {
    }

    @Override
    public void doDeleteOrCollectSuccess(String msg) {
    }

    @Override
    public void dpPostFail(Integer code, String toastMessage) {

    }

    /**
     * 副图的点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvMA:
            case R.id.tvBOLL:
            case R.id.tvMainHide:
                if (view.getId() == R.id.tvMA) {
                    maView.setSelected(true);
                    bollView.setSelected(false);
                    hideMainView.setSelected(false);
                    kChartView.changeMainDrawType(Status.MA);
                } else if (view.getId() == R.id.tvBOLL) {
                    maView.setSelected(false);
                    bollView.setSelected(true);
                    hideMainView.setSelected(false);
                    kChartView.changeMainDrawType(Status.BOLL);
                } else {
                    maView.setSelected(false);
                    bollView.setSelected(false);
                    hideMainView.setSelected(true);
                    kChartView.changeMainDrawType(Status.NONE);
                }
                popupWindow.dismiss();
                break;
            case R.id.tvFitenMis:
            case R.id.tvOneHours:
                isPopClick = false;
                selectedTextView = (TextView) view;
                stopKlistTcp(type);
                type = (int) selectedTextView.getTag();
                startKlistTcp(type);
                viewPager.setCurrentItem(type);
                kChartView.setMainDrawLine(false);
                break;
            case R.id.tvPopFen:
                isPopClick = true;
                selectedTextView = (TextView) view;
                stopKlistTcp(type);
                type = (int) selectedTextView.getTag();
                startKlistTcp(type);
                viewPager.setCurrentItem(type);
                kChartView.setMainDrawLine(true);
                popupWindow.dismiss();
                break;

            case R.id.tvPop1Fen:
            case R.id.tvPop5Fen:
            case R.id.tvPop30Fen:
            case R.id.tvPop4Hour:
            case R.id.tvPopDay:
            case R.id.tvPopMonth:
                isPopClick = true;
                selectedTextView = (TextView) view;
                stopKlistTcp(type);
                type = (int) selectedTextView.getTag();
                startKlistTcp(type);
                viewPager.setCurrentItem(type);
                kChartView.setMainDrawLine(false);
                kChartView.justShowLoading();
                popupWindow.dismiss();
                break;

            case R.id.tvMACD:
            case R.id.tvRSI:
            case R.id.tvKDJ:
            case R.id.tvWR:
            case R.id.tvChildHide:
                if (view.getId() == R.id.tvMACD) {
                    if (childType != 0){
                        kChartView.hideSelectData();
                        childType = 0;
                        macdView.setSelected(true);
                        rsiView.setSelected(false);
                        kdjView.setSelected(false);
                        wrView.setSelected(false);
                        hideChildView.setSelected(false);
                        kChartView.setChildDraw(childType);
                    }

                } else if (view.getId() == R.id.tvKDJ) {
                    if (childType != 1){
                        kChartView.hideSelectData();
                        childType = 1;
                        macdView.setSelected(false);
                        rsiView.setSelected(false);
                        kdjView.setSelected(true);
                        wrView.setSelected(false);
                        hideChildView.setSelected(false);
                        kChartView.setChildDraw(childType);
                    }

                } else if (view.getId() == R.id.tvRSI) {
                    if (childType != 2){
                        kChartView.hideSelectData();
                        childType = 2;
                        macdView.setSelected(false);
                        rsiView.setSelected(true);
                        kdjView.setSelected(false);
                        wrView.setSelected(false);
                        hideChildView.setSelected(false);
                        kChartView.setChildDraw(childType);
                    }

                } else if (view.getId() == R.id.tvWR) {
                    if (childType != 3){
                        kChartView.hideSelectData();
                        childType = 3;
                        macdView.setSelected(false);
                        rsiView.setSelected(false);
                        kdjView.setSelected(false);
                        wrView.setSelected(true);
                        hideChildView.setSelected(false);
                        kChartView.setChildDraw(childType);
                    }

                } else {
                    childType = -1;
                    macdView.setSelected(false);
                    rsiView.setSelected(false);
                    kdjView.setSelected(false);
                    wrView.setSelected(false);
                    hideChildView.setSelected(true);
                    kChartView.hideChildDraw();
                }


                popupWindow.dismiss();
                break;
        }
    }

    /**
     * 初始化深度图数据
     */
    private void initDepthData() {
        mDepthFragment = DepthFragment.getInstance(symbol,currency.getType(),tcpstatus);
        mVolumeFragment = VolumeFragment.getInstance(symbol,currency.getType(),tcpstatus);
        fragments.add(mDepthFragment);
        fragments.add(mVolumeFragment);
        String[] tabArray = getResources().getStringArray(R.array.k_line_depth);
        tabs = new ArrayList<>();
        for (int i = 0; i < tabArray.length; i++) {
            tabs.add(tabArray[i]);
        }
        depthPager.setAdapter(adapter = new PagerAdapter(getSupportFragmentManager(), fragments, tabs));
        depthTab.setTabMode(TabLayout.MODE_FIXED);
        depthTab.setupWithViewPager(depthPager);
        depthPager.setOffscreenPageLimit(fragments.size() - 1);
        depthPager.setCurrentItem(0);
    }
}
