package com.spark.szhb_master.activity.mycc;

import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.AssetsInfo;
import com.spark.szhb_master.entity.ContratInfo;
import com.spark.szhb_master.entity.NewEntrust;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.entity.TradeCOM;

import java.util.HashMap;

/**
 * author: wuzongjie
 * time  : 2018/4/17 0017 19:12
 * desc  :
 */

public interface MyccContract {
    interface View extends Contract.BaseView<Presenter> {
        void doPostFail(int e, String meg);
        void getWalletSuccess(AssetsInfo assetsInfo);
    }

    interface DqwtPresenter{
        void undoLimitContrat(HashMap hashMap);
        void getCurrentEntrust(HashMap params); // 获取当前的委托
    }

    interface DqwtView extends Contract.BaseView<MyccContract.DqwtPresenter> {
        void doPostFail(int e, String meg);
        void getCurrentEntrustSuccess(NewEntrust entrustEntity);
        void undoLimitContratSuccess(String msg);
    }

    interface DqccPresenter{
        void getCurrentHave(HashMap params); // 获取当前的持仓
        void commitUndersellContrat(HashMap params);
    }

    interface DqccView extends Contract.BaseView<MyccContract.DqccPresenter> {
        void doPostFail(int e, String meg);
        void getCurrentHaveSuccess(NewEntrust entrustEntity);
        void undersellContratSuccess(String msg);

    }

    interface Presenter {
        void getWallet(); // 获取钱包
    }
}
