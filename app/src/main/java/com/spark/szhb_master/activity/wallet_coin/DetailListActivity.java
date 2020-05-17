package com.spark.szhb_master.activity.wallet_coin;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.WalletThreeAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.WalletThreeBean;
import com.spark.szhb_master.factory.UrlFactory;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import config.Injection;

public class DetailListActivity extends BaseActivity implements CoinContract.recordeListView {

    @BindView(R.id.ar_iv_close)
    ImageView ivClose;

    @BindView(R.id.rlhead)
    RelativeLayout rlhead;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.recycleview)
    RecyclerView recyclerView;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    private int type; //0充币 1提币 2资金划转

    private CoinContract.recordeListPresenter presenter;

    private int mPage = 1;

    @Override
    public void setPresenter(CoinContract.recordeListPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_walletthreelist;
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this,code,toastMessage);
    }


    private WalletThreeAdapter adapter;
    @Override
    protected void initView() {
        super.initView();

        type = getIntent().getIntExtra("type",0);
        if (type == 0){
            tvTitle.setText("充币记录");
        }else if (type == 1){
            tvTitle.setText("提币记录");
        }else if (type == 2){
            tvTitle.setText("资金划转记录");
        }

        new RecordListPresenter(Injection.provideTasksRepository(getApplicationContext()), this);

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

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        adapter = new WalletThreeAdapter(type,mListData);
        adapter.bindToRecyclerView(recyclerView);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void loadData() {
        super.loadData();
        refreshLayout.autoRefresh();
    }

    private String getUrl(){
        if (type == 0){
            return UrlFactory.getRechargeListUrl();
        }else if (type == 1){
            return UrlFactory.getWithdrawListUrl();
        }else{
            return UrlFactory.getTransListUrl();
        }
    }

    private void doRefresh(){
        HashMap map = new HashMap<>();
        map.put("page", mPage);
        map.put("page_size", GlobalConstant.PageSize);
        presenter.getList(getUrl(),map);
    }

    private void doLoadMore(){
        HashMap map = new HashMap<>();
        map.put("page", mPage + 1);
        map.put("page_size", GlobalConstant.PageSize);
        presenter.getList(getUrl(),map);
    }

    private List<WalletThreeBean.ListBean> mListData = new ArrayList<>();
    @Override
    public void getListSuccess(WalletThreeBean walletThreeBean) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();

        if (walletThreeBean != null && walletThreeBean.getList().size() > 0) {
            if (walletThreeBean.getPage() == 1) {
                adapter.getData().clear();
            } else if (walletThreeBean.getList().size() < GlobalConstant.PageSize){
                refreshLayout.finishLoadMoreWithNoMoreData();
            }

            mPage = walletThreeBean.getPage();
            adapter.getData().addAll(walletThreeBean.getList());
            adapter.notifyDataSetChanged();
        } else {
            if (walletThreeBean.getPage() == 1) {
                adapter.getData().clear();
                adapter.notifyDataSetChanged();
                adapter.setEmptyView(R.layout.empty_no_message);
            }
        }
    }
}
