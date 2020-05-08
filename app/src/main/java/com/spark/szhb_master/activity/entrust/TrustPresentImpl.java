package com.spark.szhb_master.activity.entrust;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.MarketSymbol;
import com.spark.szhb_master.entity.NewEntrust;
import com.spark.szhb_master.entity.SymbolListBean;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;
import static com.spark.szhb_master.utils.GlobalConstant.OKHTTP_ERROR;

/**
 * author: wuzongjie
 * time  : 2018/4/18 0018 11:22
 * desc  :
 */

public class TrustPresentImpl implements ITrustContract.Presenter {
    private ITrustContract.View view;
    private final DataSource dataRepository;

    public TrustPresentImpl(DataSource dataRepository, ITrustContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void getSymbolList() {
        dataRepository.doStringGet(UrlFactory.getSymbolListUrl(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.optInt("code") == 1) {
                        SymbolListBean objs = new Gson().fromJson(object.getString("data").toString(), SymbolListBean.class);
                        view.getSymbolListSuccess(objs);
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

    @Override
    public void undoLimitContrat(HashMap hashMap) {
        dataRepository.doStringPut(UrlFactory.getUndoContratUrl(),hashMap, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 1) {
                        view.undoLimitContratSuccess(object.optString("data"));
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
                view.doPostFail(OKHTTP_ERROR, null);
            }
        });
    }

    @Override
    public void getSymbolHistroyList(HashMap hashMap) {


        String url = UrlFactory.getHistroyEntrus() + "?mark=" + hashMap.get("mark") +
                "&leverage=" + hashMap.get("leverage") + "&page=" + hashMap.get("page") + "&page_size=" + hashMap.get("page_size") +
                "&type=" + hashMap.get("type");

        dataRepository.doStringGet(url, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.optInt("code") == 1) {
                        NewEntrust objs = new Gson().fromJson(object.getString("data").toString(), NewEntrust.class);
                        view.getSymbolHistroyListSuccess(objs);
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

    @Override
    public void getCurrentOrder(HashMap<String, String> params) {
        dataRepository.doStringGet(UrlFactory.getEntrustUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.optInt("code") == 1) {
                        NewEntrust objs = new Gson().fromJson(object.getString("data").toString(), NewEntrust.class);
                        view.getCurrentOrderSuccess(objs.getList());
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

    @Override
    public void getCurrentEntrust(HashMap hashMap) {
//        view.displayLoadingPopup();

        String url = UrlFactory.getEntrustUrl() + "?mark=" + hashMap.get("mark") +
                "&leverage=" + hashMap.get("leverage") + "&page=" + hashMap.get("page") + "&page_size=" + hashMap.get("page_size") +
                "&type=" + hashMap.get("type");


        dataRepository.doStringGet(url, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                String response = (String) obj;

                try {
                    JSONObject object = new JSONObject(response);

                    if (object.optInt("code") == 1) {
                        NewEntrust objs = new Gson().fromJson(object.getString("data").toString(), NewEntrust.class);
                        view.getCurrentEntrustSuccess(objs);
                    } else {
                        view.doPostFail(object.getInt("code"), object.optString("msg"));
                    }
                    view.hideLoadingPopup();
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.doPostFail(JSON_ERROR, null);
                    view.hideLoadingPopup();
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.doPostFail(OKHTTP_ERROR, null);
            }
        });
    }

    @Override
    public void cancelOrder(String orderId) {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getCancleEntrustUrl() + orderId, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        view.cancelOrderSuccess(object.optString("message"));
                    } else {
                        view.doPostFail(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.doPostFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.doPostFail(OKHTTP_ERROR, null);
            }
        });
    }

    @Override
    public void getSymbol() {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getSymbolUrl(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONArray array = new JSONArray(response);
                    if (array.length() != 0) {
                        List<MarketSymbol> objs = new Gson().fromJson(array.toString(), new TypeToken<List<MarketSymbol>>() {
                        }.getType());
                        view.getSymbolSucccess(objs);
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
