package com.spark.szhb_master.activity.Trade;

import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.AssetsInfo;
import com.spark.szhb_master.entity.ContratInfo;
import com.spark.szhb_master.entity.Exchange;
import com.spark.szhb_master.entity.Money;
import com.spark.szhb_master.entity.NewEntrust;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.entity.TradeCOM;

import java.util.HashMap;
import java.util.List;

/**
 * author: wuzongjie
 * time  : 2018/4/17 0017 19:12
 * desc  :
 */

public interface TradeContract {
    interface View extends Contract.BaseView<Presenter> {
        void doPostFail(int e, String meg);

        void getExchangeSuccess(TradeCOM tradeCOM);

        void getContratInfoSuccess(ContratInfo info);

        void commitLimitOrderSuccess(String message);

        void commitMarketOrderSuccess(String message);

        void cancelSuccess(String message);

        void getWalletSuccess(AssetsInfo assetsInfo);

        void getSymbolInfoResult(int one, int two);

        void addCollectSuccess(String message);

        void deleteCollectSuccess(String message);

        void safeSettingSuccess(SafeSetting obj);

        void commitLiquidationSuccess(String msg);


    }

    interface DqwtPresenter{
        void undoLimitContrat(HashMap hashMap);
        void getCurrentEntrust(HashMap params); // 获取当前的委托
    }

    interface DqwtView extends Contract.BaseView<TradeContract.DqwtPresenter> {
        void doPostFail(int e, String meg);
        void getCurrentEntrustSuccess(NewEntrust entrustEntity);
        void undoLimitContratSuccess(String msg);
    }

    interface DqccPresenter{
        void getCurrentHave(HashMap params); // 获取当前的持仓
        void commitUndersellContrat(HashMap params);
    }

    interface DqccView extends Contract.BaseView<TradeContract.DqccPresenter> {
        void doPostFail(int e, String meg);
        void getCurrentHaveSuccess(NewEntrust entrustEntity);
        void undersellContratSuccess(String msg);

    }

    interface Presenter {

        void commitLiquidation();//一键平仓

        void getContratInfo(HashMap params); //获取合约交易对配置

        void getExchange(HashMap params); // 获取个人挂单手数

        void commitLimitOrder(HashMap hashMap); //提交限价委托

        void commitMarketOrder(HashMap hashMap); //提交市价委托

        void cancelEntrust(String orderId); // 取消委托

        void getWallet(); // 获取钱包

        void getSymbolInfo(HashMap<String, String> params); // 获取精确度

        void addCollect(HashMap<String, String> params); // 添加收藏

        void deleteCollect(HashMap<String, String> params); // 删除收藏

        void safeSetting();
    }
}
