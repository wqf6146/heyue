package com.spark.szhb_master.activity.c2corder;

import com.google.gson.Gson;
import com.spark.szhb_master.activity.main.MainContract;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.C2C;
import com.spark.szhb_master.entity.C2cOrder;
import com.spark.szhb_master.entity.Fiats;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2018/2/28.
 */

public class C2COrderListPresenter implements C2cOrderContract.Presenter {
    private C2cOrderContract.View view;
    private DataSource dataRepository;

    public C2COrderListPresenter(DataSource dataRepository, C2cOrderContract.View view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }


    @Override
    public void getList(HashMap hashMap) {
        dataRepository.doStringGet(UrlFactory.getC2cHistroyOrderUrl(), hashMap, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 1) {
                        C2cOrder c2C = new Gson().fromJson(object.getJSONObject("data").toString(), C2cOrder.class);
                        view.getListSuccess(c2C);
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


}
