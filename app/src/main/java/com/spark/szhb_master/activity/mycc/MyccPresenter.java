package com.spark.szhb_master.activity.mycc;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.AssetsInfo;
import com.spark.szhb_master.entity.ContratInfo;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.entity.SymbolInfo;
import com.spark.szhb_master.entity.TradeCOM;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;
import static com.spark.szhb_master.utils.GlobalConstant.OKHTTP_ERROR;

/**
 * author: wuzongjie
 * time  : 2018/4/17 0017 19:14
 * desc  :
 */

public class MyccPresenter implements MyccContract.Presenter {
    private MyccContract.View view;
    private DataSource dataRepository;

    public MyccPresenter(DataSource dataRepository, MyccContract.View view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }


    @Override
    public void getWallet() {
        dataRepository.doStringGet(UrlFactory.getWalletUrl(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 1) {
                        AssetsInfo assetsInfo = new Gson().fromJson(object.getJSONObject("data").toString(), AssetsInfo.class);
                        view.getWalletSuccess(assetsInfo);
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
