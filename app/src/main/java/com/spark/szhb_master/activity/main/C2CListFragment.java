package com.spark.szhb_master.activity.main;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.spark.szhb_master.base.BaseLazyFragment;
import com.spark.szhb_master.dialog.ShiMingDialog;
import com.spark.szhb_master.entity.C2C;
import com.spark.szhb_master.entity.CoinInfo;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.ui.AvatarImageView;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.NetCodeUtils;
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

    private C2CListAdapter adapter;

    private MainContract.C2CListPresenter presenter;
    private int pageNo = 1;
    private String advertiseType = GlobalConstant.SELL;
    private boolean isInit;

    private int type;  //0买 1卖 2

    List<C2C.C2CBean> c2cs = new ArrayList<>();

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

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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
        getC2cList(false,pageNo);
    }

    @Override
    protected void setListener() {
        super.setListener();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                getC2cList(false,pageNo);
            }
        });

        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getC2cList(false,pageNo + 1);
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if ( !MyApplication.getApp().isLogin()) {
                    showActivity(LoginStepOneActivity.class, null, LoginStepOneActivity.RETURN_LOGIN);
                    return;
                }

                BuyOrSellView buyOrSellView = new BuyOrSellView(getContext(),type == 0,(C2C.C2CBean) adapter.getData().get(position));
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
                buyOrSellView.setCallbackListener(new BuyOrSellView.CallbackListener() {
                    @Override
                    public void submit(double price, double num) {

                    }

                    @Override
                    public void dismiss() {

                    }
                });

                bottomSheetDialog.setContentView(buyOrSellView);
                bottomSheetDialog.setCanceledOnTouchOutside(true);
                bottomSheetDialog.show();
            }
        });

    }


    private void initRvContent() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvContent.setLayoutManager(manager);
        if (type == 2){
            adapter = new C2CListAdapter(getActivity(), R.layout.item_c2c_list,type,c2cs);
        }else{
            adapter = new C2CListAdapter(getActivity(), R.layout.item_c2c_list,type,c2cs);
        }
        adapter.bindToRecyclerView(rvContent);
        adapter.isFirstOnly(true);
        adapter.setEnableLoadMore(false);
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

    @Override
    public void getListSuccess(C2C c2c) {
        hideLoadingPopup();
        try {

            adapter.setEnableLoadMore(true);
            adapter.loadMoreComplete();
            refreshLayout.setEnabled(true);
            refreshLayout.setRefreshing(false);
            List<C2C.C2CBean> c2cs = c2c.getList();
            if (c2cs != null && c2cs.size() > 0) {
                if (pageNo == 1) {
                    this.c2cs.clear();
                } else {
                    adapter.loadMoreEnd();
                }

                if (c2c.getList().size() == GlobalConstant.PageSize){
                    pageNo = c2c.getPage();
                }

                this.c2cs.addAll(c2cs);
                adapter.notifyDataSetChanged();
            } else {
                if (pageNo == 1) {
//                if (pageNo == 1 && obj.getTotalElement() == 0 ) {
                    this.c2cs.clear();
                    adapter.setEmptyView(R.layout.empty_no_message);
                    adapter.notifyDataSetChanged();
                }
            }
            adapter.disableLoadMoreIfNotFullPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getListFaild(Integer code, String toastMessage) {
        hideLoadingPopup();
        adapter.setEnableLoadMore(true);
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
