package com.spark.szhb_master.activity.wallet_coin;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.AssetsInfo;
import com.spark.szhb_master.entity.Coin;
import com.spark.szhb_master.entity.RechargeAddress;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2017/9/25.
 */

public class FundtransferPresenter implements CoinContract.fundtransferPresenter {
    private final DataSource dataRepository;
    private final CoinContract.fundtransferView view;

    public FundtransferPresenter(DataSource dataRepository, CoinContract.fundtransferView view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void getWallet() {
        dataRepository.doStringGet(UrlFactory.getWalletUrl(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 1) {
                        AssetsInfo assetsInfo = new Gson().fromJson(object.getJSONObject("data").toString(), AssetsInfo.class);
                        view.getWalletSuccess(assetsInfo);
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
                view.doPostFail(code, toastMessage);
            }
        });
    }

    @Override
    public void fundtransfer(HashMap hashMap) {
        view.displayLoadingPopup();
        dataRepository.doStringPostJson(UrlFactory.getTransferUrl(), hashMap,new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 1) {
                        view.fundtransferSuccess(object.optString("msg"));
                    } else {
                        view.fundtransferFaild(object.getInt("code"), object.optString("msg"));
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
