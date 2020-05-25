package com.spark.szhb_master.activity.c2corder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.C2cOrderAdapter;
import com.spark.szhb_master.entity.C2cOrder;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.NetCodeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import config.Injection;

public class C2cOrderListFragment extends Fragment implements C2cOrderContract.View,BGARefreshLayout.BGARefreshLayoutDelegate {

    @BindView(R.id.recycleview)
    RecyclerView recyclerView;

    @BindView(R.id.refreshLayout)
    BGARefreshLayout refreshLayout;

    @BindView(R.id.rlEmpty)
    RelativeLayout rlRmpty;



    private int type,page;
    private C2cOrderAdapter adapter;
    private C2cOrderContract.Presenter presenter;

    private List<C2cOrder.ListBean> listBeanList;
    public static C2cOrderListFragment getInstance(int type) {
        C2cOrderListFragment c2CFragment = new C2cOrderListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        c2CFragment.setArguments(bundle);
        return c2CFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshLayout.beginRefreshing();
    }

    private View rootView;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.view_recycleview, null);
        ButterKnife.bind(this, rootView);
        init();
        return rootView;
    }

    @Override
    public void hideLoadingPopup() {
        refreshLayout.endLoadingMore();
        refreshLayout.endRefreshing();
    }

    @Override
    public void displayLoadingPopup() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        type = bundle.getInt("type");

        new C2COrderListPresenter(Injection.provideTasksRepository(getActivity()), this);
    }

    @Override
    public void onDestroyView() {
        if (unbinder!=null)
            unbinder.unbind();
        super.onDestroyView();
    }

    private boolean bInit = false;
    private void init(){
        refreshLayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(getActivity(), true);
        // 设置下拉刷新和上拉加载更多的风格

        refreshViewHolder.setLoadingMoreText("加载更多");
        // 设置下拉刷新和上拉加载更多的风格
//        refreshLayout.setIsShowLoadingMoreView(true);
        refreshLayout.setPullDownRefreshEnable(true);
        refreshLayout.setRefreshViewHolder(refreshViewHolder);

        listBeanList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new C2cOrderAdapter(getActivity(),listBeanList);
        adapter.setType(type);
        adapter.bindToRecyclerView(recyclerView);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(),C2cOrderDetailActivity.class);
                intent.putExtra("id",listBeanList.get(position).getId());
                intent.putExtra("type",type);
                getActivity().startActivity(intent);

            }
        });
        recyclerView.setAdapter(adapter);
    }
//    @Override
//    public void hideLoadingPopup() {
//        super.hideLoadingPopup();
//        refreshLayout.endLoadingMore();
//        refreshLayout.endRefreshing();
//    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        HashMap map = new HashMap<>();
        map.put("page", page + 1);
        map.put("page_size", GlobalConstant.PageSize);
        map.put("type",type);
        presenter.getList(map);
        return false;
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        page = 1;
        HashMap map = new HashMap<>();
        map.put("page", page);
        map.put("page_size", GlobalConstant.PageSize);
        map.put("type",type);
        presenter.getList(map);
    }

    @Override
    public void getListSuccess(C2cOrder c2cOrder) {
        try {
            adapter.setEnableLoadMore(true);
            adapter.loadMoreComplete();

            List<C2cOrder.ListBean> c2cOrderList = c2cOrder.getList();
            if (c2cOrderList != null && c2cOrderList.size() > 0) {
                if (c2cOrder.getPage() == 1) {
                    this.listBeanList.clear();
                } else if (c2cOrderList.size() < GlobalConstant.PageSize){
                    adapter.loadMoreEnd();
                }

                page = c2cOrder.getPage();

                this.listBeanList.addAll(c2cOrderList);
                adapter.notifyDataSetChanged();
            } else {
                if (c2cOrder.getPage() == 1) {
                    this.listBeanList.clear();
                    adapter.setEmptyView(R.layout.empty_no_message);
                    adapter.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void getListFaild(Integer code, String obj) {
        NetCodeUtils.checkedErrorCode(this,code,obj);
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this,code,toastMessage);
    }

    @Override
    public void setPresenter(C2cOrderContract.Presenter presenter) {
        this.presenter = presenter;
    }


}
