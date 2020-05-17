package com.spark.szhb_master.activity.kline;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.VolumeAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.base.BaseFragment;
import com.spark.szhb_master.entity.TcpEntity;
import com.spark.szhb_master.entity.VolumeBean;
import com.spark.szhb_master.entity.VolumeInfo;
import com.spark.szhb_master.factory.socket.ISocket;
import com.spark.szhb_master.factory.socket.NEWCMD;
import com.spark.szhb_master.serivce.SocketMessage;
import com.spark.szhb_master.serivce.SocketResponse;
import com.spark.szhb_master.utils.LogUtils;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import config.Injection;

/**
 * kline_成交
 * Created by daiyy on 2018/6/4 0004.
 */

public class VolumeFragment extends BaseFragment implements KlineContract.VolumeView {
    @BindView(R.id.tvNumberV)
    TextView tvNumberV;
    @BindView(R.id.tvPriceV)
    TextView tvPriceV;
    @BindView(R.id.rvVolume)
    RecyclerView recyclerView;
    @BindView(R.id.volumeBar)
    ProgressBar volumeBar;
    private String symbol;
    private String symbolType;
    private List<VolumeBean> mDataList;
    private VolumeAdapter adapter;
    private Activity activity;

    private KlineContract.VolumePresenter presenter;

    private boolean tcpStatus = false;
    private int pTcpSwitch = 0;
    private Handler mHandler;
    private boolean mRunning = true;

    public static VolumeFragment getInstance(String symbol,String symbolType,int tcpstatus) {
        VolumeFragment volumeFragment = new VolumeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("symbol", symbol);
        bundle.putSerializable("symbolType", symbolType);
        bundle.putInt("tcpstatus",tcpstatus);
        volumeFragment.setArguments(bundle);
        return volumeFragment;
    }

    public void reInit(String symbol,String symbolType){

        stopTcp();

        this.symbol = symbol;
        this.symbolType = symbolType;

        tvNumberV.setText(getResources().getString(R.string.number) + "(" + symbol + ")");
        mDataList.clear();
        mDataList = initVolumeData();
        adapter.setObjList(mDataList);
        adapter.notifyDataSetChanged();

        startTCP();

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        mRunning = true;
    }

//    @Override
//    protected void onFragmentVisibleChange(boolean isVisible) {
//        super.onFragmentVisibleChange(isVisible);
//        if (isVisible){
//            if (!tcpStatus)
//                startTCP();
//        }else{
//            stopTcp();
//        }
//    }

    @Override
    public void onStop() {
        super.onStop();
        // 取消订阅
        //EventBus.getDefault().post(new SocketMessage(0, ISocket.CMD.UNSUBSCRIBE_EXCHANGE_TRADE, symbol));
//        if (pTcpSwitch == 0){
//            stopTcp();
//        }

        EventBus.getDefault().unregister(this);
        mRunning = false;
    }

    private void stopTcp(){
        tcpStatus = false;
        String tradedetail = "market." + symbol + "_" + symbolType + ".trade.detail";
        MyApplication.getApp().stopTcp(new TcpEntity(tradedetail,NEWCMD.SUBSCRIBE_SYMBOL_TRADEDETAIL));
    }

    private void startTCP() {
        // 开始订阅
        tcpStatus = true;
        String tradedetail = "market." + symbol + "_" + symbolType + ".trade.detail";
        MyApplication.getApp().startTcp(new TcpEntity(tradedetail,NEWCMD.SUBSCRIBE_SYMBOL_TRADEDETAIL));
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

    private boolean rcvEnable = false;

    /**
     * socket 推送过来的信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSocketMessage(SocketResponse response) {
        if (response.getCmd() == NEWCMD.SUBSCRIBE_SYMBOL_TRADEDETAIL) {
            try {
                if (rcvEnable) {
                    rcvEnable = false;
                    List<VolumeBean> volumeBeanList = new Gson().fromJson(response.getResponse(), new TypeToken<List<VolumeBean>>() {}.getType());
                    String strSymbol = response.getRemark();
                    if (strSymbol != null && strSymbol.equals(symbol)) {
                        addVolumeData(volumeBeanList);
                        adapter.setObjList(mDataList);
                        adapter.notifyDataSetChanged();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_volume;
    }

    @Override
    protected void initView() {
        intLv();
        recyclerView.setNestedScrollingEnabled(false);
    }

    /**
     * 初始化列表数据
     */
    private void intLv() {
        activity = getActivity();
        mDataList = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        manager.setStackFromEnd(false);
        recyclerView.setLayoutManager(manager);
        mDataList = initVolumeData();
        adapter = new VolumeAdapter(activity, mDataList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        activity = getActivity();
        new VolumePresenterImpl(Injection.provideTasksRepository(activity), this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            symbol = bundle.getString("symbol");
            symbolType = bundle.getString("symbolType");
            pTcpSwitch = bundle.getInt("tcpstatus");
//            String[] strings = symbol.split("/");
            tvNumberV.setText(getResources().getString(R.string.number) + "(" + symbol + ")");
            tvPriceV.setText(getResources().getString(R.string.price) + "(USDT)");
//            startTCP();
//            getVolume();
        }

        HandlerThread thread = new HandlerThread("MyHandlerThread");
        thread.start();//创建一个HandlerThread并启动它
        mHandler = new Handler(thread.getLooper());//使用HandlerThread的looper对象创建Handler，如果使用默认的构造方法，很有可能阻塞UI线程
        mHandler.post(mBackgroundRunnable);//将线程post到Handler中

        startTCP();
    }

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


    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mBackgroundRunnable);
    }


    /**
     * 获取成交数据
     */
    private void getVolume() {
        if (volumeBar != null)
            volumeBar.setVisibility(View.VISIBLE);
        HashMap<String, String> map = new HashMap<>();
        map.put("symbol", symbol);
        map.put("size", "20");
        presenter.getVolume(map);
    }

    /**
     */
    private void addVolumeData(List<VolumeBean> objList) {
        if (objList != null) {
            for (int i = 0; i < objList.size(); i++) {
                mDataList.add(0,objList.get(i));
            }
        }

        for(int i=20; i < mDataList.size(); i++){
            mDataList.remove(i);
        }
    }

    private List<VolumeBean> initVolumeData() {
        ArrayList<VolumeBean> volumeBeans = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            VolumeBean volumeBean = new VolumeBean();
            volumeBean.setTs(-1);
            volumeBean.setDirection(null);
            volumeBean.setPrice(-1);
            volumeBean.setAmount(-1);
            volumeBeans.add(volumeBean);
        }
        return volumeBeans;
    }


    @Override
    public void setPresenter(KlineContract.VolumePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getVolumeSuccess(ArrayList<VolumeInfo> list) {
//        if (volumeBar != null)
//            volumeBar.setVisibility(View.GONE);
//        objList = initData(list);
//        if (adapter == null) {
//            adapter = new VolumeAdapter(activity, objList);
//            recyclerView.setAdapter(adapter);
//        }
    }

    @Override
    public void dpPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode((BaseActivity) activity, code, toastMessage);
    }
}
