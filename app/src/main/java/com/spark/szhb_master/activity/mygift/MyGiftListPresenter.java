package com.spark.szhb_master.activity.mygift;

import com.google.gson.Gson;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.C2cOrder;
import com.spark.szhb_master.entity.FreshGitBean;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2018/2/28.
 */

public class MyGiftListPresenter implements MyGiftContract.Presenter {
    private MyGiftContract.View view;
    private DataSource dataRepository;

    public MyGiftListPresenter(DataSource dataRepository, MyGiftContract.View view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }


    @Override
    public void getList(HashMap hashMap) {
        dataRepository.doStringGet(UrlFactory.getMyGiftUrl(), hashMap, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 1) {
                        FreshGitBean freshGitBean = new Gson().fromJson(object.getJSONObject("data").toString(), FreshGitBean.class);
                        view.getListSuccess(freshGitBean);
                    } else {
                        view.getListFaild(object.getInt("code"), object.optString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.doPostFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.doPostFail(JSON_ERROR, null);
            }
        });
    }

    @Override
    public void doUseGift(HashMap hashMap) {
        view.displayLoadingPopup();
        dataRepository.doStringPostJson(UrlFactory.getMyGiftUrl(), hashMap, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 1) {
                        view.doUseGiftSuccess(object.optString("msg"));
                    } else {
                        view.doUseGiftFaild(object.getInt("code"), object.optString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.doUseGiftFaild(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.doUseGiftFaild(JSON_ERROR, null);
            }
        });
    }

}
