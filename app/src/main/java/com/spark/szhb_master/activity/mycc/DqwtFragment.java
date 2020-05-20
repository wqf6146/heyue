package com.spark.szhb_master.activity.mycc;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.TrustAdapter;
import com.spark.szhb_master.base.BaseFragment;
import com.spark.szhb_master.entity.NewEntrust;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import config.Injection;

/**
 * kline_深度
 * Created by daiyy on 2018/6/4 0004.
 */

public class DqwtFragment extends BaseFragment implements MyccContract.DqwtView {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.recycleview)
    RecyclerView recyclerView;

    private int mType;

    private MyccContract.DqwtPresenter presenter;
    private TrustAdapter trustAdapter;
    private List<NewEntrust.ListBean> entrustList;
    private OnCallBackEvent callBackEvent;

    public static DqwtFragment getInstance(int type) {
        DqwtFragment fragment = new DqwtFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_smartrecycleview;
    }

    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        mType = bundle.getInt("type");
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
//                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败

                doRefresh();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
//                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                doLoadMore();
            }
        });


        entrustList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        trustAdapter = new TrustAdapter(entrustList);
        trustAdapter.setType(mType);
        trustAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                HashMap hashMap = new HashMap();
                hashMap.put("limit_id",(int)view.getTag());
                presenter.undoLimitContrat(hashMap);
            }
        });
        trustAdapter.bindToRecyclerView(recyclerView);

    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible && MyApplication.getApp().isLogin())
            doRefresh();
    }


    public void doLoadMore() {
        HashMap map = new HashMap<>();
        map.put("page", mPage + 1);
        map.put("page_size", GlobalConstant.PageSize);

        if (mType == 1){
            map.put("status",0);
        }else{
            map.put("status",1);
        }

        presenter.getCurrentEntrust(map);
    }

    public void doRefresh() {
        mPage = 1;
        HashMap map = new HashMap<>();
        map.put("page", mPage);
        map.put("page_size", GlobalConstant.PageSize);

        if (mType == 1){
            map.put("status",0);
        }else{
            map.put("status",1);
        }
        presenter.getCurrentEntrust(map);
    }


    private int mPage = 1;

    @Override
    protected void initData(){
        super.initData();
        new DqwtPresenter(Injection.provideTasksRepository(activity), this);

        doRefresh();
    }


    @Override
    public void undoLimitContratSuccess(String msg) {
        ToastUtils.showToast("撤销成功");
        doRefresh();
        if (callBackEvent!=null)
            callBackEvent.undoLimitContratSuccess();
    }

    public void setCallBackEvent(OnCallBackEvent callBackEvent) {
        this.callBackEvent = callBackEvent;
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return false;
    }

    @Override
    public void getCurrentEntrustSuccess(NewEntrust entrustEntity) {

        if (entrustEntity != null && entrustEntity.getList().size() > 0) {
            if (callBackEvent!=null)
                callBackEvent.showEmpty(false);
            if (entrustEntity.getPage() == 1) {
                trustAdapter.getData().clear();
            }
            if (entrustEntity.getList().size() < GlobalConstant.PageSize){
                refreshLayout.finishLoadMoreWithNoMoreData();
            }

            mPage = entrustEntity.getPage();
            trustAdapter.getData().addAll(entrustEntity.getList());
            trustAdapter.notifyDataSetChanged();
        } else {
            if (entrustEntity.getPage() == 1) {
                trustAdapter.getData().clear();
                trustAdapter.setEmptyView(R.layout.empty_no_message);
                trustAdapter.notifyDataSetChanged();
                if (callBackEvent!=null)
                    callBackEvent.showEmpty(true);
            }
        }
    }

    @Override
    public void hideLoadingPopup() {
        super.hideLoadingPopup();
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
    }

    @Override
    public void doPostFail(int e, String msg){
        NetCodeUtils.checkedErrorCode(this, e, msg);
    }

    @Override
    public void setPresenter(MyccContract.DqwtPresenter presenter) {
        this.presenter = presenter;
    }



    public interface OnCallBackEvent {
        void undoLimitContratSuccess();
        void showEmpty(boolean isShow);
    }
}
