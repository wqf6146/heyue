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
import com.spark.szhb_master.entity.NewEntrust;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.ToastUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import config.Injection;

/**
 * kline_深度
 * Created by daiyy on 2018/6/4 0004.
 */

public class DqccFragment extends BaseFragment implements TradeContract.DqccView,BGARefreshLayout.BGARefreshLayoutDelegate  {

    @BindView(R.id.recycleview)
    RecyclerView mRecycleView;

    @BindView(R.id.refreshLayout)
    BGARefreshLayout mRefreshlayout;

    @BindView(R.id.rlEmpty)
    RelativeLayout rlEmpty;

    private TradeContract.DqccPresenter presenter;
    private TrustAdapter trustAdapter;
    private List<NewEntrust.ListBean> entrustList;

    public static DqccFragment getInstance(String symbol, String symbolType) {
        DqccFragment dqccFragment = new DqccFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("symbol", symbol);
        bundle.putSerializable("symbolType", symbolType);
        dqccFragment.setArguments(bundle);
        return dqccFragment;
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible)
            mRefreshlayout.beginRefreshing();
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
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
        mRefreshlayout.setRefreshViewHolder(refreshViewHolder);

        mRefreshlayout.setPullDownRefreshEnable(false);

        entrustList = new ArrayList<>();
        mRecycleView.setLayoutManager(new LinearLayoutManager(activity));
        trustAdapter = new TrustAdapter(entrustList);
        trustAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                HashMap hashMap = new HashMap();
                hashMap.put("market_id",(int)view.getTag());
                presenter.commitUndersellContrat(hashMap);
            }
        });
        mRecycleView.setAdapter(trustAdapter);
    }

    @Override
    public void undersellContratSuccess(String msg) {
        ToastUtils.showToast("平仓成功");
        beginRefreshing();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        HashMap map = new HashMap<>();
        map.put("mark", mMark);
        map.put("leverage", mLeverage);
        map.put("page", mPage + 1);
        map.put("page_size", 20);
        presenter.getCurrentHave(map);
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
        presenter.getCurrentHave(map);
    }

    private String mMark,mLeverage;
    private int mPage = 1,mPageSize=20;

    @Override
    protected void initData() {
        super.initData();
        new DqccPresenter(Injection.provideTasksRepository(activity), this);
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
    public void getCurrentHaveSuccess(NewEntrust entrustEntity) {
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
            if (entrustEntity.getList().size() == 1) {
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
    public void setPresenter(TradeContract.DqccPresenter presenter) {
        this.presenter = presenter;
    }
}
