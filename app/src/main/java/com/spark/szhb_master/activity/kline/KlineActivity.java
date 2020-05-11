package com.spark.szhb_master.activity.kline;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.fujianlian.klinechart.DataHelper;
import com.github.fujianlian.klinechart.KLineChartAdapter;
import com.github.fujianlian.klinechart.KLineChartView;

import com.github.fujianlian.klinechart.KLineEntity;
import com.github.fujianlian.klinechart.draw.Status;
import com.github.fujianlian.klinechart.formatter.DateFormatter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.Trade.NewTradeActivity;
import com.spark.szhb_master.activity.Trade.TradeActivity;
import com.spark.szhb_master.activity.main.MainActivity;
import com.spark.szhb_master.activity.mychart.DataParse;
import com.spark.szhb_master.activity.mychart.KLineBean;
import com.spark.szhb_master.activity.mychart.MinutesBean;
import com.spark.szhb_master.adapter.MyPagerAdapter;
import com.spark.szhb_master.adapter.PagerAdapter;
import com.spark.szhb_master.base.BaseFragment;
import com.spark.szhb_master.entity.ChartBean;
import com.spark.szhb_master.entity.Exchange;
import com.spark.szhb_master.entity.NewCurrency;
import com.spark.szhb_master.entity.SymbolBean;
import com.spark.szhb_master.entity.SymbolStep;
import com.spark.szhb_master.entity.TcpEntity;
import com.spark.szhb_master.factory.socket.NEWCMD;
import com.spark.szhb_master.ui.CustomViewPager;
import com.spark.szhb_master.ui.intercept.MyScrollView;
import com.spark.szhb_master.utils.DpPxUtils;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.Currency;
import com.spark.szhb_master.entity.Favorite;
import com.spark.szhb_master.factory.socket.ISocket;
import com.spark.szhb_master.serivce.SocketMessage;
import com.spark.szhb_master.serivce.SocketResponse;
import com.spark.szhb_master.ui.MyViewPager;

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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

import static android.widget.RelativeLayout.CENTER_IN_PARENT;

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
//    TextView kLandLow;

    @BindView(R.id.scrollView)
    MyScrollView scrollView;

    @BindView(R.id.tab)
    LinearLayout tab;

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
    @BindView(R.id.tvMore)
    TextView tvMore;
    @BindView(R.id.tvIndex)
    TextView tvIndex;
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
//    private MinuteChartView minuteChartView;
    private ArrayList<TextView> textViews;
    private ArrayList<View> views;
    private TextView selectedTextView;
    private KLineChartAdapter kChartAdapter;
    private int type;
    private String symbol = "";
    private String resolution;
    private KlineContract.Presenter presenter;
    private ArrayList<KLineBean> kLineDatas;     // K线图数据
    private SymbolBean mCurrency;
    private List<Currency> currencies = new ArrayList<>();
    private boolean isStart = false;
    private Date startDate;
    private Date endDate;
    private ProgressBar mProgressBar;
    private boolean isFace = false;
    private boolean isPopClick;
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
    private int currentCount;
