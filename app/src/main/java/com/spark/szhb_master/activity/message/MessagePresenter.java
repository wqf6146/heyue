package com.spark.szhb_master.activity.message;


import com.google.gson.Gson;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.MessageBean;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2017/9/25.
 */

public class MessagePresenter implements MessageContract.Presenter {
    private final DataSource dataRepository;
    private final MessageContract.View view;

    public MessagePresenter(DataSource dataRepository, MessageContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void message(HashMap params) {
        dataRepository.doStringGet(UrlFactory.getMessageUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 1) {
                        MessageBean bean = new Gson().fromJson(object.optString("data"), MessageBean.class);
                        view.messageSuccess(bean);
                    } else {
                        view.messageFail(object.getInt("code"), object.optString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.messageFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.messageFail(code, toastMessage);
            }
        });
    }


}
