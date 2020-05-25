package com.spark.szhb_master.activity.wallet_coin;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.ExtAddressAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.ExtAddressEntity;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.spark.szhb_master.widget.TipDialog;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

/**
 * 提币
 */
public class ExtractAddressListActivity extends BaseActivity implements CoinContract.extractListView {

    @BindView(R.id.ar_iv_close)
    ImageView ivClose;

    @BindView(R.id.rlhead)
    RelativeLayout rlhead;

    @BindView(R.id.tvAdd)
    TextView tvAdd;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.recycleview)
    RecyclerView recyclerView;


    private TipDialog removeTipDialog;
    private int mPage = 1;
    private CoinContract.extractListPresenter presenter;
    private List<ExtAddressEntity.ListBean> listBeans;
    private ExtAddressAdapter adapter;
    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_extaddresslist;
    }

    @Override
    protected void initView() {
        setImmersionBar(rlhead);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                doRefresh();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                doLoadMore();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        adapter = new ExtAddressAdapter(listBeans);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (removeTipDialog == null) {
                    removeTipDialog = new TipDialog.Builder(ExtractAddressListActivity.this)
                            .setPicResources(R.mipmap.woo)
                            .setMessage("是否删除该地址?")
                            .setCanCancelOutside(false)
                            .setEnsureClickListener(new TipDialog.EnsureClickListener() {
                                @Override
                                public void onEnsureClick(TipDialog tipDialog) {
                                    tipDialog.dismiss();
                                    HashMap hashMap = new HashMap();
                                    ExtAddressEntity.ListBean bean = (ExtAddressEntity.ListBean)adapter.getData().get(position);
                                    hashMap.put("id",bean.getId());
                                    presenter.removeExtAddress(hashMap);
                                }
                            }).build();
                }
                removeTipDialog.show();
            }
        });
        adapter.bindToRecyclerView(recyclerView);
    }

    @Override
    protected void initData() {
        super.initData();
        new ExtractListPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshLayout.autoRefresh();
    }

    @OnClick({ R.id.ar_iv_close,R.id.tvAdd,})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.ar_iv_close:
                finish();
                break;
            case R.id.tvAdd: // 提币

                showActivity(AddExtActivity.class,null);
                break;

        }
    }

    private void doLoadMore() {
        HashMap map = new HashMap<>();
        map.put("page", mPage + 1);
        map.put("page_size", GlobalConstant.PageSize);
        presenter.getExtAddress(map);
    }

    private void doRefresh() {
        HashMap map = new HashMap<>();
        map.put("page", mPage);
        map.put("page_size", GlobalConstant.PageSize);
        presenter.getExtAddress(map);
    }


    @Override
    public void setPresenter(CoinContract.extractListPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }


    @Override
    public void addExtAddressSuccess(String obj) {

    }

    @Override
    public void removeExtAddressSuccess(String obj) {
        ToastUtils.showToast(obj);
        refreshLayout.autoRefresh();
    }

    @Override
    public void getExtListSuccess(ExtAddressEntity extAddressEntity) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();

        if (extAddressEntity != null && extAddressEntity.getList().size() > 0) {
            if (extAddressEntity.getPage() == 1) {
                adapter.getData().clear();
            } else if (extAddressEntity.getList().size() < GlobalConstant.PageSize){
                refreshLayout.finishLoadMoreWithNoMoreData();
            }

            mPage = extAddressEntity.getPage();
            adapter.getData().addAll(extAddressEntity.getList());
            adapter.notifyDataSetChanged();
        } else {
            if (extAddressEntity.getPage() == 1) {
                adapter.getData().clear();
                adapter.notifyDataSetChanged();
                adapter.setEmptyView(R.layout.empty_no_message);
            }
        }
    }
}