//    private String[] titleArray;
    private boolean isInit = true;
    private NewCurrency currency;
    private String symbolType;
    private DataParse kData = new DataParse();

    private String typeNameLists[] = {"1分钟", "5分钟", "15分", "30分", "1小时","4小时","1天", "1月"};
    private String typeLists[] = {"1min", "5min", "15min", "30min", "1hour","4hour","1day", "1month"};
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
        tab.removeAllViews();
        moreTabLayout.removeAllViews();
        textViews = new ArrayList<>();
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) { // 横屏
            isVertical = false;
            llState.setVisibility(View.GONE);
//            llLandText.setVisibility(View.VISIBLE);
            llVertical.setVisibility(View.GONE);
            ivBack.setVisibility(View.GONE);
            depthTab.setVisibility(View.GONE);
            depthPager.setVisibility(View.GONE);
            params.height = LinearLayout.LayoutParams.MATCH_PARENT;
            viewPager.setLayoutParams(params);
            currentCount = 6;
            initTextView(currentCount);
            intMoreTab(currentCount);
            if (type == GlobalConstant.TAG_30_MINUTE) {
                isPopClick = false;
            }
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            isVertical = true;
            llState.setVisibility(View.VISIBLE);
//            llLandText.setVisibility(View.INVISIBLE);
            llVertical.setVisibility(View.VISIBLE);
            ivBack.setVisibility(View.VISIBLE);
            depthTab.setVisibility(View.VISIBLE);
            depthPager.setVisibility(View.VISIBLE);
            params.height = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 340, getResources().getDisplayMetrics()));
            viewPager.setLayoutParams(params);
            currentCount = 5;
            initTextView(currentCount);
            intMoreTab(currentCount);
            if (type == GlobalConstant.TAG_30_MINUTE) {
                isPopClick = true;
            }
        }
        for (int i = 0; i < views.size(); i++) {
            View view = views.get(i);
            KLineChartView kChartView = view.findViewById(R.id.kLineChartView);
//            MinuteChartView minuteChartView = view.findViewById(R.id.minuteChartView);
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

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();

//        String st = "market." + symbol + "_" + symbolType + ".klist." + typeLists[type];
//        EventBus.getDefault().post(new SocketMessage(0, NEWCMD.SUBSCRIBE_SYMBOL_KLIST,
//                buildGetBodyJson(st, "0").toString())); //
//
//        String sthead = "market." + symbol + "_" + symbolType + ".detail";
//        EventBus.getDefault().post(new SocketMessage(0, NEWCMD.SUBSCRIBE_SYMBOL_DETAIL,
//                buildGetBodyJson(sthead, "0").toString()));


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
//        EventBus.getDefault().post(new SocketMessage(0, NEWCMD.SUBSCRIBE_SYMBOL_KLIST,
//                buildGetBodyJson(st, "0").toString())); //
        MyApplication.getApp().stopTcp(new TcpEntity(st,NEWCMD.SUBSCRIBE_SYMBOL_KLIST));

        String sthead = "market." + symbol + "_" + symbolType + ".detail";
//        EventBus.getDefault().post(new SocketMessage(0, NEWCMD.SUBSCRIBE_SYMBOL_DETAIL,
//                buildGetBodyJson(sthead, "0").toString()));
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
//        titleArray = activity.getResources().getStringArray(R.array.k_line_tab);
        isVertical = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
        new KlinePresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        textViews = new ArrayList<>();
        views = new ArrayList<>();
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

//    private LineChartManager lineChartManager2;
//    private LineChartManager lineChartManager;
//
//    private void initchart() {
//        lineChartManager2 = new LineChartManager(mLineChart);
//        lineChartManager = new LineChartManager(lineChartbuy);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isInit) {
            isInit = false;
            List<String> titles = Arrays.asList(typeLists);
            if (titles != null) {
                currentCount = 5;
                initTextView(currentCount);
                initPopWindow(currentCount);
                initViewpager(titles);
                initDepthData();
            }
            selectedTextView = textViews.get(1);
            type = (int) selectedTextView.getTag();
        }
    }

    @Override
    protected void loadData() {
        super.loadData();
//        presenter.allCurrency();
    }


    @OnClick({R.id.ivBack,R.id.llSymbolTitle, R.id.ivFullScreen, R.id.tvSell, R.id.tvBuy, R.id.tvMore, R.id.tvIndex})
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
                break;
            case R.id.ivFullScreen:
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
            case R.id.tvMore:
                tvMore.setBackground(getDrawable(R.drawable.shape_bg_kline_tab_hover));
                tvIndex.setBackground(getDrawable(R.drawable.shape_bg_kline_tab_normal));
                moreTabLayout.setVisibility(View.VISIBLE);
                indexLayout.setVisibility(View.GONE);
                break;
            case R.id.tvIndex:
                tvMore.setBackground(getDrawable(R.drawable.shape_bg_kline_tab_normal));
                tvIndex.setBackground(getDrawable(R.drawable.shape_bg_kline_tab_hover));
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
    private void initPopWindow(int count) {
        View contentView = LayoutInflater.from(activity).inflate(R.layout.layout_kline_popwindow, null);
        initPopChidView(contentView);
        intMoreTab(count);
        popupWindow = new PopupWindow(activity);
        popupWindow.setContentView(contentView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
    }

    /**
     * 设置more显示内容
     *
     * @param count
     */
    private void intMoreTab(int count) {
        List<String> titles = Arrays.asList(typeNameLists);
        for (int i = count; i < titles.size(); i++) {
            View popTextView = LayoutInflater.from(activity).inflate(R.layout.textview_pop, null);
            TextView textView = popTextView.findViewById(R.id.tvPop);
            LinearLayout tvLayout = popTextView.findViewById(R.id.tvLayout);
            tvLayout.removeAllViews();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            textView.setLayoutParams(layoutParams);
            textView.setPadding(DpPxUtils.dip2px(activity, 20), 0, 0, 0);
            textView.setText(titles.get(i));
            textView.setTag(i);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isPopClick = true;
                    selectedTextView = (TextView) view;
                    int selectedTag = (int) selectedTextView.getTag();
                    type = selectedTag;
                    viewPager.setCurrentItem(selectedTag);
                    popupWindow.dismiss();
                }
            });
            moreTabLayout.addView(textView);
            textViews.add(textView);
        }
    }

    /**
     * 设置tab栏显示内容
     *
     * @param count
     */
    private void initTextView(int count) {
        List<String> titles = Arrays.asList(typeNameLists);
        for (int i = 0; i < titles.size(); i++) {
            if (i < count) {
                View popTextView = LayoutInflater.from(activity).inflate(R.layout.textview_pop, null);
                TextView textView = popTextView.findViewById(R.id.tvPop);
                LinearLayout tvLayout = popTextView.findViewById(R.id.tvLayout);
                tvLayout.removeAllViews();
                textView.setText(titles.get(i));
                textView.setTag(i);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isPopClick = false;
                        selectedTextView = (TextView) view;
                        int selectedTag = (int) selectedTextView.getTag();
                        stopKlistTcp(type);
                        type = selectedTag;
                        startKlistTcp(type);
                        viewPager.setCurrentItem(selectedTag);
                    }
                });
                textViews.add(textView);
                tab.addView(textView);
            }
        }
    }

    /**
     * 初始化popwindow里的控件
     *
     * @param contentView
     */
    private void initPopChidView(View contentView) {
        moreTabLayout = contentView.findViewById(R.id.tabPop);
        indexLayout = contentView.findViewById(R.id.llIndex);
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

                //setCurrentcy(currencyList);

                try {
//                    kData.parseKLine(currencyList, type);
//                    kLineDatas = kData.getKLineDatas();
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
                        kChartView.startAnimation();
//                        kChartView.refreshEnd();
                    } else {
//                        kChartView.refreshEnd();
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
     * 加载k线数据
     */
    private void loadKLineData() {
//        if (type != GlobalConstant.TAG_DIVIDE_TIME)
//            kChartView.showLoading();
//        else
//        kChartView.showLoading();
//        mProgressBar.setVisibility(View.VISIBLE);
//        Long to = System.currentTimeMillis();
//        endDate = DateUtils.getDate("HH:mm", to);
//        Long from = to;
//        switch (type) {
////            case GlobalConstant.TAG_DIVIDE_TIME:
////                Calendar c = Calendar.getInstance();
////                int hour = c.get(Calendar.HOUR_OF_DAY) - 6;
////                c.set(Calendar.HOUR_OF_DAY, hour);
////                String strDate = DateUtils.getFormatTime("HH:mm", c.getTime());
////                startDate = DateUtils.getDateTransformString(strDate, "HH:mm");
////                resolution = 1 + "";
////                String str = DateUtils.getFormatTime(null, c.getTime());
////                from = DateUtils.getTimeMillis(null, str);
////                break;
//            case GlobalConstant.TAG_ONE_MINUTE:
//                from = to - 24L * 60 * 60 * 1000;//前一天数据
//                resolution = 1 + "";
//                break;
//            case GlobalConstant.TAG_FIVE_MINUTE:
//                from = to - 2 * 24L * 60 * 60 * 1000;//前两天数据
//                resolution = 5 + "";
//                break;
//            case GlobalConstant.TAG_THIRTY_MINUTE:
//                from = to - 12 * 24L * 60 * 60 * 1000; //前12天数据
//                resolution = 30 + "";
//                break;
//            case GlobalConstant.TAG_AN_HOUR:
//                from = to - 24 * 24L * 60 * 60 * 1000;//前 24天数据
//                resolution = 1 + "H";
//                break;
//            case GlobalConstant.TAG_DAY:
//                from = to - 60 * 24L * 60 * 60 * 1000; //前60天数据
//                resolution = 1 + "D";
//                break;
//            case GlobalConstant.TAG_MONTH:
//                from = to - 1095 * 24L * 60 * 60 * 1000; //前三年数据
//                resolution = 1 + "M";
//                break;
//        }
//        getKLineData(symbol, from, to, resolution);
    }

    /**
     * 获取网络数据
     *
     * @param symbol
     * @param from
     * @param to
     * @param resolution
     */
    private void getKLineData(String symbol, Long from, Long to, String resolution) {
        HashMap<String, String> map = new HashMap<>();
        map.put("symbol", symbol);
        map.put("from", from + "");
        map.put("to", to + "");
        map.put("resolution", resolution);
        presenter.KData(map);
    }


    /**
     * 头部显示内容
     *
     * @param
     */
    private void setCurrentcy(SymbolBean symbolBean) {
        try {
            mCurrency = symbolBean;
//            for (NewCurrency currency : objs) {
//                if (symbol.equals(currency.getSymbol())) {
//                    mCurrency = currency;
//                    break;
//                }
//            }
            if (mCurrency != null) {
                String strUp = String.valueOf(mCurrency.getHigh());
                String strLow = String.valueOf(mCurrency.getLow());
                String strCount = String.valueOf(mCurrency.getVol());
//                Double douChg = mCurrency.getS();
//                String strRang = MathUtils.getRundNumber(mCurrency.getChg() * 100, 2, "########0.") + "%";
//                String strDataText = "≈" + MathUtils.getRundNumber(mCurrency.getClose() * MainActivity.rate * mCurrency.getBaseUsdRate(),
//                        2, null) + GlobalConstant.CNY;
                String strDataOne = String.valueOf(mCurrency.getClose());
                kUp.setText(strUp);
                kLow.setText(strLow);
                kCount.setText(strCount);
//                kRange.setText(getString(R.string.gains) + mCurrency.getScale() + "%");
                mDataOne.setText(strDataOne);
                mDataText.setText("≈" + mCurrency.getConvert() + "USDT");
                mDataOne.setTextColor(mCurrency.getScale() < 0 ? getResources().getColor(R.color.main_font_red) : getResources().getColor(R.color.main_font_green));
//                kRange.setTextColor(mCurrency.getScale()  < 0 ? getResources().getColor(R.color.main_font_red) : getResources().getColor(R.color.main_font_green));
//                kRange.setTextColor(mCurrency.getScale()  < 0 ? getResources().getColor(R.color.main_font_red) : getResources().getColor(R.color.main_font_green));
                kRange.setBackground(mCurrency.getScale() < 0 ? getResources().getDrawable(R.drawable.bg_kl_corner_red) : getResources().getDrawable(R.drawable.bg_kl_corner_green));
//                kLandDataOne.setTextColor(mCurrency.getScale()  < 0 ? getResources().getColor(R.color.main_font_red) : getResources().getColor(R.color.main_font_green));
//                kLandUp.setText(strUp);
//                kLandLow.setText(strLow);
//                kLandCount.setText(strCount);
                kRange.setText((mCurrency.getScale()  < 0 ? "-" : "+") + mCurrency.getScale() +"%");
//                kLandDataOne.setText(strDataOne);
//                kLandDataText.setText("≈" + mCurrency.getConvert() + "USDT");
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
//        EventBus.getDefault().post(new SocketMessage(0, NEWCMD.SUBSCRIBE_SYMBOL_DETAIL,
//                buildGetBodyJson(detail, "1").toString()));

        MyApplication.getApp().startTcp(new TcpEntity(detail,NEWCMD.SUBSCRIBE_SYMBOL_DETAIL));
    }

    private void startKlistTcp(int type){
        String klist = "market." + symbol + "_" + symbolType + ".klist." + typeLists[type];
//        EventBus.getDefault().post(new SocketMessage(0, NEWCMD.SUBSCRIBE_SYMBOL_KLIST,
//                buildGetBodyJson(klist, "1").toString())); //
        MyApplication.getApp().startTcp(new TcpEntity(klist,NEWCMD.SUBSCRIBE_SYMBOL_KLIST));
    }

    private void stopKlistTcp(int type){
        String klist = "market." + symbol + "_" + symbolType + ".klist." + typeLists[type];
//        EventBus.getDefault().post(new SocketMessage(0, ,
//                buildGetBodyJson(klist, "0").toString())); //
        MyApplication.getApp().stopTcp(new TcpEntity(klist,NEWCMD.SUBSCRIBE_SYMBOL_KLIST));
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
     * 初始化viewpager
     *
     * @param titles
     */
    private void initViewpager(List<String> titles) {
        for (int i = 0; i < titles.size(); i++) {
            View view = LayoutInflater.from(activity).inflate(R.layout.layout_kchartview, null);
//            if (i == 0) {
//                minuteChartView = view.findViewById(R.id.minuteChartView);
//                minuteChartView.setVisibility(View.VISIBLE);
//                RelativeLayout mLayout = view.findViewById(R.id.mLayout);
//                mProgressBar = new ProgressBar(activity);
//                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewUtil.Dp2Px(activity, 50), ViewUtil.Dp2Px(activity, 50));
//                lp.addRule(CENTER_IN_PARENT);
//                mLayout.addView(mProgressBar, lp);
//            } else {
                KLineChartView kChartView = view.findViewById(R.id.kLineChartView);
                initKchartView(kChartView);
                kChartView.setVisibility(View.VISIBLE);
                kChartView.setAdapter(new KLineChartAdapter());
//            }
            views.add(view);
        }
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(views);
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
//        kChartView.setOnSelectedChangedListener(new BaseKChartView.OnSelectedChangedListener() {
//            @Override
//            public void onSelectedChanged(BaseKChartView view, Object point, int index) {
//                KLineEntity data = (KLineEntity) point;
//            }
//        });
    }

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
                    tvMore.setBackground(getDrawable(R.drawable.shape_bg_kline_tab_normal));
                } else {
                    tvMore.setText(getString(R.string.more));
                    tvMore.setSelected(false);
                    tvMore.setBackground(getDrawable(R.drawable.shape_bg_kline_tab_normal));
                    tvIndex.setBackground(getDrawable(R.drawable.shape_bg_kline_tab_normal));
                    textViews.get(j).setSelected(true);
                }
                View view = views.get(j);
                kChartView = view.findViewById(R.id.kLineChartView);
                if ( maView.isSelected()){
                    kChartView.changeMainDrawType(Status.MA);
                }else if (bollView.isSelected()){
                    kChartView.changeMainDrawType(Status.BOLL);
                }
                kChartView.setChildDraw(childType);
                kChartAdapter = (KLineChartAdapter) kChartView.getAdapter();
                if (kChartAdapter.getCount()  == 0) {
                    loadKLineData();
                }
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
        DataParse kData = new DataParse();
        switch (type) {
//            case GlobalConstant.TAG_DIVIDE_TIME: // 分时图
//                com.github.tifezh.kchartlib.utils.WonderfulLogUtils.logi("KDataSuccess");
//                mProgressBar.setVisibility(View.GONE);
//                try {
//                    kData.parseMinutes(obj, (float) mCurrency.getLastDayClose());
//                    ArrayList<MinutesBean> objList = kData.getDatas();
//                    if (objList != null && objList.size() > 0) {
//                        ArrayList<MinuteLineEntity> minuteLineEntities = new ArrayList<>();
//                        for (int i = 0; i < objList.size(); i++) {
//                            MinuteLineEntity minuteLineEntity = new MinuteLineEntity();
//                            MinutesBean minutesBean = objList.get(i);
//                            minuteLineEntity.setAvg(minutesBean.getAvprice()); // 成交价
//                            minuteLineEntity.setPrice(minutesBean.getCjprice());
//                            minuteLineEntity.setTime(DateUtils.getDateTransformString(minutesBean.getTime(), "HH:mm"));
//                            minuteLineEntity.setVolume(minutesBean.getCjnum());
//                            minuteLineEntity.setClose(minutesBean.getClose());
//                            minuteLineEntities.add(minuteLineEntity);
//                        }
//                        if (isFirstLoad) {
//                            DataHelper.calculateMA30andBOLL(minuteLineEntities);
//                            minuteChartView.initData(minuteLineEntities,
//                                    startDate,
//                                    endDate,
//                                    null,
//                                    null,
//                                    (float) mCurrency.getLow(), maView.isSelected());
//                            isFirstLoad = false;
//                        }
//                    }
//                } catch (Exception e) {
//                    ToastUtils.showToast(getString(R.string.parse_error));
//                }
//                break;
//            default:
//                try {
//                    kData.parseKLine(obj, type);
//                    kLineDatas = kData.getKLineDatas();
//                    if (kLineDatas != null && kLineDatas.size() > 0) {
//                        ArrayList<KLineEntity> kLineEntities = new ArrayList<>();
//                        for (int i = 0; i < kLineDatas.size(); i++) {
//                            KLineEntity lineEntity = new KLineEntity();
//                            KLineBean kLineBean = kLineDatas.get(i);
//                            lineEntity.setDate(kLineBean.getDate());
//                            lineEntity.setOpen(kLineBean.getOpen());
//                            lineEntity.setClose(kLineBean.getClose());
//                            lineEntity.setHigh(kLineBean.getHigh());
//                            lineEntity.setLow(kLineBean.getLow());
//                            lineEntity.setVolume(kLineBean.getVol());
//                            kLineEntities.add(lineEntity);
//                        }
//                        kChartAdapter.addFooterData(DataHelper.getALL(activity, kLineEntities));
//                        kChartView.startAnimation();
//                        kChartView.refreshEnd();
//                    } else {
//                        kChartView.refreshEnd();
//                    }
//                } catch (Exception e) {
//                    ToastUtils.showToast(getString(R.string.parse_error));
//                }
//
//                break;
        }
    }

    @Override
    public void allCurrencySuccess(List<Currency> obj) {
//        if (obj != null) {
//            currencies.clear();
//            currencies.addAll(obj);
//            setCurrentcy(currencies);
//        }
    }

    @Override
    public void doDeleteOrCollectSuccess(String msg) {
//        if (isFace) {
//            ToastUtils.showToast(getString(R.string.text_cancel_success));
//            //mTvCollect.setText(getString(R.string.text_add_favorite));
//            isFace = false;
//            mTvCollect.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.icon_collect_normal), null, null);
//        } else {
//            ToastUtils.showToast(getString(R.string.text_add_success));
//            isFace = true;
//            mTvCollect.setText(getString(R.string.text_collected));
//            mTvCollect.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.icon_collect_hover), null, null);
//        }

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
//                if (type == GlobalConstant.TAG_DIVIDE_TIME) {
//                    minuteChartView.setMAandBOLL(maView.isSelected(), bollView.isSelected());
//                } else {
//                    kChartView.setMAandBOLL(maView.isSelected(), bollView.isSelected());
//                }


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


    /**
     * 获取深度图数据
     */
//    private void getDepth() {
//        post().url(UrlFactory.getDepth()).addParams("symbol", symbol).build().execute(new StringCallback() {
//            @Override
//            public void onError(Request request, Exception e) {
//
//            }
//
//            @Override
//            public boolean onResponse(String response) {
//                doLogicData(response);
//                return false;
//            }
//        });

//    }

//    /**
//     * 解析数据
//     *
//     * @param response
//    */
//
//    private List<Exchange> sellExchangeList = new ArrayList<>();
//    private List<Exchange> buyExchangeList = new ArrayList<>();
//    private void doLogicData(String response) {
//        try {
//            gson = new Gson();
//            DepthChart result = gson.fromJson(response, new TypeToken<DepthChart>() {
//            }.getType());
//            if (result != null) {
//                DepthChart.AskBean ask = result.getAsk();
//                if (ask.getMinAmount() == 0 && ask.getMaxAmount()==0)return;
//
////                lineChartBeansell = LocalJsonAnalyzeUtil.JsonToObject(this,"line_lib.json",Linelibbean.class);
//                itemsBeensell = ask.getItems();
//                if (itemsBeensell.size() == 0){
//                    mLineChart.setVisibility(View.GONE);
//                }
//                lineChartManager2.showLineChartwo(itemsBeensell, "累计", getResources().getColor(R.color.kgreen));
//                lineChartManager2.setMarkerView(this);
//
//                DepthChart.BidBean bid = result.getBid();
//                if (bid.getMinAmount() == 0 && bid.getMaxAmount()==0)return;
//
//                itemsBeenBuy = bid.getItems();
//                if(itemsBeenBuy.size() == 0){
//                    lineChartbuy.setVisibility(View.GONE);
//                }
//                lineChartManager.showLineChar(itemsBeenBuy, "累计", getResources().getColor(R.color.kgreen));
//                lineChartManager.setMarkerView(this);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//    private List<DepthChart.AskBean.ItemsBean>itemsBeensell;
//    private List<DepthChart.BidBean.ItemsBeanX>itemsBeenBuy;
//    private Gson gson;
}
