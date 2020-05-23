package com.spark.szhb_master.activity.main;

import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.AssetsInfo;
import com.spark.szhb_master.entity.BannerEntity;
import com.spark.szhb_master.entity.BannerInfo;
import com.spark.szhb_master.entity.C2C;
import com.spark.szhb_master.entity.C2cConfig;
import com.spark.szhb_master.entity.Coin;
import com.spark.szhb_master.entity.CoinInfo;
import com.spark.szhb_master.entity.Entrust;
import com.spark.szhb_master.entity.Favorite;
import com.spark.szhb_master.entity.Fiats;
import com.spark.szhb_master.entity.Message;
import com.spark.szhb_master.entity.Plate;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.entity.SymbolListBean;
import com.spark.szhb_master.entity.User;
import com.spark.szhb_master.entity.Vision;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/2/24.
 */

public interface MainContract {
    interface View extends Contract.BaseView<Presenter> {

        void allCurrencySuccess(Object obj);

        void allCurrencyFail(Integer code, String toastMessage);

        void homeCurrencySuccess(String obj);

        void homeCurrencyFail(Integer code, String toastMessage);

        void findSuccess(List<Favorite> obj);

        void findFail(Integer code, String toastMessage);

        void getNewVersionSuccess(Vision obj);

        void getNewVersionFail(Integer code, String toastMessage);

        void getRateSuccess(double obj);

        void getRateFail(Integer code, String toastMessage);

        void getCurrcyContractSuccess(SymbolListBean obj);

        void getCurrcyContractFail(Integer code, String toastMessage);
    }

    interface Presenter extends Contract.BasePresenter {

        void allCurrency();

        void homeCurrency();

        void getCurrcyContract();

        void find();

        void getNewVersion();

        void getRate();

    }


    interface HomeView extends Contract.BaseView<HomePresenter> {

        void bannersSuccess(BannerInfo bannerInfo);

        void bannersFail(Integer code, String toastMessage);

        void getMarqueeSuccess(List<Message> messages);

        void getMarqueeFail(Integer code, String toastMessage);

        void safeSettingFiled(Integer code, String toastMessage);

        void safeSettingSuccess(SafeSetting obj);

    }

    interface HomePresenter extends Contract.BasePresenter {

        void banners(HashMap<String, String> map);

        void getMarqueeText(HashMap<String, String> map);

        void safeSetting();
    }


    interface HeyuePresenter extends Contract.BasePresenter {

    }

    interface HeyueView extends Contract.BaseView<HeyuePresenter> {

    }

    interface MarketPresenter extends Contract.BasePresenter {

    }

    interface MarketView extends Contract.BaseView<MarketPresenter> {

    }

    interface ThreePresenter extends Contract.BasePresenter {

    }

    interface ThreeView extends Contract.BaseView<ThreePresenter> {
    }

    interface C2CPresenter extends Contract.BasePresenter {
        void getC2cConfig();
    }

    interface C2CView extends Contract.BaseView<C2CPresenter> {

        void getC2cConfigSuccess(C2cConfig c2cConfig);

        void doPostFail(Integer code, String toastMessage);
    }

    interface MyPresenter extends Contract.BasePresenter {

        void myWallet();
        void getUserInfo();
        void getNewVision();
        void safeSetting();
    }

    interface MyView extends Contract.BaseView<MyPresenter> {

        void myWalletSuccess(AssetsInfo assetsInfo);
        void getUserInfoSuccess(User user);
        void doPostFail(Integer code, String toastMessage);
        void getNewVisionSuccess(Vision obj);
        void safeSettingSuccess(SafeSetting obj);
    }

    interface BuyPresenter extends Contract.BasePresenter {

        void fastBuy(HashMap hashMap);

        void optBuy(HashMap hashMap);
    }

    interface BuyView extends Contract.BaseView<BuyPresenter> {
        void fastBuySuccess(String obj);
        void optBuySuccess(String obj);
        void doPostFail(Integer code, String toastMessage);
    }

    interface SellPresenter extends Contract.BasePresenter {

        void fastSell(HashMap hashMap);
        void optSell(HashMap hashMap);

    }

    interface SellView extends Contract.BaseView<SellPresenter> {
        void fastBuySuccess(String obj);
        void optBuySuccess(String obj);
        void doPostFail(Integer code, String toastMessage);
    }

    interface C2CListPresenter extends Contract.BasePresenter {
        void getList(HashMap hashMap);
        void getFaitsList(HashMap hashMap);
        void doCancelFiats(HashMap hashMap);
        void doFiatsOrder(HashMap hashMap);
    }

    interface C2CListView extends Contract.BaseView<MainContract.C2CListPresenter> {

        void getListSuccess(C2C c2c);
        void getListFaild(Integer code, String toastMessage);
        void doFiatsOrderSuccess(String obj);
        void getFiatsListSuccess(Fiats fiats);
        void getFiatsListFaild(Integer code, String toastMessage);
        void doCancelFiatsSuccess(String obj);
        void doPostFail(Integer code, String toastMessage);

    }

    interface EntrustPresenter extends Contract.BasePresenter {

        void entrust(String token, int pageSize, int pageNo, String symbol);

    }

    interface EntrustView extends Contract.BaseView<EntrustPresenter> {

        void entrustSuccess(List<Entrust> obj);

        void entrustFail(Integer code, String toastMessage);

        void cancleEntrustSuccess(String obj);

        void cancleEntrustFail(Integer code, String toastMessage);
    }












}
