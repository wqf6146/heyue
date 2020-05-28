package com.spark.szhb_master;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.spark.szhb_master.activity.login.LoginActivity;
import com.spark.szhb_master.activity.login.LoginStepOneActivity;
import com.spark.szhb_master.activity.main.MainActivity;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.BannerInfo;
import com.spark.szhb_master.entity.NewCurrency;
import com.spark.szhb_master.entity.SymbolListBean;
import com.spark.szhb_master.entity.TcpEntity;
import com.spark.szhb_master.entity.User;
import com.spark.szhb_master.factory.socket.NEWCMD;
import com.spark.szhb_master.serivce.MyTextService;
import com.spark.szhb_master.serivce.SocketMessage;
import com.spark.szhb_master.utils.FileUtils;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.LogUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.Config;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;
import org.litepal.LitePal;
import org.xutils.x;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by pc on 2017/3/8.
 */
public class MyApplication extends Application {
    private boolean isReleased = false; // 是否发布了
    private boolean isLoginStatusChange = false; // 当前用户信息是否发生改变
    public static MyApplication app;
    private User currentUser = new User();
    private TextView tvToast; // Toast
    private int mWidth; // 当前手机屏幕的宽高
    private int mHeight;

    private String TAG = MyApplication.class.toString();
    private BannerInfo mBaseInfo;

    // 正常状态
    public static final int STATE_NORMAL = 0;
    // 从后台回到前台
    public static final int STATE_BACK_TO_FRONT = 1;
    // 从前台进入后台
    public static final int STATE_FRONT_TO_BACK = 2;

    // APP状态
    private static int sAppState = STATE_NORMAL;
    // 标记程序是否已进入后台(依据onStop回调)
    private boolean flag;
    // 标记程序是否已进入后台(依据onTrimMemory回调)
    private boolean background;
    // 从前台进入后台的时间
    private static long frontToBackTime;
    // 从后台返回前台的时间
    private static long backToFrontTime;

    private List<TcpEntity> mTcpEntity = new ArrayList<>();

    public List<TcpEntity> getTcpEntity() {
        return mTcpEntity;
    }

    private SymbolListBean mSymbolListConfig;

    public void setSymbolListConfig(SymbolListBean symbolListConfig) {
        this.mSymbolListConfig = symbolListConfig;
        saveSymbolListConfig();
    }

    public SymbolListBean getSymbolListConfig(){
        if (mSymbolListConfig == null){
            getSymBolConfigFromFile();
        }
        return mSymbolListConfig;
    }

    public SymbolListBean.Symbol getSymbolConfig(NewCurrency currency){
        SymbolListBean symbolListBean = getSymbolListConfig();
        for (int i=0;i<symbolListBean.getSymbolList().size();i++){
            SymbolListBean.Symbol symbol = symbolListBean.getSymbolList().get(i);
            if (symbol.getMark().equals(currency.getSymbol())){
                return symbol;
            }
        }
        return null;
    }

    public void setBaseInfo(BannerInfo mBaseInfo) {
        this.mBaseInfo = mBaseInfo;
    }

    public BannerInfo getBaseInfo() {
        return mBaseInfo;
    }

    public int getSymbolSize(String symBolName){
        SymbolListBean symbolListBean = getSymbolListConfig();
        if (symbolListBean == null)
            return 2;
        for (int i=0;i<symbolListBean.getSymbolList().size();i++){
            SymbolListBean.Symbol symbol = symbolListBean.getSymbolList().get(i);
            if (symbol.getMark().equals(symBolName)){
                return GlobalConstant.getFloatSize(symbol);
            }
        }
        return 2;
    }

    public void addTcpEntityWithStatus(TcpEntity tcpEntity, String status){
        int exit = -1;
        for (int i=0;i<mTcpEntity.size();i++){
            TcpEntity local = mTcpEntity.get(i);
            if (local.getTcpKey().equals(tcpEntity.getTcpKey())){
                exit = i;
                local.setStatus(status);
                break;
            }
        }
        if (exit == -1){
            tcpEntity.setStatus(status);
            mTcpEntity.add(tcpEntity);
        }
    }

