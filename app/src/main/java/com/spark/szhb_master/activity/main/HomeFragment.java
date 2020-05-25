package com.spark.szhb_master.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.mcxtzhang.nestlistview.NestFullListView;
import com.mcxtzhang.nestlistview.NestFullListViewAdapter;
import com.mcxtzhang.nestlistview.NestFullViewHolder;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.login.LoginStepOneActivity;
import com.spark.szhb_master.activity.main.presenter.CommonPresenter;
import com.spark.szhb_master.activity.main.presenter.CollectView;
import com.spark.szhb_master.activity.kline.KlineActivity;
import com.spark.szhb_master.activity.message.WebViewActivity;
import com.spark.szhb_master.activity.mycc.MyccActivity;
import com.spark.szhb_master.activity.wallet_coin.RechargeActivity;
import com.spark.szhb_master.adapter.BannerImageLoader;
import com.spark.szhb_master.adapter.HomeOneAdapter;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.base.BaseTransFragment;
import com.spark.szhb_master.dialog.ShiMingDialog;
import com.spark.szhb_master.entity.BannerEntity;
import com.spark.szhb_master.entity.BannerInfo;
import com.spark.szhb_master.entity.Coin;
import com.spark.szhb_master.entity.Message;
import com.spark.szhb_master.entity.NewCurrency;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.entity.User;
import com.spark.szhb_master.factory.UrlFactory;
import com.spark.szhb_master.factory.socket.NEWCMD;
import com.spark.szhb_master.serivce.SocketResponse;
import com.spark.szhb_master.ui.intercept.MyScrollView;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.SharedPreferenceInstance;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.spark.szhb_master.utils.okhttp.StringCallback;
import com.sunfusheng.marqueeview.MarqueeView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;
import okhttp3.Request;

import static android.app.Activity.RESULT_OK;
import static com.spark.szhb_master.utils.okhttp.OkhttpUtils.post;

/**
 * Created by Administrator on 2018/1/7.
 */

