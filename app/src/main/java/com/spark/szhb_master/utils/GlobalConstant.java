package com.spark.szhb_master.utils;

import com.spark.szhb_master.entity.SymbolListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/5.
 */

public class GlobalConstant {
    public static final boolean isDebug = true; // 是否代开log日志
    public static final boolean isOPenGoogle = false; // 是否打开谷歌验证
    public static final boolean isUpLoadFile = false; // 上传图片是否为文件上传

    //TOKEN失效错误码
    public static final int TOKEN_DISABLE1 = 401;
    public static final int TOKEN_DISABLE2 = -1;

    //自定义错误码
    public static final int JSON_ERROR = -9999;
    public static final int VOLLEY_ERROR = -9998;
    public static final int TOAST_MESSAGE = -9997;
    public static final int OKHTTP_ERROR = -9996;
    public static final int NO_DATA = -9995;

    ///////////////////permission
    public static final int PERMISSION_CONTACT = 0;
    public static final int PERMISSION_CAMERA = 1;
    public static final int PERMISSION_STORAGE = 2;

    //常用常量
    public static final int TAKE_PHOTO = 10;
    public static final int CHOOSE_ALBUM = 11;

    public static final int KEFU = 100;

    public static final int step7 = 5;
    public static final int step8 = 4;
    public static final int step9 = 3;
    public static final int step10 = 2;
    public static final int step11 = 1;

    public static int getFloatSize(SymbolListBean.Symbol symbol){
        String dp = symbol.getDepth_default();
        if (dp.equals("step7")){
            return step7;
        }else if (dp.equals("step8")){
            return step8;
        }else if (dp.equals("step9")){
            return step9;
        }else if (dp.equals("step10")){
            return step10;
        }else if (dp.equals("step11")){
            return step11;
        }
        return 0;
    }

    public static int getFloatSize(String symbol){
        if (symbol.equals("step7")){
            return step7;
        }else if (symbol.equals("step8")){
            return step8;
        }else if (symbol.equals("step9")){
            return step9;
        }else if (symbol.equals("step10")){
            return step10;
        }else if (symbol.equals("step11")){
            return step11;
        }
        return 0;
    }

    public static String getFloatShowStr(String symbol){
        if (symbol.equals("step7")){
            return "0.00001";
        }else if (symbol.equals("step8")){
            return "0.0001";
        }else if (symbol.equals("step9")){
            return "0.001";
        }else if (symbol.equals("step10")){
            return "0.01";
        }else if (symbol.equals("step11")){
            return "0.1";
        }
        return "0.01";
    }

    public static String getFloatStrName(String symbol){
        if (symbol.equals("0.00001")){
            return "step7";
        }else if (symbol.equals("0.0001")){
            return "step8";
        }else if (symbol.equals("0.001")){
            return "step9";
        }else if (symbol.equals("0.01")){
            return "step10";
        }else if (symbol.equals("0.1")){
            return "step11";
        }
        return "step10";
    }

    public static List<String> getFloatSizeList(String config[]){
        List<String> res = new ArrayList<>();
        for (int i=0 ;i<config.length;i++){
            res.add(getFloatShowStr(config[i]));
        }
        return res;
    }

    /**
     * k线图对应tag值
     */
    public static final int TAG_DIVIDE_TIME = 0; // 分时图
    public static final int TAG_ONE_MINUTE = 1; // 1分钟
    public static final int TAG_FIVE_MINUTE = 2; // 5分钟
    public static final int TAG_15_MINUTE = 2; // 15分钟
    public static final int TAG_30_MINUTE = 4; // 30分钟
    public static final int TAG_1_HOUR = 5; // 1小时
    public static final int TAG_4_HOUR = 6; // 4小时
    public static final int TAG_DAY = 7; // 1天
    public static final int TAG_MONTH = 8; // 1月

    /**
     * 应用该自定义常量
     */
    public static final String LIMIT_PRICE = "LIMIT_PRICE"; // 限价
    public static final String MARKET_PRICE = "MARKET_PRICE"; // 市价
    public static final String BUY = "buy"; // 买
    public static final String SELL = "sell"; // 卖
    public static final String CNY = "CNY"; // 人民币
    public static final String HK = "HK";
    public static final String USD = "USD";
    public static final String JPY = "JPY";
    public static final int PageSize = 10;
    public static final String UserSaveFileName = "user.info";
    public static final String SymbolFileName = "symbolconfig.info";
    public static final String WebSocketFileName = "websocket.info";
    public static final String LOGIN_LANGUAGE = "LOGIN_LANGUAGE";

    public static final String APP_ID = "wx9f595f5316941e1f";
//    public static final String APP_ID = "wx069b07a808e5b1de";

    public static String imageurl = "https://www.caymanex.pro/108.png";
    public static String share_title = "全民区块链挖宝";
    public static String share_description = "新用户免费挖宝，不花钱宝贝拿回家。快来注册，跟我一起挖宝吧！";

    public static String share_title_pro = "领衔全球的数字资产交易平台";
    public static String share_descriptionPro = "抢超级福利，新用户瓜分100000GCX，快来注册领取！";

    public static String share_des = "新用户免费挖宝，不花钱宝贝拿回家。";
    public static String rule_url = "https://caymanex.zendesk.com/hc/zh-cn/articles/360018828051";//挖宝规则

    public static String pro_url = "https://caymanex.zendesk.com/hc/zh-cn/articles/360018703272";//我的邀请规则

    public static String register_url = "https://caymanex.zendesk.com/hc/zh-cn/articles/360018788051";//用户协议链接
    public static String match_url = "https://caymanex.zendesk.com/hc/zh-cn/articles/360012630152";//配对规则链接


    public static String logo_frd = "https://www.caymanex.pro/share/logo-android.png";//邀请好友分享链接
    public static String logo_trea = "https://www.caymanex.pro/share/activity-android.png";//挖宝分享链接

}
