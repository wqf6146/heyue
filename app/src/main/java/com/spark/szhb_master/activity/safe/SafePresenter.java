package com.spark.szhb_master.activity.safe;


import com.spark.szhb_master.activity.account_pwd.AccountPwdContract;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2017/9/25.
 */

public class SafePresenter implements SafeContract.Presenter {
    private final DataSource dataRepository;
    private final SafeContract.View view;

    public SafePresenter(DataSource dataRepository, SafeContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void updateZjmmPwd(HashMap params) {
        view.displayLoadingPopup();
        dataRepository.doStringPut(UrlFactory.updateZjmm(), params,new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    int code = object.optInt("code");
                    if (code == 1) {
                        view.updateZjmmPwdSuccess(object.optString("msg"));
                    } else {
                        view.doPostFail(object.optInt("code"), object.optString("msg"));
                    }
                } catch (Exception e) {
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
    public void sendCode() {
        view.displayLoadingPopup();
        dataRepository.doStringGet(UrlFactory.sendSMS(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    int code = object.optInt("code");
                    if (code == 1) {
                        view.sendCodeSuccess(object.optString("msg"));
                    } else {
                        view.doPostFail(object.optInt("code"), object.optString("msg"));
                    }
                } catch (Exception e) {
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
