package com.spark.szhb_master.activity.message;


import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.MessageBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface MessageContract {
    interface View extends Contract.BaseView<Presenter> {

        void messageSuccess(MessageBean obj);

        void messageFail(Integer code, String toastMessage);
    }

    interface Presenter extends Contract.BasePresenter {

        void message(HashMap params);

    }
}
