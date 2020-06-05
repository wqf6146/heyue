package com.spark.szhb_master.activity.mygift;


import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.C2cOrder;
import com.spark.szhb_master.entity.C2cOrderDetail;
import com.spark.szhb_master.entity.FreshGitBean;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface MyGiftContract {
    interface View extends Contract.BaseView<Presenter> {

        void getListSuccess(FreshGitBean giftBean);
        void getListFaild(Integer code, String obj);
        void doPostFail(Integer code, String toastMessage);

        void doUseGiftSuccess(String msg);
        void doUseGiftFaild(Integer code, String obj);
    }


    interface Presenter extends Contract.BasePresenter {
        void getList(HashMap params);
        void doUseGift(HashMap hashMap);
    }

}
