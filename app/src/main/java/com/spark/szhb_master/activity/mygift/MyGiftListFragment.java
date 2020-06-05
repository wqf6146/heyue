package com.spark.szhb_master.activity.mygift;

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
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.c2corder.C2cOrderDetailActivity;
import com.spark.szhb_master.adapter.C2cOrderAdapter;
import com.spark.szhb_master.adapter.MyGiftAdapter;
import com.spark.szhb_master.entity.C2cOrder;
import com.spark.szhb_master.entity.FreshGitBean;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.ToastUtils;

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

public class MyGiftListFragment extends Fragment implements MyGiftContract.View{

    @BindView(R.id.recycleview)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;



    private int type;
    private MyGiftAdapter adapter;
    private MyGiftContract.Presenter presenter;

    private List<FreshGitBean.Gift> listBeanList = new ArrayList<>();;
    public static MyGiftListFragment getInstance(int type) {
        MyGiftListFragment c2CFragment = new MyGiftListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        c2CFragment.setArguments(bundle);
        return c2CFragment;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //setUserVisibleHint()有可能在fragment的生命周期外被调用
        if (rootView == null || !bInit) {
            return;
        }

        if (isFirstVisible && isVisibleToUser) {
            onFragmentFirstVisible();
            isFirstVisible = false;
        }

        refreshLayout.autoRefresh();
    }

    protected void onFragmentFirstVisible() {
        refreshLayout.autoRefresh();
    }

    private void initVariable() {
        isFirstVisible = true;
        rootView = null;
    }

    private View rootView;
    Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariable();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        initVariable();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.view_smartrecycleview, null);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void displayLoadingPopup() {

    }

    private boolean isFirstVisible;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        type = bundle.getInt("type");
        init();

        if (getUserVisibleHint()) {
            if (isFirstVisible) {
                onFragmentFirstVisible();
                isFirstVisible = false;
            }
        }

        new MyGiftListPresenter(Injection.provideTasksRepository(getActivity()), this);
    }

    @Override
    public void hideLoadingPopup() {

    }

    @Override
    public void onDestroyView() {
        if (unbinder!=null)
            unbinder.unbind();
        super.onDestroyView();
    }


    private boolean bInit = false;
    private void init(){
        bInit = true;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                HashMap map = new HashMap<>();
                map.put("status", type);
                presenter.getList(map);
            }
        });
        refreshLayout.setEnableLoadMore(false);


        adapter = new MyGiftAdapter(getActivity(),listBeanList);
        adapter.setDoUseGiftCallback(new MyGiftAdapter.doUseGiftCallback() {
            @Override
            public void doUseGift(int id) {
                HashMap hashMap = new HashMap();
                hashMap.put("id",id);
                presenter.doUseGift(hashMap);
            }
        });
        adapter.setType(type);
        adapter.bindToRecyclerView(recyclerView);

    }


    @Override
    public void doUseGiftSuccess(String msg) {
        ToastUtils.showToast(msg);
        refreshLayout.autoRefresh();
    }

    @Override
    public void doUseGiftFaild(Integer code, String obj) {
        NetCodeUtils.checkedErrorCode(this,code,obj);
    }

    @Override
    public void getListSuccess(FreshGitBean freshGitBean) {
        try {
            refreshLayout.finishRefresh();
//            refreshLayout.finishLoadMore();

            List<FreshGitBean.Gift> giftList = freshGitBean.getList();

            if (giftList != null && giftList.size() > 0) {
                listBeanList.clear();
                listBeanList.addAll(giftList);
                adapter.notifyDataSetChanged();
            } else {
                listBeanList.clear();
                adapter.notifyDataSetChanged();
                adapter.setEmptyView(R.layout.empty_no_message);
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
    public void setPresenter(MyGiftContract.Presenter presenter) {
        this.presenter = presenter;
    }


}
