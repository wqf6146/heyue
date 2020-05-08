package com.spark.szhb_master.activity.main.presenter;

import com.google.gson.Gson;
import com.spark.szhb_master.activity.main.MainContract;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.C2cConfig;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2018/2/24.
 */

public class C2cBuyPresenterImpl implements MainContract.BuyPresenter {
    private MainContract.BuyView view;
    private DataSource dataRepository;

    public C2cBuyPresenterImpl(DataSource dataRepository, MainContract.BuyView view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }


    @Override
    public void optBuy(HashMap hashMap) {
        dataRepository.doStringPostJson(UrlFactory.getOptBuyOrSellUrl(),hashMap,  new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 1) {
                        view.optBuySuccess(object.optString("msg"));
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

    @Override
    public void fastBuy(HashMap hashMap) {
        dataRepository.doStringPostJson(UrlFactory.getFastBuyOrSellUrl(),hashMap,  new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 1) {
                        view.fastBuySuccess(object.optString("msg"));
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
