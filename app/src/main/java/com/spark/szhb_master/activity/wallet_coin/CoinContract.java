package com.spark.szhb_master.activity.wallet_coin;


import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.AssetsInfo;
import com.spark.szhb_master.entity.ContratInfo;
import com.spark.szhb_master.entity.ExtractInfo;
import com.spark.szhb_master.entity.RechargeAddress;
import com.spark.szhb_master.entity.WalletThreeBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface CoinContract {

    interface fundtransferView extends Contract.BaseView<fundtransferPresenter> {
        void fundtransferSuccess(String obj);
        void fundtransferFaild(Integer code,String obj);
        void getWalletSuccess(AssetsInfo assetsInfo);
        void doPostFail(Integer code, String toastMessage);
    }

    interface extractView extends Contract.BaseView<extractPresenter>{
        void extractSuccess(String obj);
        void doPostFail(Integer code, String toastMessage);
    }

    interface rechargeView extends Contract.BaseView<rechargePresenter>{
        void getRechargeAddressSuccess(RechargeAddress rechargeAddress);
        void doPostFail(Integer code, String toastMessage);
    }

    interface recordeListView extends Contract.BaseView<recordeListPresenter>{
        void getListSuccess(WalletThreeBean walletThreeBean);
        void doPostFail(Integer code, String toastMessage);
    }

    interface extractPresenter extends Contract.BasePresenter {
        void extract(HashMap params);
    }

    interface rechargePresenter extends Contract.BasePresenter {
        void getRechargeAddress();
    }

    interface fundtransferPresenter extends Contract.BasePresenter {
        void fundtransfer(HashMap hashMap);
        void getWallet();
    }

    interface recordeListPresenter extends Contract.BasePresenter {
        void getList(String url,HashMap hashMap);
    }
}
