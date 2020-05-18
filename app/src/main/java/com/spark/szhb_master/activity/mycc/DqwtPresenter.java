package com.spark.szhb_master.activity.mycc;

import com.google.gson.Gson;
import com.spark.szhb_master.activity.Trade.TradeContract;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.NewEntrust;
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

public class DqwtPresenter implements MyccContract.DqwtPresenter {
    private MyccContract.DqwtView view;
    private DataSource dataRepository;

    public DqwtPresenter(DataSource dataRepository, MyccContract.DqwtView view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }

    @Override
    public void undoLimitContrat(HashMap hashMap) {
        dataRepository.doStringPut(UrlFactory.getUndoContratUrl(),hashMap, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 1) {
                        view.undoLimitContratSuccess(object.optString("data"));
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
                view.doPostFail(OKHTTP_ERROR, null);
            }
        });
    }

    @Override
    public void getCurrentEntrust(HashMap params) {
//        view.displayLoadingPopup();
        dataRepository.doStringGet(UrlFactory.getEntrustUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                String response = (String) obj;

                try {
                    JSONObject object = new JSONObject(response);

                    if (object.optInt("code") == 1) {
                        NewEntrust objs = new Gson().fromJson(object.getString("data").toString(), NewEntrust.class);
                        view.getCurrentEntrustSuccess(objs);
                    } else {
                        view.doPostFail(object.getInt("code"), object.optString("msg"));
                    }
                    view.hideLoadingPopup();
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.doPostFail(JSON_ERROR, null);
                    view.hideLoadingPopup();
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.doPostFail(OKHTTP_ERROR, null);
            }
        });
    }

}