public class HomeFragment extends BaseTransFragment implements MainContract.HomeView, CollectView {
    public static final String TAG = HomeFragment.class.getSimpleName();
    @BindView(R.id.rvMessageTag)
    RelativeLayout rvMessageTag;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.rvContent)
    RecyclerView rvContent; // 固定三个grid
    @BindView(R.id.marqueeView)
    MarqueeView marqueeView;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;


    @BindView(R.id.scrollView)
    MyScrollView scrollView;
    @BindView(R.id.ivchatTip)
    ImageView ivchatTip;


    @BindView(R.id.llHomeContent)
    LinearLayout llHomeContent;

    @BindView(R.id.llXinshouzhinan)
    LinearLayout llXinshouzhinan;

    @BindView(R.id.llXinrenlingj)
    LinearLayout llXinrenlingj;

    @BindView(R.id.llChongbi)
    LinearLayout llChongbi;

    @BindView(R.id.llwdcc)
    LinearLayout llwdcc;

    @BindView(R.id.llyqhy)
    LinearLayout llyqhy;

    @BindView(R.id.rlhy)
    RelativeLayout rlhy;

    @BindView(R.id.cstFullShowListView)
    NestFullListView nestFullListView;

    @BindView(R.id.symblistview)
    NestFullListView symblistview;

    private List<String> imageUrls = new ArrayList<>();
    private List<NewCurrency> currencies = new ArrayList<>();
    private List<NewCurrency> currenciesTwo = new ArrayList<>();
    private HomeOneAdapter adapter;
    private MainContract.HomePresenter presenter;
    private List<Message> messageList = new ArrayList<>();
    private List<String> info = new ArrayList<>();
    private CommonPresenter commonPresenter;
    private boolean isfirst = false;

    @Override
    public void onResume() {
        super.onResume();
        if (marqueeView != null) marqueeView.startFlipping();
        banner.startAutoPlay();
        //10.19更改
        User user = MyApplication.getApp().getCurrentUser();
        if (!MyApplication.getApp().isLogin()) {

        } else if (!StringUtils.isEmpty(user.getNick_name())){
            getMoney(getmActivity().getToken());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        marqueeView.startFlipping();
    }

    @Override
    public void onStop() {
        super.onStop();
        marqueeView.stopFlipping();
    }

    @Override
    protected String getmTag() {
        return TAG;
    }

    @Override
    public void onPause() {
        super.onPause();
        banner.stopAutoPlay();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    private NestFullListViewAdapter mListAdapter;
    @Override
    protected void initView() {
        super.initView();
        rvMessageTag.setVisibility(View.VISIBLE);
        scrollView.requestDisallowInterceptTouchEvent(false);
        tvGoto.setVisibility(View.GONE);
        //10.19更改
//        if (!MyApplication.getApp().isLogin()) {
//            text_balance.setVisibility(View.GONE);
//            text_balance_cny.setVisibility(View.GONE);
//            ivSee.setVisibility(View.GONE);
//            btn_Login.setVisibility(View.VISIBLE);
//            login_after.setVisibility(View.VISIBLE);
//        } else {
//            getMoney(getmActivity().getToken());
//            text_balance.setVisibility(View.VISIBLE);
//            text_balance_cny.setVisibility(View.VISIBLE);
//            ivSee.setVisibility(View.VISIBLE);
//            btn_Login.setVisibility(View.GONE);
//            login_after.setVisibility(View.GONE);
//        }
//        isfirst = true;

        String p = "123456";
        char[] d = p.toCharArray();

        List<String> list = new ArrayList<>();
        for (int i=0;i<d.length;i++){
            list.add(String.valueOf(d[i]));
        }

        nestFullListView.setAdapter(new NestFullListViewAdapter<String>(R.layout.item_people,list) {
            @Override
            public void onBind(int i, String s, NestFullViewHolder nestFullViewHolder) {
                ((TextView)nestFullViewHolder.getView(R.id.ip_tv)).setText(s);
            }
        });

        symblistview.setAdapter(mListAdapter = new NestFullListViewAdapter<NewCurrency>(R.layout.item_market,currencies) {
            @Override
            public void onBind(int i, NewCurrency item, NestFullViewHolder helper) {
                boolean tol = Float.parseFloat(item.getScale()) > 0 ? true : false;

                helper.setText(R.id.tvBuySymbol, item.getSymbol());
                String symbol = item.getSymbol();
                if (symbol.equals("BTC")){
                    helper.setText(R.id.tvSecSymbol, "/" + item.getType()).setVisible(R.id.tvSecSymbol,true);
                    helper.getView(R.id.tvCHNName).setVisibility(View.GONE);
                }else{
                    helper.setVisible(R.id.tvSecSymbol,false);

                    if (symbol.equals("ETH")){
                        helper.setText(R.id.tvCHNName, "以太坊").setVisible(R.id.tvCHNName,true);
                    }else if (symbol.equals("EOS")){
                        helper.setText(R.id.tvCHNName, "柚子币").setVisible(R.id.tvCHNName,true);
                    }else if (symbol.equals("LTC")){
                        helper.setText(R.id.tvCHNName, "莱特币").setVisible(R.id.tvCHNName,true);
                    }else if (symbol.equals("BCH")){
                        helper.setText(R.id.tvCHNName, "比特币现金").setVisible(R.id.tvCHNName,true);
                    }else if (symbol.equals("XRP")){
                        helper.setText(R.id.tvCHNName, "瑞波币").setVisible(R.id.tvCHNName,true);
                    }else if (symbol.equals("ETC")){
                        helper.setText(R.id.tvCHNName, "以太经典").setVisible(R.id.tvCHNName,true);
                    }else if (symbol.equals("TRX")){
                        helper.setText(R.id.tvCHNName, "波场").setVisible(R.id.tvCHNName,true);
                    }else if (symbol.equals("BSV")){
                        helper.setText(R.id.tvCHNName, "比特币SV").setVisible(R.id.tvCHNName,true);
                    }
                }

                helper.setText(R.id.tvPrice, new BigDecimal(Double.parseDouble(item.getClose()))
                        .setScale(MyApplication.getApp().getSymbolSize(item.getSymbol()), RoundingMode.UP).toString());

                if (tol){
                    helper.setText(R.id.item_home_chg, "+"+ item.getScale() + "%");
                    helper.getView(R.id.item_home_chg).setEnabled(true);
                }else{
                    helper.setText(R.id.item_home_chg, item.getScale() + "%");
                    helper.getView(R.id.item_home_chg).setEnabled(false);
                }
            }
        });
        symblistview.setOnItemClickListener(new NestFullListView.OnItemClickListener() {
            @Override
            public void onItemClick(NestFullListView nestFullListView, View view, int i) {
                NewCurrency currency = currencies.get(i);
                Bundle bundle = new Bundle();
                bundle.putString("symbol", currency.getSymbol());
                bundle.putSerializable("currency", currency);
                showActivity(KlineActivity.class, bundle);
            }
        });
    }



    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.home));
        new CommonPresenter(Injection.provideTasksRepository(getActivity().getApplicationContext()), this);
        initThreeContent();

        iniHomeSocketData();
    }

    private void iniHomeSocketData() {

    }

    @Override
    protected void loadData() {
        notifyData();
        isNeedLoad = false;
        getImage();
        getMarqueeText();
        //10.19更改
        if (!MyApplication.getApp().isLogin()) {

        } else {

            getMoney(getmActivity().getToken());
        }
    }


    /**
     * 获取滚动图片
     */
    private void getImage() {
        if (imageUrls == null || imageUrls.size() == 0) {
            HashMap map = new HashMap<>();
            presenter.banners(map);
        }
    }

    /**
     * 获取滚动text
     */
    private void getMarqueeText() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("pageNo", "1");
        map.put("pageSize", "5");
        presenter.getMarqueeText(map);
    }

    /**
     * 初始化三个固定的grid
     */
    private void initThreeContent() {
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
        rvContent.setLayoutManager(manager);
        adapter = new HomeOneAdapter(R.layout.item_home_one, currenciesTwo);
        adapter.isFirstOnly(true);
        rvContent.setAdapter(adapter);

        rvContent.setHasFixedSize(true);
        rvContent.setNestedScrollingEnabled(false);
    }

    /**
     * 初始化排行榜
     */
    private void initSortContent() {
        //mSortContent.setLayoutManager(new LinearLayoutManager(getActivity()));
//        sortAdapter = new HomeSortAdapter(currencies,getContext());
//        sortAdapter.isFirstOnly(true);
//        sortAdapter.setLoad(true);
//        mSortContent.setAdapter(sortAdapter);
//
//        mSortContent.setHasFixedSize(true);
//        mSortContent.setNestedScrollingEnabled(false);

        //Test

    }

    private JSONObject buildGetBodyJson(String value, int id) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("value", value);
            if (id != 0) // 不需要id
                obj.put("type", id);
            return obj;
        } catch (Exception ex) {
            return null;
        }
    }

    @OnClick({R.id.llChongbi,R.id.ivMessage,R.id.llXinshouzhinan,R.id.rlhy,R.id.llwdcc})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        if (v.getId() == R.id.btn_Login) {
            showActivity(LoginStepOneActivity.class, null, LoginStepOneActivity.RETURN_LOGIN);
            return;
        }
        switch (v.getId()){
            case R.id.llwdcc:
                if (!MyApplication.getApp().isLogin()){
                    showActivity(LoginStepOneActivity.class, null, LoginStepOneActivity.RETURN_LOGIN);
                    return;
                }
                showActivity(MyccActivity.class,null);
                break;
            case R.id.llChongbi:
                if (!MyApplication.getApp().isLogin()){
                    showActivity(LoginStepOneActivity.class, null, LoginStepOneActivity.RETURN_LOGIN);
                    return;
                }
                showActivity(RechargeActivity.class,null);
                break;
            case R.id.llXinshouzhinan:{

            }
            break;
            case R.id.rlhy:{
                if (operateCallback!=null)
                    operateCallback.toheyue();
            }
            break;
        }
    }



    /**
     * 盘口信息的返回 和 当前委托的返回
     *
     * @param response SocketResponse
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(SocketResponse response) {
        if (response.getCmd() == null) return;
        if (response.getCmd() == NEWCMD.SUBSCRIBE_HOME_TRADE){

        }
    }

    private NewCurrency currency;
    @Override
    protected void setListener() {
        super.setListener();
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                doCollect(position);
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                currency = (NewCurrency) adapter.getItem(position);
                skipTrade();
            }
        });

        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                BannerInfo.PhotoBean photoBean = mBannerInfo.getPhoto().get(position);
                if (photoBean != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("title", "title");
                    bundle.putString("url", photoBean.getLink());
                    bundle.putBoolean("isImage", true);
                    showActivity(WebViewActivity.class, bundle);
                }
            }
        });
        marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                Message message = messageList.get(position);
                Bundle bundle = new Bundle();
//                bundle.putString("id", message.getId());
                bundle.putString("url", message.getContent());
                showActivity(WebViewActivity.class, bundle);
            }
        });
    }

    /**
     * 跳转到k线界面
     */
    private void skipTrade() {
        if (currency != null) {
//            Intent intent = new Intent(activity, TradeActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("currency", currency);
//            bundle.putInt("type", 1);
//            intent.putExtras(bundle);
//            activity.startActivity(intent); //  执行父级activity的回调

            Bundle bundle = new Bundle();
            bundle.putString("symbol", currency.getSymbol());
            bundle.putSerializable("currency", currency);
            showActivity(KlineActivity.class, bundle);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 0 && resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                ((MainActivity) getActivity()).skipTradeView(bundle);
            }
        }
    }

    /**
     * 收藏
     *
     * @param position
     */
    private void doCollect(int position) {
        if (!MyApplication.getApp().isLogin()) {
            ToastUtils.showToast("请先登录再添加收藏！");
            return;
        }
//        String symbol = currencies.get(position).getSymbol();
//        HashMap<String, String> map = new HashMap<>();
//        map.put("symbol", symbol);
//        if (currencies.get(position).isCollect()) {
//            commonPresenter.delete(map, position);
//        } else {
//            commonPresenter.add(map, position);
//        }
    }


    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        immersionBar.flymeOSStatusBarFontColor("#FFFFFF").init();
        if (!isSetTitle) {
            immersionBar.setTitleBar(getActivity(), llTitle);
            isSetTitle = true;
        }
    }

    @Override
    public void setPresenter(MainContract.HomePresenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void setPresenter(CommonPresenter presenter) {
        this.commonPresenter = presenter;
    }

    public void dataLoadedThree(List<NewCurrency> tow) {
        llHomeContent.setVisibility(View.VISIBLE);
        this.currenciesTwo.clear();
        if (tow != null && tow.size() > 3) {
            for (int i = 0; i < 3; i++) {
                this.currenciesTwo.add(tow.get(i));
            }
        } else {
            this.currenciesTwo.addAll(tow);
        }
        adapter.notifyDataSetChanged();
    }

    public void dataLoaded(List<NewCurrency> currencies) {
        this.currencies.clear();

        for (int i=0;i<6;i++){
            if (i< currencies.size()){
                this.currencies.add(currencies.get(i));
            }
        }
        if (symblistview != null) {
//            if (currencies.size() == 0)
//                symblistview..setEmptyView(R.layout.empty_no_message);
            symblistview.updateUI();
        }
    }



    public void showEmptyView() {
//        llHomeContent.setVisibility(View.GONE);
    }

    public void setChatTip(boolean hasNew) {
        if (hasNew) ivchatTip.setVisibility(View.VISIBLE);
        else ivchatTip.setVisibility(View.INVISIBLE);
    }

    @Override
    public void deleteFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode((BaseActivity) activity, code, toastMessage);
    }

    @Override
    public void deleteSuccess(String obj, int position) {
        //this.currencies.get(position).setCollect(false);
        //adapter.notifyDataSetChanged();
    }

    @Override
    public void addFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode((BaseActivity) activity, code, toastMessage);
    }

    @Override
    public void addSuccess(String obj, int position) {
        //this.currencies.get(position).setCollect(true);
        //adapter.notifyDataSetChanged();
    }

    public void notifyData() {
        if (adapter != null) adapter.notifyDataSetChanged();
    }


    private BannerInfo mBannerInfo;
    @Override
    public void bannersSuccess(BannerInfo bannerInfo) {
        MyApplication.getApp().setBaseInfo(bannerInfo);
        mBannerInfo = bannerInfo;
        for (BannerInfo.PhotoBean photoBean : bannerInfo.getPhoto()) {
            String url = photoBean.getUrl();
            if (StringUtils.isNotEmpty(url)) {
                if (!url.contains("http")) {
                    url = UrlFactory.getHost() + "/" + url;
                }
                imageUrls.add(url);
            }

        }

        if (imageUrls.size() > 0) {
            banner.setImages(imageUrls); // 设置图片集合
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR).setIndicatorGravity(BannerConfig.CENTER)// 设置样式
                    .setImageLoader(new BannerImageLoader());
            banner.setDelayTime(3000); // 设置轮播时间

            banner.start();
        }
    }

    @Override
    public void bannersFail(Integer code, String toastMessage) {

    }

    @Override
    public void getMarqueeSuccess(List<Message> messages) {
        messageList.clear();
        messageList.addAll(messages);
        setMarqueeView(messageList);
    }

    @Override
    public void getMarqueeFail(Integer code, String toastMessage) {

    }

    @Override
    public void safeSettingFiled(Integer code, String toastMessage) {

    }
    private SafeSetting safeSetting;
    @Override
    public void safeSettingSuccess(SafeSetting obj) {
        if (obj == null)
            return;
        safeSetting = obj;
        if (safeSetting.getRealVerified() == 0) {
            ShiMingDialog shiMingDialog = new ShiMingDialog(getContext());
            String name = safeSetting.getRealNameRejectReason();
            if (safeSetting.getRealVerified() == 0) {
                if (safeSetting.getRealAuditing() == 1) {
                    shiMingDialog.setEntrust(7, name,1);
                } else {
                    if (safeSetting.getRealNameRejectReason() != null)
                        shiMingDialog.setEntrust(8, name,1);
                    else
                        shiMingDialog.setEntrust(9, name,1);
                }
            } else {
                shiMingDialog.setEntrust(6, name,1);
            }
            shiMingDialog.show();
        } else {

        }
    }

    public void tcpNotify() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
        if (symblistview!=null)
            symblistview.updateUI();
    }


    private void setMarqueeView(List<Message> messageList) {
        info.clear();
        for (Message message : messageList) {
            info.add(message.getTitle());
        }
        marqueeView.startWithList(info);
    }

    int status = 1;
    int num = 0;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (status == 1 && num == 1) {
            status = 2;
            marqueeView.startFlipping();
        } else if (status == 2) {
            status = 1;
            marqueeView.stopFlipping();
        }
        num = 1;
        Log.e("TAG", status + "---------" + num);
        if (isfirst) {
            getivSee();
        }
    }

    private OperateCallback operateCallback;

    public void setOperateCallback(OperateCallback operateCallback) {
        this.operateCallback = operateCallback;
    }

    public interface OperateCallback {
        void tofabi();
        void toheyue();
    }

    private void getMoney(String token) {
        post().url(UrlFactory.getWalletUrl()).addHeader("x-auth-token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
            }
            @Override
            public boolean onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<Coin> obj = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<Coin>>() {}.getType());
                        if (obj == null) return false;
                        calcuTotal(obj);
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
                return false;
            }
        });
    }
    public static double sumUsd = 0;
    double sumCny = 0;
    private void calcuTotal(List<Coin> coins) {
        sumUsd = 0;
        sumCny = 0;
        for (Coin coin : coins) {
//            sumUsd += (coin.getBalance() * coin.getCoin().getUsdRate());
//            sumCny += (coin.getBalance() * coin.getCoin().getCnyRate());
            sumUsd += ((coin.getBalance() * coin.getCoin().getUsdRate()) + (coin.getFrozenBalance() * coin.getCoin().getUsdRate()));
            sumCny += ((coin.getBalance() * coin.getCoin().getCnyRate()) + (coin.getFrozenBalance() * coin.getCoin().getCnyRate()));
        }
        getivSee();
    }

    private void getivSee() {
        if (SharedPreferenceInstance.getInstance().getMoneyShowType() == 2){

        }else {

        }
    }
}
