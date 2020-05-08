package com.spark.szhb_master.activity.entrust;

import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.MarketSymbol;
import com.spark.szhb_master.entity.NewEntrust;
import com.spark.szhb_master.entity.SymbolListBean;

import java.util.HashMap;
import java.util.List;

/**
 * author: wuzongjie
 * time  : 2018/4/18 0018 11:21
 * desc  :
 */

public class ITrustContract {
    interface View extends Contract.BaseView<ITrustContract.Presenter> {

        void doPostFail(int e, String meg);

        void getCurrentOrderSuccess(List<NewEntrust.ListBean> objs);
        void getSymbolSucccess(List<MarketSymbol> objs);
        void getCurrentEntrustSuccess(NewEntrust newEntrust);
        void cancelOrderSuccess(String message);
        void getSymbolHistroyListSuccess(NewEntrust newEntrust);
        void getSymbolListSuccess(SymbolListBean symbolListBean);
        void undoLimitContratSuccess(String msg);
    }

    interface Presenter extends Contract.BasePresenter {
        void getCurrentOrder(HashMap<String, String> params);
        void getCurrentEntrust(HashMap hashMap);
        void getSymbolHistroyList(HashMap hashMap);
        void cancelOrder(String orderId);
        void getSymbol();
        void undoLimitContrat(HashMap hashMap);
        void getSymbolList();

    }
}
