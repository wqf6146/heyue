package com.spark.szhb_master.activity.Trade;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.TrustAdapter;
import com.spark.szhb_master.base.BaseFragment;
import com.spark.szhb_master.entity.Entrust;
import com.spark.szhb_master.entity.NewEntrust;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.ToastUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.refreshlayout.BGAMeiTuanRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGAStickinessRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGAStickyNavLayout;
import config.Injection;

/**
 * kline_深度
 * Created by daiyy on 2018/6/4 0004.
 */

public class DqwtFragment extends BaseFragment implements TradeContract.DqwtView,BGARefreshLayout.BGARefreshLayoutDelegate  {

    @BindView(R.id.recycleview)
    RecyclerView mRecycleView;

    @BindView(R.id.refreshLayout)
    BGARefreshLayout mRefreshlayout;

    @BindView(R.id.rlEmpty)
    RelativeLayout rlEmpty;

    private TradeContract.DqwtPresenter presenter;
    private TrustAdapter trustAdapter;
    private List<NewEntrust.ListBean> entrustList;

    public static DqwtFragment getInstance(String symbol, String symbolType) {
        DqwtFragment depthFragment = new DqwtFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("symbol", symbol);
        bundle.putSerializable("symbolType", symbolType);
        depthFragment.setArguments(bundle);
        return depthFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_recycleview;
    }

    @Override
    protected void initView() {

        mRefreshlayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(getActivity(), true);
        // 设置下拉刷新和上拉加载更多的风格

        refreshViewHolder.setLoadingMoreText("加载更多");
        // 设置下拉刷新和上拉加载更多的风格
        mRefreshlayout.setIsShowLoadingMoreView(true);
        mRefreshlayout.setPullDownRefreshEnable(false);
        mRefreshlayout.setRefreshViewHolder(refreshViewHolder);

        entrustList = new ArrayList<>();
        mRecycleView.setLayoutManager(new LinearLayoutManager(activity));
        trustAdapter = new TrustAdapter(entrustList);
        trustAdapter.setType(1);
        trustAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                HashMap hashMap = new HashMap();
                hashMap.put("limit_id",(int)view.getTag());
                presenter.undoLimitContrat(hashMap);
            }
        });
        mRecycleView.setAdapter(trustAdapter);

    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible)
            mRefreshlayout.beginRefreshing();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        HashMap map = new HashMap<>();
        map.put("mark", mMark);
        map.put("leverage", mLeverage);
        map.put("page", mPage + 1);
        map.put("page_size", 20);
        presenter.getCurrentEntrust(map);
        return true;
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mPage = 1;
        HashMap map = new HashMap<>();
        map.put("mark", mMark);
        map.put("leverage", mLeverage);
        map.put("page", mPage);
        map.put("page_size", 20);
        presenter.getCurrentEntrust(map);
    }

    private String mMark,mLeverage;
    private int mPage = 1,mPageSize=20;

    @Override
    protected void initData(){
        super.initData();
        new DqwtPresenter(Injection.provideTasksRepository(activity), this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mMark = bundle.getString("mark");
            mLeverage = bundle.getString("leverage");
        }

        if (MyApplication.getApp().isLogin()) {
            mRefreshlayout.beginRefreshing();
        }else{
            rlEmpty.setVisibility(View.VISIBLE);
        }
    }

    public void beginRefreshing(){
        mRefreshlayout.beginRefreshing();
    }

    @Override
    public void undoLimitContratSuccess(String msg) {
        ToastUtils.showToast("撤销成功");
        beginRefreshing();
    }

    @Override
    public void getCurrentEntrustSuccess(NewEntrust entrustEntity) {
//        if (entrustEntity.getPage() == 1 && entrustEntity.getList().size() == 0){
//            rlEmpty.setVisibility(View.VISIBLE);
//        }else{
//            rlEmpty.setVisibility(View.GONE);
//
//            if (entrustEntity.getList().size() > 0 && entrustEntity.getList().size() < mPageSize){
//                mPage = entrustEntity.getPage();
//                if (mRefreshlayout.getCurrentRefreshStatus() == BGARefreshLayout.RefreshStatus.REFRESHING){
//                    entrustList.clear();
//                }
//                entrustList.addAll(entrustEntity.getList());
//            }
//
//
//            trustAdapter.notifyDataSetChanged();
//        }

        if (entrustEntity != null && entrustEntity.getList().size() > 0) {
            if (mPage == 1) {
                this.entrustList.clear();
            } else {
                mRefreshlayout.endLoadingMore();
            }

            if (entrustList.size() == mPageSize){
                mPage = entrustEntity.getPage();
            }

            this.entrustList.addAll(entrustEntity.getList());
            trustAdapter.notifyDataSetChanged();
        } else {
            if (mPage == 1) {
//                if (pageNo == 1 && obj.getTotalElement() == 0 ) {
                this.entrustList.clear();
                rlEmpty.setVisibility(View.VISIBLE);
                trustAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void hideLoadingPopup() {
        super.hideLoadingPopup();
        mRefreshlayout.endLoadingMore();
        mRefreshlayout.endRefreshing();
    }

    @Override
    public void doPostFail(int e, String msg){
        NetCodeUtils.checkedErrorCode(this, e, msg);
    }

    @Override
    public void setPresenter(TradeContract.DqwtPresenter presenter) {
        this.presenter = presenter;
    }
}
