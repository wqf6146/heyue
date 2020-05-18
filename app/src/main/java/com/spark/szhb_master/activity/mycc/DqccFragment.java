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


public class DqccFragment extends BaseFragment implements MyccContract.DqccView {


    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.recycleview)
    RecyclerView recyclerView;

    private int mType; //0-当前持仓 1-当前委托 2历史委托 3历史持仓

    private MyccContract.DqccPresenter presenter;
    private TrustAdapter trustAdapter;
    private List<NewEntrust.ListBean> entrustList;
    private OnCallBackEvent onCallBackEvent;

    public static DqccFragment getInstance(int type) {
        DqccFragment dqccFragment = new DqccFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        dqccFragment.setArguments(bundle);
        return dqccFragment;
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible && MyApplication.getApp().isLogin())
            refreshLayout.autoRefresh();
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.view_smartrecycleview;
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return false;
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
                hashMap.put("market_id",(int)view.getTag());
                presenter.commitUndersellContrat(hashMap);
            }
        });
        trustAdapter.bindToRecyclerView(recyclerView);
    }

    @Override
    public void undersellContratSuccess(String msg) {
        ToastUtils.showToast("平仓成功");
        refreshLayout.autoRefresh();
        if (onCallBackEvent!=null)
            onCallBackEvent.undersellContratSuccess();
    }


    public boolean doLoadMore() {
        HashMap map = new HashMap<>();
        map.put("page", mPage + 1);
        map.put("page_size", GlobalConstant.PageSize);
        presenter.getCurrentHave(map);
        return true;
    }

    public void doRefresh() {
        mPage = 1;
        HashMap map = new HashMap<>();
        map.put("page", mPage);
        map.put("page_size", GlobalConstant.PageSize);
        presenter.getCurrentHave(map);
    }

    private String mMark,mLeverage;
    private int mPage = 1;

    @Override
    protected void initData() {
        super.initData();
        new DqccPresenter(Injection.provideTasksRepository(activity), this);

        doRefresh();
    }



    @Override
    public void getCurrentHaveSuccess(NewEntrust entrustEntity) {
        if (entrustEntity != null && entrustEntity.getList().size() > 0) {
            if (onCallBackEvent!=null)
                onCallBackEvent.showEmpty(false);
            if (entrustEntity.getPage() == 1) {
                trustAdapter.getData().clear();
            } else if (entrustEntity.getList().size() < GlobalConstant.PageSize){
                refreshLayout.finishLoadMoreWithNoMoreData();
            }

            mPage = entrustEntity.getPage();
            trustAdapter.addData(entrustEntity.getList());
        } else {
            if (entrustEntity.getPage() == 1) {
                trustAdapter.getData().clear();
                trustAdapter.setEmptyView(R.layout.empty_no_message);
                trustAdapter.notifyDataSetChanged();
                if (onCallBackEvent!=null)
                    onCallBackEvent.showEmpty(true);
            }
        }
    }


    @Override
    public void hideLoadingPopup() {
        super.hideLoadingPopup();
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }

    @Override
    public void doPostFail(int e, String msg){
        NetCodeUtils.checkedErrorCode(this, e, msg);
    }

    @Override
    public void setPresenter(MyccContract.DqccPresenter presenter) {
        this.presenter = presenter;
    }

    public void setOnCallBackEvent(OnCallBackEvent onCallBackEvent) {
        this.onCallBackEvent = onCallBackEvent;
    }

    public interface OnCallBackEvent {
        void undersellContratSuccess();
        void showEmpty(boolean isShow);
    }
}
