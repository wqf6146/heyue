package com.spark.szhb_master.activity.main.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.activity.main.MainContract;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.BannerInfo;
import com.spark.szhb_master.entity.MessageBean;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

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
    public void banners(HashMap<String, String> map) {
//        view.displayLoadingPopup();
        dataRepository.doStringGet(UrlFactory.getBannersUrl(), map, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 1) {
                        BannerInfo bannerInfo = new Gson().fromJson(object.getJSONObject("data").toString(), BannerInfo.class);
                        view.bannersSuccess(bannerInfo);
                    } else {
                        view.bannersFail(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.bannersFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.bannersFail(code, toastMessage);
            }
        });
    }

    @Override
    public void getMarqueeText(HashMap<String, String> map) {
//        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getMessageUrl(), map, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<MessageBean.Message> messages = new Gson().fromJson(object.getJSONObject("data").getJSONArray("content").toString(), new TypeToken<List<MessageBean.Message>>() {
                        }.getType());
                        view.getMarqueeSuccess(messages);
                    } else {
                        view.getMarqueeFail(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.getMarqueeFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.getMarqueeFail(code, toastMessage);
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
