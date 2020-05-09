package com.spark.szhb_master.activity.entrust;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.TrustAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.dialog.EntrustDialog;
import com.spark.szhb_master.entity.Entrust;
import com.spark.szhb_master.entity.MarketSymbol;
import com.spark.szhb_master.entity.NewCurrency;
import com.spark.szhb_master.entity.NewEntrust;
import com.spark.szhb_master.entity.SymbolListBean;
import com.spark.szhb_master.utils.DpPxUtils;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.ToastUtils;
import com.spark.szhb_master.widget.SymbolDropDownDialog;
import com.spark.szhb_master.widget.pwdview.SymbolListDialog;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

/**
 * 当前委托的界面 和历史
 */
public class CurrentTrustActivity extends BaseActivity implements ITrustContract.View {

    @BindView(R.id.mRefresh)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView; // 列表控价


    @BindView(R.id.tvSymbol)
    TextView mTvSymbol;

    @BindView(R.id.allSpinner)
    NiceSpinner mSpinnerAll;

    @BindView(R.id.rltitle)
    RelativeLayout rlTitle;

    @BindView(R.id.ivBack)
    ImageView ivback;

    @BindView(R.id.tvCurrentTrust)
    TextView tvCurrentTrust;

    @BindView(R.id.tvHistroyTrust)
    TextView tvHistroyTrust;

    @BindView(R.id.flroot)
    FrameLayout flroot;



    private List<NewEntrust.ListBean> mData = new ArrayList<>();
    private TrustAdapter mAdapter;
    private int page = 1; // 页码
    private ITrustContract.Presenter mPresenter;
    private String mSymbol,mType;
    private EntrustDialog dialog;
    private String orderId;

    private boolean bCurOrHis = true;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_all_trust;
    }

    @Override
    protected void initView() {
//        setSetTitleAndBack(false, true);
        setImmersionBar(rlTitle);

        if (mSymbolDialog == null){
            mSymbolDialog = new SymbolListDialog(this,flroot,mTvSymbol);
            mSymbolDialog.setOnItemClickCallback(new SymbolListDialog.OnItemClickCallback() {
                @Override
                public void onitemClick(SymbolListBean.Symbol symbol) {
                    mSymbolDialog.dismiss();
                    mSymbolDialog.dismiss();
                    mTvSymbol.setText(symbol.getMark() + "/" + symbol.getLeverage());
                    mSymbol = symbol.getMark();
                    mType = symbol.getLeverage();
                    refreshText();
                }
            });
        }

    }

    @Override
    public void getSymbolHistroyListSuccess(NewEntrust newEntrust) {
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        mAdapter.setType(1);
        mAdapter.setEnableLoadMore(true);
        mAdapter.loadMoreComplete();
        if (newEntrust == null) return;
        if (page == 1) {
            this.mData.clear();
            if (newEntrust.getList().size() == 0) {
                mAdapter.loadMoreEnd();
                mAdapter.setEmptyView(R.layout.empty_no_message);
            } else {
                this.mData.addAll(newEntrust.getList());
            }
        } else {
            if (newEntrust.getList().size() != 0) this.mData.addAll(newEntrust.getList());
            else mAdapter.loadMoreEnd();
        }
        mAdapter.disableLoadMoreIfNotFullPage();
        mAdapter.notifyDataSetChanged();
    }

    private int type = -1;
    @Override
    protected void initData() {
        super.initData();
        dialog = new EntrustDialog(activity);

        mSpinnerAll.attachDataSource(new LinkedList<>(Arrays.asList(activity.getResources().getStringArray(R.array.alltype))));
        mSpinnerAll.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0){
                    //全部
                    type = 2;
                    refreshText();
                }else if (i == 1){
                    //做多
                    type = 0;
                    refreshText();
                }else{
                    //做空
                    type = 1;
                    refreshText();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        new TrustPresentImpl(Injection.provideTasksRepository(getApplicationContext()), this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mSymbol = bundle.getString("currency");
            mType = bundle.getString("type");

            mTvSymbol.setText(mSymbol+ "/" + mType);
        }
        initRv();


        mPresenter.getSymbolList();
    }

    /**
     * 初始化列表
     */
    private void initRv() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TrustAdapter(mData);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                HashMap hashMap = new HashMap();
                hashMap.put("limit_id",(int)view.getTag());
                mPresenter.undoLimitContrat(hashMap);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setEnableLoadMore(false);
    }

    @Override
    public void undoLimitContratSuccess(String msg) {
        ToastUtils.showToast("撤销成功");
        refreshText();
    }

    private SymbolListDialog mSymbolDialog;

    @OnClick({R.id.ivBack,R.id.tvSymbol,R.id.tvHistroyTrust,R.id.tvCurrentTrust})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);

        switch (v.getId()){
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvSymbol:
                mSymbolDialog.show();
                mSymbolDialog.dataLoaded(symbolListBean.getSymbolList());
                break;
            case R.id.tvCurrentTrust:
                bCurOrHis = true;
                tvCurrentTrust.setTextColor(getColor(R.color.white));
                tvHistroyTrust.setTextColor(getColor(R.color.tab_font));

                tvCurrentTrust.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
                tvHistroyTrust.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
                refreshText();
                break;
            case R.id.tvHistroyTrust:
                bCurOrHis = false;
                tvHistroyTrust.setTextColor(getColor(R.color.white));
                tvCurrentTrust.setTextColor(getColor(R.color.tab_font));

                tvHistroyTrust.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
                tvCurrentTrust.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
                refreshText();
                break;

        }