    public void delTcpEntityWithStatus(TcpEntity tcpEntity){
        for (int i=0;i<mTcpEntity.size();i++){
            TcpEntity local = mTcpEntity.get(i);
            if (local.getTcpKey().equals(tcpEntity.getTcpKey())){
                mTcpEntity.remove(i);
            }
        }
    }

    public void startBaseTcp(){
        //首页货币信息推送
        EventBus.getDefault().post(new SocketMessage(0, NEWCMD.SUBSCRIBE_HOME_TRADE,
                buildGetBodyJson("market.overview3", "1").toString())); // 需要id
        //合约推送
        EventBus.getDefault().post(new SocketMessage(0, NEWCMD.SUBSCRIBE_SIDE_TRADE,
                buildGetBodyJson("market.overview", "1").toString())); // 需要id

        EventBus.getDefault().post(new SocketMessage(0, NEWCMD.SUBSCRIBE_HOME_PEOPLE,
                buildGetBodyJson("count", "1").toString())); // 需要id
    }

    public void stopBaseTcp(){
        //首页货币信息推送
        EventBus.getDefault().post(new SocketMessage(0, NEWCMD.SUBSCRIBE_HOME_TRADE,
                buildGetBodyJson("market.overview3", "0").toString())); // 需要id
        //合约推送
        EventBus.getDefault().post(new SocketMessage(0, NEWCMD.SUBSCRIBE_SIDE_TRADE,
                buildGetBodyJson("market.overview", "0").toString())); // 需要id

        EventBus.getDefault().post(new SocketMessage(0, NEWCMD.SUBSCRIBE_HOME_PEOPLE,
                buildGetBodyJson("count", "0").toString())); // 需要id
    }


    public void startTcp(TcpEntity tcpEntity){
        addTcpEntityWithStatus(tcpEntity,"1");
        EventBus.getDefault().post(new SocketMessage(0, tcpEntity.getNewcmd(),
                buildGetBodyJson(tcpEntity.getTcpKey(), "1").toString())); //
    }

    public void stopTcp(TcpEntity tcpEntity){
        delTcpEntityWithStatus(tcpEntity);
        EventBus.getDefault().post(new SocketMessage(0, tcpEntity.getNewcmd(),
                buildGetBodyJson(tcpEntity.getTcpKey(), "0").toString())); //
    }


    public void delSomeTcp(){
        for (int i=0;i<mTcpEntity.size();i++){
            TcpEntity local = mTcpEntity.get(i);
            EventBus.getDefault().post(new SocketMessage(0, local.getNewcmd(),
                    buildGetBodyJson(local.getTcpKey(), "0").toString())); //
        }
        mTcpEntity.clear();
    }

    public void stopAlltcp(){
        for (int i=0;i<mTcpEntity.size();i++){
            TcpEntity local = mTcpEntity.get(i);
            EventBus.getDefault().post(new SocketMessage(0, local.getNewcmd(),
                    buildGetBodyJson(local.getTcpKey(), "0").toString())); //
        }
    }

