package com.spark.szhb_master.activity.Bind_bank;


import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.BankEntity;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface BindBankContract {
    interface View extends Contract.BaseView<Presenter> {

        void submitSuccess(String obj);
        void doPostFail(Integer code, String toastMessage);
        void sendCodeSuccess(String obj);
    }

    interface ListView extends Contract.BaseView<ListPresenter> {
        void getBankListSuccess(BankEntity bankEntity);
        void doPostFail(Integer code, String toastMessage);
        void changeBankSuccess(String obj);
        void deleteBankSuccess(String obj);
    }

    interface Presenter extends Contract.BasePresenter {
        void submit(HashMap params);
        void sendCode();
    }
    interface ListPresenter extends Contract.BasePresenter {
        void getList();
        void changeBank(HashMap hashMap);
        void deleteBank(HashMap hashMap);
    }



}
