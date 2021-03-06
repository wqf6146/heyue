package com.spark.szhb_master.activity.main.presenter;

import com.google.gson.Gson;
import com.spark.szhb_master.activity.main.MainContract;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.BaseInfo;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2018/2/24.
 */

public class HomePresenterImpl implements MainContract.HomePresenter {
    private MainContract.HomeView view;
    private DataSource dataRepository;

    public HomePresenterImpl(DataSource dataRepository, MainContract.HomeView view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }


    @Override
    public void getBaseInfo() {
//        view.displayLoadingPopup();
        dataRepository.doStringGet(UrlFactory.getBaseInfoUrl(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 1) {
                        BaseInfo baseInfo = new Gson().fromJson(object.getJSONObject("data").toString(), BaseInfo.class);
                        view.getBaseInfoSuccess(baseInfo);
                    } else {
                        view.getBaseInfoFail(object.getInt("code"), object.optString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.getBaseInfoFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.getBaseInfoFail(code, toastMessage);
            }
        });
    }

    @Override
    public void safeSetting() {
//        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getSafeSettingUrl(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        SafeSetting safeSetting = new Gson().fromJson(object.getJSONObject("data").toString(), SafeSetting.class);
                        view.safeSettingSuccess(safeSetting);
                    } else {
                        view.safeSettingFiled(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.safeSettingFiled(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.safeSettingFiled(JSON_ERROR, null);
            }
        });
    }
}
