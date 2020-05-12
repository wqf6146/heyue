package com.spark.szhb_master.activity.Bind_bank;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.BankItemAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.BankEntity;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import config.Injection;

public class BankListActivity extends BaseActivity implements BindBankContract.ListView, BGARefreshLayout.BGARefreshLayoutDelegate{

    @BindView(R.id.ar_iv_close)
    ImageView ivClose;

    @BindView(R.id.refreshLayout)
    BGARefreshLayout refreshLayout;

    @BindView(R.id.recycleview)
    RecyclerView recyclerView;

    @BindView(R.id.rlEmpty)
    RelativeLayout rlEmpty;

    @BindView(R.id.ivEmpty)
    ImageView ivEmpty;

    @BindView(R.id.tvEmpty)
    TextView tvEmpty;

    @BindView(R.id.rlhead)
    RelativeLayout rlhead;

    private List<BankEntity.ListBean> beanList = new ArrayList<>();
    private BankItemAdapter adapter;
    private BindBankContract.ListPresenter presenter;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_banklist;
    }

    @Override
    protected void initView() {
        super.initView();
        setImmersionBar(rlhead);

        refreshLayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(this, false);
        // 设置下拉刷新和上拉加载更多的风格
        refreshLayout.setRefreshViewHolder(refreshViewHolder);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new BankItemAdapter(this,beanList);
        adapter.setOnItemCheckChange(new BankItemAdapter.OnItemCheckChange() {
            @Override
            public void onItemCheckChange(int bankid, int isOpen, int status) {
                HashMap hashMap = new HashMap();
                hashMap.put("id",bankid);
                hashMap.put("is_open",isOpen);
                hashMap.put("status",status);
                presenter.changeBank(hashMap);
            }

            @Override
            public void deleteItem(int id) {
                HashMap hashMap = new HashMap();
                hashMap.put("id",id);
                presenter.deleteBank(hashMap);
            }
        });
        adapter.addFooterView(getFooterView());

        recyclerView.setAdapter(adapter);
    }

    private View getFooterView(){
        View view = LayoutInflater.from(this).inflate(R.layout.view_footer,null);
        view.findViewById(R.id.tvAddBank).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showActivity(BindBankActivity.class,null);
            }
        });
        return view;
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        presenter.getList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshLayout.beginRefreshing();
    }

    @Override
    public void changeBankSuccess(String obj) {
        ToastUtils.showToast(obj);
        refreshLayout.beginRefreshing();
    }

    @Override
    public void deleteBankSuccess(String obj) {
        ToastUtils.showToast(obj);
        refreshLayout.beginRefreshing();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    @Override
    protected void initData() {
        super.initData();
        new BankListPresenter(Injection.provideTasksRepository(activity), this);
    }

    @OnClick({R.id.ar_iv_close,R.id.tvEmpty})
    @Override
    protected void setOnClickListener(View v) {
        switch (v.getId()){
            case R.id.ar_iv_close:
                finish();
                break;

            case R.id.tvEmpty:
                showActivity(BindBankActivity.class,null);
                break;
        }
    }

    @Override
    public void hideLoadingPopup() {
        super.hideLoadingPopup();
        if (refreshLayout!=null)
            refreshLayout.endRefreshing();
    }

    @Override
    public void getBankListSuccess(BankEntity bankEntity) {
        if (bankEntity!=null && bankEntity.getList().size() > 0){
            beanList.clear();
            beanList.addAll(bankEntity.getList());
            adapter.notifyDataSetChanged();
            rlEmpty.setVisibility(View.GONE);
        }else{
            rlEmpty.setVisibility(View.VISIBLE);
            ivEmpty.setVisibility(View.GONE);
            tvEmpty.setText("添加银行卡");
            tvEmpty.setVisibility(View.VISIBLE);
            tvEmpty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showActivity(BindBankActivity.class,null);
                }
            });
        }
    }

    @Override
    public void setPresenter(BindBankContract.ListPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode((BaseActivity) activity, code, toastMessage);
    }

}


