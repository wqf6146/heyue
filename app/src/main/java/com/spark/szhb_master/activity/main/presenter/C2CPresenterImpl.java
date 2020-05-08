package com.spark.szhb_master.activity.main.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.activity.main.MainContract;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.C2cConfig;
import com.spark.szhb_master.entity.CoinInfo;
import com.spark.szhb_master.entity.Message;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2018/2/24.
 */

public class C2CPresenterImpl implements MainContract.C2CPresenter {
    private MainContract.C2CView view;
    private DataSource dataRepository;

    public C2CPresenterImpl(DataSource dataRepository, MainContract.C2CView view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }

    @Override
    public void getC2cConfig() {
        dataRepository.doStringGet(UrlFactory.getC2cConfigUrl(),  new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 1) {
                        C2cConfig c2cConfig = new Gson().fromJson(object.getJSONObject("data").toString(), C2cConfig.class);
                        view.getC2cConfigSuccess(c2cConfig);
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
