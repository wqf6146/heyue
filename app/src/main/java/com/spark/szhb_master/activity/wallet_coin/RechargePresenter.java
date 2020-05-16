package com.spark.szhb_master.activity.wallet_coin;


import com.google.gson.Gson;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.RechargeAddress;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2017/9/25.
 */

public class RechargePresenter implements CoinContract.rechargePresenter {
    private final DataSource dataRepository;
    private final CoinContract.rechargeView view;

    public RechargePresenter(DataSource dataRepository, CoinContract.rechargeView view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void getRechargeAddress() {
        view.displayLoadingPopup();
        dataRepository.doStringGet(UrlFactory.getRechargeAddressUrl(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 1) {
                        RechargeAddress objs = new Gson().fromJson(object.get("data").toString(), RechargeAddress.class);
                        view.getRechargeAddressSuccess(objs);
                    } else {
                        view.doPostFail(object.getInt("code"), object.optString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.doPostFail(JSON_ERROR, null);
                }

            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.doPostFail(code, toastMessage);

            }
        });
    }
}
