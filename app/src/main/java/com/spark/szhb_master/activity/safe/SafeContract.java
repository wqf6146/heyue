package com.spark.szhb_master.activity.safe;


import com.spark.szhb_master.base.Contract;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface SafeContract {
    interface View extends Contract.BaseView<Presenter> {

        void updateZjmmPwdSuccess(String obj);

        void sendCodeSuccess(String obj);

        void doPostFail(Integer code, String toastMessage);
    }

    interface Presenter extends Contract.BasePresenter {
        void updateZjmmPwd(HashMap params);
        void sendCode();
    }
}
