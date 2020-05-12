package com.spark.szhb_master.activity.main;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.buy_or_sell.C2CBuyOrSellActivity;
import com.spark.szhb_master.activity.login.LoginActivity;
import com.spark.szhb_master.activity.login.LoginStepOneActivity;
import com.spark.szhb_master.activity.main.presenter.C2CListPresenterImpl;
import com.spark.szhb_master.adapter.C2CListAdapter;
import com.spark.szhb_master.adapter.FiatsListAdapter;
import com.spark.szhb_master.base.BaseLazyFragment;
import com.spark.szhb_master.dialog.ShiMingDialog;
import com.spark.szhb_master.entity.C2C;
import com.spark.szhb_master.entity.CoinInfo;
import com.spark.szhb_master.entity.Fiats;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.ui.AvatarImageView;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.spark.szhb_master.widget.pwdview.BuyOrSellView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import config.Injection;

/**
 * Created by Administrator on 2018/2/28.
 */

public class C2CListFragment extends BaseLazyFragment implements MainContract.C2CListView {
    @BindView(R.id.rvContent)
    RecyclerView rvContent;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private C2CListAdapter c2CListAdapter;
    private FiatsListAdapter fiatsListAdapter;
    private MainContract.C2CListPresenter presenter;
    private int pageNo = 1;
    private boolean isInit;

    private int type;  //0买 1卖 2委托

    List<C2C.C2CBean> c2cs = new ArrayList<>();
    List<Fiats.FiatsBean> fiats = new ArrayList<>();

    public static C2CListFragment getInstance(int type) {
        C2CListFragment c2CFragment = new C2CListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        c2CFragment.setArguments(bundle);
        return c2CFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_c2c_list;
    }

    @Override
    protected void initView() {
        super.initView();
        setShowBackBtn(true);
    }

    @Override
    protected void initData() {
        presenter = new C2CListPresenterImpl(Injection.provideTasksRepository(getActivity().getApplicationContext()), this);
        Bundle bundle = getArguments();
        type = bundle.getInt("type");
        initRvContent();
    }

    @Override
    protected void loadData() {
        super.loadData();
        if (type == 2){
            getFaitsList(false,pageNo);
        }else{
            getC2cList(false,pageNo);
        }

    }

    @Override
    public void doFiatsOrderSuccess(String obj) {
        ToastUtils.showToast(obj);
        if(bottomSheetDialog !=null && bottomSheetDialog.isShowing()){
            bottomSheetDialog.dismiss();
        }
        getC2cList(false,pageNo = 1);
    }

