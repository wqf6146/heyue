package com.spark.szhb_master.activity.freshgift;


import com.spark.szhb_master.activity.feed.FeedBackContract;
import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.FreshGitBean;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface FreshGiftContract {

    interface View extends Contract.BaseView<Presenter> {

        void getGiftListSuccess(FreshGitBean obj);

        void getGiftListFail(Integer code, String toastMessage);

        void doGetGiftSuccess(String obj);

        void doGetGiftFail(Integer code, String toastMessage);
    }

    interface Presenter extends Contract.BasePresenter {

        void getGiftList();

        void doGetGift(HashMap hashMap);
    }
}
