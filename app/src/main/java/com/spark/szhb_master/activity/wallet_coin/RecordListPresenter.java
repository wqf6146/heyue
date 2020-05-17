package com.spark.szhb_master.activity.wallet_coin;


import com.google.gson.Gson;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.RechargeAddress;
import com.spark.szhb_master.entity.WalletThreeBean;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2017/9/25.
 */

public class RecordListPresenter implements CoinContract.recordeListPresenter {
    private final DataSource dataRepository;
    private final CoinContract.recordeListView view;

    public RecordListPresenter(DataSource dataRepository, CoinContract.recordeListView view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void getList(String url,HashMap hashMap) {
        dataRepository.doStringGet(url, hashMap,new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 1) {
                        WalletThreeBean objs = new Gson().fromJson(object.get("data").toString(), WalletThreeBean.class);
                        view.getListSuccess(objs);
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
                view.doPostFail(code, toastMessage);
            }
        });
    }
}
