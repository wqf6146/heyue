package com.spark.szhb_master.activity.delegate;


import com.spark.szhb_master.base.Contract;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface PostDelegateContract {
    interface View extends Contract.BaseView<Presenter> {

        void submitSuccess(String obj);
        void doPostFail(Integer code, String toastMessage);
    }

    interface Presenter extends Contract.BasePresenter {
        void submit(HashMap params);
    }


}