    public void sendTcp(){
        for (int i=0;i<mTcpEntity.size();i++){
            TcpEntity local = mTcpEntity.get(i);
            EventBus.getDefault().post(new SocketMessage(0, local.getNewcmd(),
                    buildGetBodyJson(local.getTcpKey(), "1").toString())); //
        }
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

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        //static 代码段可以防止内存泄露


        new Thread(new Runnable() {
            @Override
            public void run() {
                initView();
                getDisplayMetric();
                getCurrentUserFromFile();
                LitePal.initialize(MyApplication.getApp());
                ZXingLibrary.initDisplayOpinion(MyApplication.getApp());
                x.Ext.init(MyApplication.getApp());
            }
        }).start();

        UMShareAPI.get(this);//初始化sdk
        //开启debug模式，方便定位错误，具体错误检查方式可以查看http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
        Config.DEBUG = true;


        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
                if (background || flag) {
                    background = false;
                    flag = false;
                    sAppState = STATE_BACK_TO_FRONT;
                    backToFrontTime = System.currentTimeMillis();
                    Log.e(TAG, "onResume: STATE_BACK_TO_FRONT");
                    startService(new Intent(activity, MyTextService.class));
                    if (needWakeWebSock()) {
                        getCurrenTcpFromFile();
                        resumeTcp();
                    }
                } else {
                    sAppState = STATE_NORMAL;
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                //判断当前activity是否处于前台
                if (!isCurAppTop()) {
                    // 从前台进入后台
                    sAppState = STATE_FRONT_TO_BACK;
                    frontToBackTime = System.currentTimeMillis();
                    flag = true;
                    Log.e(TAG, "onStop: " + "STATE_FRONT_TO_BACK");

                    stopService(new Intent(activity, MyTextService.class));
                    saveCurrentTcp();
                    stopAllChildTcp();
                } else {
                    // 否则是正常状态
                    sAppState = STATE_NORMAL;
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }

    private void stopAllChildTcp() {
        stopBaseTcp();
        stopAlltcp();
    }

    public void resumeTcp(){
        startBaseTcp();
        sendTcp();
    }

    public static boolean needWakeWebSock() {
        return sAppState == STATE_BACK_TO_FRONT;
    }
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        // TRIM_MEMORY_UI_HIDDEN是UI不可见的回调, 通常程序进入后台后都会触发此回调,大部分手机多是回调这个参数
        // TRIM_MEMORY_BACKGROUND也是程序进入后台的回调, 不同厂商不太一样, 魅族手机就是回调这个参数
        if (level == Application.TRIM_MEMORY_UI_HIDDEN || level == TRIM_MEMORY_BACKGROUND) {
            background = true;
        } else if (level == Application.TRIM_MEMORY_COMPLETE) {
            background = !isCurAppTop();
        }
        if (background) {
            frontToBackTime = System.currentTimeMillis();
            sAppState = STATE_FRONT_TO_BACK;
        } else {
            sAppState = STATE_NORMAL;
        }

    }

    public boolean isCurAppTop() {
        String curPackageName = getPackageName();
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ActivityManager.RunningTaskInfo info = list.get(0);
            String topPackageName = info.topActivity.getPackageName();
            String basePackageName = info.baseActivity.getPackageName();
            if (topPackageName.equals(curPackageName) && basePackageName.equals(curPackageName)) {
                return true;
            }
        }
        return false;
    }

    private void initView() {
        tvToast = (TextView) View.inflate(app, R.layout.my_toast, null);



    }

    public boolean isLogin() {
        if (getCurrentUser() == null) return false;
        return !StringUtils.isEmpty(getCurrentUser().getToken());
    }

