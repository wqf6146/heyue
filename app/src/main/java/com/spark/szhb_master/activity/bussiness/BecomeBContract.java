package com.spark.szhb_master.activity.bussiness;


import com.spark.szhb_master.base.Contract;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface BecomeBContract {
    interface View extends Contract.BaseView<Presenter> {

        void submitSuccess(String obj);
        void doPostFail(Integer code, String toastMessage);
    }

    interface Presenter extends Contract.BasePresenter {
        void submit(HashMap<String, String> params);
    }


}
