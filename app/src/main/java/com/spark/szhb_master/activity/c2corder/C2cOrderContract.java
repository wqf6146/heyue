package com.spark.szhb_master.activity.c2corder;


import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.BankEntity;
import com.spark.szhb_master.entity.C2cOrder;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface C2cOrderContract {
    interface View extends Contract.BaseView<Presenter> {

        void getListSuccess(C2cOrder c2cOrder);
        void getListFaild(Integer code,String obj);
        void doPostFail(Integer code, String toastMessage);
    }


    interface Presenter extends Contract.BasePresenter {
        void getList(HashMap params);
    }

}
