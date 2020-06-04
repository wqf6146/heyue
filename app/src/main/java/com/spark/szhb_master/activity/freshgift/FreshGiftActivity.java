package com.spark.szhb_master.activity.freshgift;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.FreshGiftAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.FreshGitBean;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.spark.szhb_master.widget.TipDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import config.Injection;


public class FreshGiftActivity extends BaseActivity implements FreshGiftContract.View {


    private FreshGiftContract.Presenter presenter;


    @BindView(R.id.recycleview)
    RecyclerView rvMessage;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.ivBack)
    ImageView ivBack;

    private List<FreshGitBean.Gift> mGiftList = new ArrayList<>();
    private FreshGiftAdapter adapter;

    private TipDialog tipDialog;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_freshgift;
    }

    @Override
    protected void initView() {
        new FreshGiftPresenter(Injection.provideTasksRepository(getApplicationContext()), this);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvMessage.setLayoutManager(manager);
        adapter = new FreshGiftAdapter(R.layout.item_freshgift, mGiftList, this);
        adapter.bindToRecyclerView(rvMessage);

        adapter.setDoGetGiftCallback(new FreshGiftAdapter.doGetGiftCallback() {
            @Override
            public void doGetGift(final int id) {
                HashMap hashMap = new HashMap();
                hashMap.put("id",id);
                presenter.doGetGift(hashMap);
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mGiftList.clear();
                adapter.notifyDataSetChanged();
                presenter.getGiftList();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        refreshLayout.setEnableLoadMore(false);
    }

    @Override
    protected void loadData() {
        super.loadData();
        refreshLayout.autoRefresh();
    }

    @Override
    public void setPresenter(FreshGiftContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getGiftListSuccess(FreshGitBean freshGitBean) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();

        List<FreshGitBean.Gift> giftList = freshGitBean.getList();
        if (giftList != null && giftList.size() > 0) {
            this.mGiftList.addAll(giftList);
            adapter.notifyDataSetChanged();
        } else {
            this.mGiftList.clear();
            adapter.notifyDataSetChanged();
            adapter.setEmptyView(R.layout.empty_no_message);
        }
    }

    @Override
    public void doGetGiftSuccess(String obj) {
        if (tipDialog == null) {
            tipDialog = new TipDialog.Builder(FreshGiftActivity.this)
                    .setPicResources(R.mipmap.nike)
                    .setMessage(obj)
                    .setCancelIsShow(false)
                    .setCanCancelOutside(false)
                    .setEnsureClickListener(new TipDialog.EnsureClickListener() {
                        @Override
                        public void onEnsureClick(TipDialog tipDialog) {
                            tipDialog.dismiss();
                            refreshLayout.autoRefresh();
                        }
                    }).build();
        }
        tipDialog.show();
    }

    @Override
    public void getGiftListFail(Integer code, String toastMessage) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    @Override
    public void doGetGiftFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

}