    /**
     * 获取屏幕的宽高
     */
    private void getDisplayMetric() {
        mWidth = getResources().getDisplayMetrics().widthPixels;
        mHeight = getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取程序的Application对象
     */
    public static MyApplication getApp() {
        return app;
    }

    /**
     * 重新登录
     */
    public void loginAgain(BaseActivity activity) {
        deleteCurrentUser();
        activity.startActivityForResult(new Intent(activity, LoginStepOneActivity.class), LoginStepOneActivity.RETURN_LOGIN);
        //10.19改动
        if (StringUtils.isNotEmpty(activity.getClass().toString()) && activity.getClass().toString().equals(MainActivity.class.toString())) {
            return;
        }
        activity.finish();
    }

    /**
     * 重新登录
     */
    public void loginAgain(Fragment fragment) {
        Activity activity = fragment.getActivity();
        deleteCurrentUser();
        fragment.startActivityForResult(new Intent(fragment.getActivity(), LoginStepOneActivity.class), LoginStepOneActivity.RETURN_LOGIN);
        if (StringUtils.isNotEmpty(activity.getClass().toString()) && activity.getClass().toString().equals(MainActivity.class.toString())) {
            return;
        }
        activity.finish();
    }

    public synchronized void saveSymbolListConfig() {
        try {
            File file = FileUtils.getLongSaveFile(this, "SymBolConfig", GlobalConstant.SymbolFileName);
            if (file.exists()) {
                file.delete();
            }
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(mSymbolListConfig);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void saveCurrentUser() {
        try {
            File file = FileUtils.getLongSaveFile(this, "User", GlobalConstant.UserSaveFileName);
            if (file.exists()) {
                file.delete();
            }
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(currentUser);
            LogUtils.i("save token==" + currentUser.getToken());
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void saveCurrentTcp() {
        try {
            File file = FileUtils.getLongSaveFile(this, "WebSocket", GlobalConstant.WebSocketFileName);
            if (file.exists()) {
                file.delete();
            }
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            TcpEntity[] tcpEntities = new TcpEntity[mTcpEntity.size()];
            mTcpEntity.toArray(tcpEntities);
            oos.writeObject(tcpEntities);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 删除本地存储的文件
     */
    public void deleteCurrentTcp() {
        this.mTcpEntity = null;
        File file = FileUtils.getLongSaveFile(this, "WebSocket", GlobalConstant.WebSocketFileName);
        if (file != null && file.exists()) {
            file.delete();
        }
    }

    /**
     * 删除本地存储的文件
     */
    public void deleteCurrentUser() {
        this.currentUser = null;
        File file = FileUtils.getLongSaveFile(this, "User", GlobalConstant.UserSaveFileName);
        if (file != null && file.exists()) {
            file.delete();
        }
    }

    public void deleteSymbolConfig() {
        this.currentUser = null;
        File file = FileUtils.getLongSaveFile(this, "SymBolConfig", GlobalConstant.SymbolFileName);
        if (file != null && file.exists()) {
            file.delete();
        }
    }

    public void getCurrenTcpFromFile() {
        try {
            File file = new File(FileUtils.getLongSaveDir(this, "WebSocket"), GlobalConstant.WebSocketFileName);
            if (file != null && file.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                TcpEntity[] tcpEntities = (TcpEntity[]) ois.readObject();
                List<TcpEntity> tcpEntityList = Arrays.asList(tcpEntities);
                this.mTcpEntity = new ArrayList<>(tcpEntityList);
                if (this.mTcpEntity == null) {
                    this.mTcpEntity = new ArrayList<>();
                }
                ois.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void getSymBolConfigFromFile() {
        try {
            File file = new File(FileUtils.getLongSaveDir(this, "SymBolConfig"), GlobalConstant.SymbolFileName);
            if (file != null && file.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                this.mSymbolListConfig = (SymbolListBean) ois.readObject();
                if (this.mSymbolListConfig == null) {
                    this.mSymbolListConfig = new SymbolListBean();
                }
                ois.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void getCurrentUserFromFile() {
        try {
            File file = new File(FileUtils.getLongSaveDir(this, "User"), GlobalConstant.UserSaveFileName);
            if (file != null && file.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                this.currentUser = (User) ois.readObject();
                if (this.currentUser == null) {
                    this.currentUser = new User();
                }
                ois.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public int getmWidth() {
        return mWidth;
    }
    public int getmHeight() {
        return mHeight;
    }



    public User getCurrentUser() {
        return currentUser == null ? currentUser = new User() : currentUser;
    }

    public synchronized void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        saveCurrentUser();
    }

    public synchronized void updateCurrentUser(User currentUser){
        User localUser = getCurrentUser();

        
    }

    public boolean isLoginStatusChange() {
        return isLoginStatusChange;
    }

    public void setLoginStatusChange(boolean loginStatusChange) {
        isLoginStatusChange = loginStatusChange;
    }

    {
        PlatformConfig.setWeixin("wx9f595f5316941e1f", "490c07ebfb2a50c3eabb625123bda6a8");
//        PlatformConfig.setWeixin("wx069b07a808e5b1de", "cd658bc42cad59911cbe099175420879");//lxd
    }
}
