package com.spark.szhb_master.activity.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.MessageAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.MessageBean;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.NetCodeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import config.Injection;

/**
 * 平台消息
 */
public class MessageActivity extends BaseActivity implements MessageContract.View {

    @BindView(R.id.recycleview)
    RecyclerView rvMessage;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.ivBack)
    ImageView ivBack;

    private int pageNo = 1;
    private MessageContract.Presenter presenter;
    private List<MessageBean.Message> messages = new ArrayList<>();
    private MessageAdapter adapter;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setListener() {
        super.setListener();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                pageNo = 1;

                HashMap map = new HashMap<>();
                map.put("pageNo", pageNo);
                map.put("pageSize", GlobalConstant.PageSize);
                presenter.message(map);
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MessageBean.Message message = (MessageBean.Message) adapter.getItem(position);

                Intent intent = new Intent(activity, MessageContentActivity.class);
                intent.putExtra("title",message.getTitle());
                intent.putExtra("time",message.getCreated_at());
                intent.putExtra("body",message.getBody());
                activity.startActivity(intent);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                HashMap map = new HashMap<>();
                map.put("pageNo", pageNo + 1);
                map.put("pageSize", GlobalConstant.PageSize);
                presenter.message(map);
            }
        });
    }



    @Override
    protected void initData() {
        super.initData();
        new MessagePresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        initRv();
    }


    private void initRv() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvMessage.setLayoutManager(manager);
        adapter = new MessageAdapter(R.layout.item_message, messages, this);
        adapter.bindToRecyclerView(rvMessage);
    }

    @Override
    protected void loadData() {
        refreshLayout.autoRefresh();
    }


    @Override
    public void setPresenter(MessageContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void messageSuccess(MessageBean messageBean) {

        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();

        List<MessageBean.Message> messageList = messageBean.getList();
        if (messageList != null && messageList.size() > 0) {
            if (messageBean.getPage() == 1) {
                this.messages.clear();
            }

            if (messageList.size() < GlobalConstant.PageSize){
                refreshLayout.finishLoadMoreWithNoMoreData();
            }

            pageNo = messageBean.getPage();
            this.messages.addAll(messageList);
            adapter.notifyDataSetChanged();
        } else {
            if (messageBean.getPage() == 1) {
                this.messages.clear();
                adapter.notifyDataSetChanged();
                adapter.setEmptyView(R.layout.empty_no_message);
            }
        }
    }

    @Override
    public void messageFail(Integer code, String toastMessage) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }
}