//        Bundle bundle = new Bundle();
//        bundle.putString("symbol", symbol);
//        showActivity(AllTrustActivity.class, bundle);
    }

    private SymbolListBean symbolListBean;

    @Override
    public void getSymbolListSuccess(SymbolListBean symbolListBean) {
        this.symbolListBean = symbolListBean;
        if (mSymbolDialog!=null && mSymbolDialog.isHasShow())
            mSymbolDialog.dataLoaded(symbolListBean.getSymbolList());
    }

    @Override
    protected void setListener() {
        super.setListener();
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMoreText();
            }
        }, mRecyclerView);
//        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                Entrust entrust = (Entrust) adapter.getItem(position);
//                orderId = entrust.getOrderId();
//                if (entrust != null) {
//                    dialog.setEntrust(entrust);
//                    dialog.show();
//                }
//            }
//        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshText();
            }
        });

        dialog.setOnCancelOrder(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.cancelOrder(orderId);
                dialog.dismiss();
            }
        });

    }

    @Override
    protected void loadData() {
        getList(true);
    }

    /**
     * 刷新
     */
    private void refreshText() {
        page = 1;
        mAdapter.setEnableLoadMore(false);
        getList(false);
    }

    /**
     * 下拉
     */
    private void loadMoreText() {
        refreshLayout.setEnabled(false);
        page++;
        getList(false);
    }

    private void getList(boolean isShow) {
        if (isShow)
            displayLoadingPopup();
        HashMap map = new HashMap<>();

        map.put("mark", mSymbol);
        map.put("leverage", mType);
        map.put("page", page);
        map.put("page_size", 20);
        map.put("type",type);


        if (bCurOrHis){
            mPresenter.getCurrentEntrust(map);
        }else{
            mPresenter.getSymbolHistroyList(map);
        }

    }

    @Override
    public void getCurrentEntrustSuccess(NewEntrust newEntrust) {
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        mAdapter.setType(1);
        mAdapter.setEnableLoadMore(true);
        mAdapter.loadMoreComplete();
        if (newEntrust == null) return;
        if (page == 1) {
            this.mData.clear();
            if (newEntrust.getList().size() == 0) {
                mAdapter.loadMoreEnd();
                mAdapter.setEmptyView(R.layout.empty_no_message);
            } else {
                this.mData.addAll(newEntrust.getList());
            }
        } else {
            if (newEntrust.getList().size() != 0) this.mData.addAll(newEntrust.getList());
            else mAdapter.loadMoreEnd();
        }
        mAdapter.disableLoadMoreIfNotFullPage();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void doPostFail(int e, String meg) {
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        mAdapter.setEnableLoadMore(true);
        mAdapter.loadMoreComplete();
    }

    @Override
    public void getCurrentOrderSuccess(List<NewEntrust.ListBean> obj) {
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        mAdapter.setEnableLoadMore(true);
        mAdapter.loadMoreComplete();
        if (obj == null) return;
        if (page == 1) {
            this.mData.clear();
            if (obj.size() == 0) {
                mAdapter.loadMoreEnd();
                mAdapter.setEmptyView(R.layout.empty_no_message);
            } else {
                this.mData.addAll(obj);
            }
        } else {
            if (obj.size() != 0) this.mData.addAll(obj);
            else mAdapter.loadMoreEnd();
        }
        mAdapter.disableLoadMoreIfNotFullPage();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void getSymbolSucccess(List<MarketSymbol> objs) {

    }

    @Override
    public void cancelOrderSuccess(String message) {
        getList(true);
    }

    @Override
    public void setPresenter(ITrustContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
