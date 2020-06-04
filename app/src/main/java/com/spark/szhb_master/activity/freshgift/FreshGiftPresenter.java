package com.spark.szhb_master.activity.freshgift;


import com.google.gson.Gson;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.FreshGitBean;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2017/9/25.
 */

public class FreshGiftPresenter implements FreshGiftContract.Presenter {
    private final DataSource dataRepository;
    private final FreshGiftContract.View view;

    public FreshGiftPresenter(DataSource dataRepository, FreshGiftContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void getGiftList() {

        dataRepository.doStringGet(UrlFactory.getFreshGiftUrl(),  new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 1) {
                        FreshGitBean objs = new Gson().fromJson(object.getString("data").toString(), FreshGitBean.class);
                        view.getGiftListSuccess(objs);
                    } else {
                        view.getGiftListFail(object.getInt("code"), object.optString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.getGiftListFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.getGiftListFail(code, toastMessage);
            }
        });
    }

    @Override
    public void doGetGift(HashMap params) {
        view.displayLoadingPopup();
        dataRepository.doStringPostJson(UrlFactory.getFreshGiftUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 1) {
                        view.doGetGiftSuccess(object.getString("msg"));
                    } else {
                        view.doGetGiftFail(object.getInt("code"), object.optString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.doGetGiftFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.doGetGiftFail(code, toastMessage);
            }
        });
    }

}