    @Override
    public void doCancelFiatsSuccess(String obj) {
        ToastUtils.showToast(obj);
        getFaitsList(false,pageNo = 1);
    }


    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void setListener() {
        super.setListener();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (type == 2){
                    getFaitsList(false,pageNo = 1);
                }else{
                    getC2cList(false,pageNo = 1);
                }

            }
        });

        if (fiatsListAdapter != null){
            fiatsListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    getFaitsList(false,pageNo + 1);
                }
            });
            fiatsListAdapter.setCallBackEvent(new FiatsListAdapter.CallBackEvent() {
                @Override
                public void onClickCallback(Fiats.FiatsBean item) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("id",item.getId());
                    presenter.doCancelFiats(hashMap);
                }
            });

        }else {
            c2CListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    getC2cList(false,pageNo + 1);
                }
            });

            c2CListAdapter.setCallBackEvent(new C2CListAdapter.CallBackEvent() {
                @Override
                public void onClickCallback(C2C.C2CBean item) {
                    if ( !MyApplication.getApp().isLogin()) {
                        showActivity(LoginStepOneActivity.class, null, LoginStepOneActivity.RETURN_LOGIN);
                        return;
                    }


                    if (bottomSheetDialog == null){
                        bottomSheetDialog = new BottomSheetDialog(getContext(),R.style.dialog_soft_input);
                        bottomSheetDialog.setCanceledOnTouchOutside(true);
                    }
                    BuyOrSellView buyOrSellView = new BuyOrSellView(getContext(),type == 0,item);
                    buyOrSellView.setCallbackListener(new BuyOrSellView.CallbackListener() {
                        @Override
                        public void submit(C2C.C2CBean c2CBean, String num) {
                            if (StringUtils.isEmpty(num) ){
                                ToastUtils.showToast("请输入合法参数");
                            }else{
                                HashMap hashMap = new HashMap();
                                hashMap.put("id",c2CBean.getId());
                                hashMap.put("type",type == 0 ? 1 : 0);
                                hashMap.put("num",Double.parseDouble(num));
                                presenter.doFiatsOrder(hashMap);
                            }
                        }

                        @Override
                        public void dismiss() {
                            bottomSheetDialog.dismiss();
                        }
                    });
                    bottomSheetDialog.setContentView(buyOrSellView);
                    bottomSheetDialog.show();
                }
            });
        }




    }


    private void initRvContent() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvContent.setLayoutManager(manager);
        if (type == 2){
            fiatsListAdapter = new FiatsListAdapter(getActivity(), R.layout.item_fiats_list,type,fiats);
            fiatsListAdapter.bindToRecyclerView(rvContent);
            fiatsListAdapter.isFirstOnly(true);
            fiatsListAdapter.setEnableLoadMore(false);
        }else{
            c2CListAdapter = new C2CListAdapter(getActivity(), R.layout.item_c2c_list,type,c2cs);

            c2CListAdapter.bindToRecyclerView(rvContent);
            c2CListAdapter.isFirstOnly(true);
            c2CListAdapter.setEnableLoadMore(false);
        }

    }

    /**
     * 获取列表数据
     */
    public void getC2cList(boolean isShow , int pageNo) {
        if (isShow)
            displayLoadingPopup();
        HashMap map = new HashMap<>();
        map.put("page", pageNo);
        map.put("page_size", GlobalConstant.PageSize);
        map.put("type", type);
        presenter.getList(map);
    }

    public void getFaitsList(boolean isShow , int pageNo) {
        if (isShow)
            displayLoadingPopup();
        HashMap map = new HashMap<>();
        map.put("page", pageNo);
        map.put("page_size", GlobalConstant.PageSize);
        map.put("type", type);
        presenter.getFaitsList(map);
    }

    @Override
    public void getFiatsListFaild(Integer code, String toastMessage) {
        hideLoadingPopup();
        fiatsListAdapter.setEnableLoadMore(true);
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        NetCodeUtils.checkedErrorCode(getmActivity(), code, toastMessage);
    }

    @Override
    public void getFiatsListSuccess(Fiats fiats) {
        hideLoadingPopup();
        try {
            fiatsListAdapter.setEnableLoadMore(true);
            fiatsListAdapter.loadMoreComplete();
            refreshLayout.setEnabled(true);
            refreshLayout.setRefreshing(false);
            List<Fiats.FiatsBean> fiatsBeans = fiats.getList();
            if (fiatsBeans != null && fiatsBeans.size() > 0) {
                if (pageNo == 1) {
                    this.fiats.clear();
                } else if (fiatsBeans.size() < GlobalConstant.PageSize){
                    fiatsListAdapter.loadMoreEnd();
                }

                if (fiatsBeans.size() == GlobalConstant.PageSize){
                    pageNo = fiats.getPage();
                }

                this.fiats.addAll(fiatsBeans);
                fiatsListAdapter.notifyDataSetChanged();
            } else {
                if (fiatsBeans.size() == 1) {
//                if (pageNo == 1 && obj.getTotalElement() == 0 ) {
                    this.fiats.clear();
                    fiatsListAdapter.setEmptyView(R.layout.empty_no_message);
                    fiatsListAdapter.notifyDataSetChanged();
                }
            }
            fiatsListAdapter.disableLoadMoreIfNotFullPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getListSuccess(C2C c2c) {
        hideLoadingPopup();
        try {
            c2CListAdapter.setEnableLoadMore(true);
            c2CListAdapter.loadMoreComplete();
            refreshLayout.setEnabled(true);
            refreshLayout.setRefreshing(false);
            List<C2C.C2CBean> c2cs = c2c.getList();
            if (c2cs != null && c2cs.size() > 0) {
                if (c2c.getPage() == 1) {
                    this.c2cs.clear();
                } else if (c2cs.size() < GlobalConstant.PageSize){
                    c2CListAdapter.loadMoreEnd();
                }

                pageNo = c2c.getPage();
                this.c2cs.addAll(c2cs);
                c2CListAdapter.notifyDataSetChanged();
            } else {
                if (c2c.getPage() == 1) {
//                if (pageNo == 1 && obj.getTotalElement() == 0 ) {
                    this.c2cs.clear();
                    c2CListAdapter.setEmptyView(R.layout.empty_no_message);
                    c2CListAdapter.notifyDataSetChanged();
                }
            }
            c2CListAdapter.disableLoadMoreIfNotFullPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getListFaild(Integer code, String toastMessage) {
        hideLoadingPopup();
        c2CListAdapter.setEnableLoadMore(true);
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        NetCodeUtils.checkedErrorCode(getmActivity(), code, toastMessage);
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        ToastUtils.showToast(toastMessage);
    }


    @Override
    public void setPresenter(MainContract.C2CListPresenter presenter) {
        this.presenter = presenter;
    }
}
