package com.spark.szhb_master.activity.bussiness;


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

public class BecomeBPresenter implements BecomeBContract.Presenter {
    private final DataSource dataRepository;
    private final BecomeBContract.View view;

    public BecomeBPresenter(DataSource dataRepository, BecomeBContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void submit(HashMap<String, String> params) {
        view.displayLoadingPopup();
        dataRepository.doStringPostJson(UrlFactory.getBecomeBUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 1) {
                        view.submitSuccess(object.optString("msg"));
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
