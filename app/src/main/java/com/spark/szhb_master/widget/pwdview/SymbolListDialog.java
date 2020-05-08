package com.spark.szhb_master.widget.pwdview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.spark.szhb_master.R;
import com.spark.szhb_master.entity.NewCurrency;
import com.spark.szhb_master.entity.SymbolListBean;
import com.spark.szhb_master.utils.UIUtils;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author crazyZhangxl on 2018/10/29.
 * Describe:
 */
public class SymbolListDialog extends FrameLayout implements View.OnClickListener {
    private Animation mAnimationIn;
    private Animation mAnimationOut;
    private Animation mMaskAnimationIn;
    private Animation mMaskAnimationOut;
    private View mContentFrame;
    private View maskView;
    private boolean isShow;
    private TextView mTextView;
    private Drawable drawableUp, drawableDown;

    private List<SymbolListBean.Symbol> symbolList = new ArrayList<>();

    private ListView mListView;
    private CommonAdapter mAdapter;
    private OnItemClickCallback onItemClickCallback;

    public SymbolListDialog(@NonNull Context context, FrameLayout frameLayout, TextView textView) {
        super(context);
        initView(context);
        mTextView = textView;
        // 将布局加入,也就是将遮层布局放置在该布局之上
        frameLayout.addView(this,
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    private void initView(Context mContext) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_symbollistdialog, null);
        maskView = view.findViewById(R.id.mask);
        mContentFrame = view.findViewById(R.id.contentFm);
        mListView = view.findViewById(R.id.vp_listview);

        mAdapter = new CommonAdapter<SymbolListBean.Symbol>(getContext(),R.layout.item_symbol_list_pop,symbolList) {
            @Override
            protected void convert(ViewHolder viewHolder, final SymbolListBean.Symbol item, int position) {
                viewHolder.setText(R.id.tvBuySymbol,item.getMark());
                viewHolder.setText(R.id.tvSecSymbol,"/"+item.getLeverage());

                viewHolder.setOnClickListener(R.id.rlroot, new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickCallback.onitemClick(item);
                    }
                });
            }
        };


        mListView.setAdapter(mAdapter);

        maskView.setOnClickListener(this);
        addView(view);
        drawableUp = ContextCompat.getDrawable(mContext, R.mipmap.ic_arrow_top);
        drawableDown = ContextCompat.getDrawable(mContext, R.mipmap.ic_arrow_bottom);
        mAnimationIn = AnimationUtils.loadAnimation(mContext,R.anim.dd_menu_in);
        mAnimationOut = AnimationUtils.loadAnimation(mContext,R.anim.dd_menu_out);
        mMaskAnimationIn = AnimationUtils.loadAnimation(mContext,R.anim.dd_mask_in);
        mMaskAnimationOut = AnimationUtils.loadAnimation(mContext,R.anim.dd_mask_out);
        setVisibility(GONE);
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public void dataLoaded(List<SymbolListBean.Symbol> datas) {
        if (datas!=null){
            this.symbolList.clear();
            this.symbolList.addAll(datas);
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mask:
                dismiss();
                break;
        }
    }

    public void dismiss(){
        isShow = false;
        mContentFrame.setVisibility(GONE);
        mContentFrame.startAnimation(mAnimationOut);
        maskView.setVisibility(GONE);
        maskView.startAnimation(mMaskAnimationOut);
        UIUtils.modifyTextViewDrawable(mTextView, drawableDown, 2);
    }

    public void show(){
        isShow = true;
        setVisibility(VISIBLE);
        mContentFrame.setVisibility(VISIBLE);
        mContentFrame.startAnimation(mAnimationIn);
        maskView.setVisibility(VISIBLE);
        maskView.startAnimation(mMaskAnimationIn);
        UIUtils.modifyTextViewDrawable(mTextView, drawableUp, 2);
    }

    public boolean isHasShow(){
        return isShow;
    }

    public interface OnItemClickCallback {
        void onitemClick(SymbolListBean.Symbol symbol);
    }
}
