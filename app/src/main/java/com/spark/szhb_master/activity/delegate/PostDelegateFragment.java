package com.spark.szhb_master.activity.delegate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spark.szhb_master.R;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.PriceTextWatcher;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.spark.szhb_master.widget.TextWatcher;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import config.Injection;

public class PostDelegateFragment extends Fragment implements PostDelegateContract.View{

    protected View rootView;

    private int mType;// 0buy 1sell
    Unbinder unbinder;

    @BindView(R.id.rlSubmit)
    RelativeLayout rlSubmit;

    @BindView(R.id.edPrice)
    EditText edPrice;

    @BindView(R.id.edNum)
    EditText edNum;

    @BindView(R.id.edMinNum)
    EditText edMinNum;

    @BindView(R.id.edMaxNum)
    EditText edMaxNum;

    @BindView(R.id.edRemark)
    EditText edRemark;

    @BindView(R.id.tvTotalPrice)
    TextView tvTotalPrice;

    private OnCallBackEvent onCallBackEvent;


    private PostDelegateContract.Presenter presenter;

    public static PostDelegateFragment newInstance(int type){
        PostDelegateFragment fragment = new PostDelegateFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        fragment.setArguments(bundle);
        return fragment;
    }

    private Double mPrice =0.0d;
    private int mNum = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_postdelegate, null);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);

        Bundle bundle = getArguments();
        mType = bundle.getInt("type");

        if (mType == 0){
            edPrice.setHint("请输入预期购买单价");
            edNum.setHint("请输入预期购买数量");
        }
        edPrice.addTextChangedListener(new PriceTextWatcher(edPrice).addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);

                try{
                    mPrice = Double.parseDouble(s.toString());

                    tvTotalPrice.setText(String.valueOf(new BigDecimal(mPrice).multiply(new BigDecimal(mNum))
                            .setScale(2, RoundingMode.UP).toString()));
                }catch (Exception e){

                }

            }
        }));

        edNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);

                try{
                    mNum = Integer.parseInt(s.toString());
                    tvTotalPrice.setText(String.valueOf(new BigDecimal(mPrice).multiply(new BigDecimal(mNum))
                            .setScale(2, RoundingMode.UP).toString()));
                }catch (Exception e){

                }
            }
        });

        rlSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String price = edPrice.getText().toString();
                String num = edNum.getText().toString();
                String minnum = edMinNum.getText().toString();
                String maxnum = edMaxNum.getText().toString();
                String remark = edRemark.getText().toString();

                if (StringUtils.isEmpty(price,num,minnum,maxnum,remark)){
                    ToastUtils.showToast("填写信息不完整");
                    return;
                }
                HashMap hashMap = new HashMap();
                hashMap.put("type",mType);
                hashMap.put("num",Integer.parseInt(num));
                hashMap.put("price",Double.parseDouble(price));
                hashMap.put("max_num",Integer.parseInt(maxnum));
                hashMap.put("min_num",Integer.parseInt(minnum));
                hashMap.put("remark",remark);
                presenter.submit(hashMap);

            }
        });

        presenter = new PostDelegatePresenter(Injection.provideTasksRepository(getActivity().getApplicationContext()), this);
    }

    public void setOnCallBackEvent(OnCallBackEvent onCallBackEvent) {
        this.onCallBackEvent = onCallBackEvent;
    }

    @Override
    public void setPresenter(PostDelegateContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void submitSuccess(String obj) {
        if (onCallBackEvent!=null)
            onCallBackEvent.submitSuccess(obj);
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this,code,toastMessage);
    }

    @Override
    public void hideLoadingPopup() {

    }

    @Override
    public void displayLoadingPopup() {

    }

    public interface OnCallBackEvent {
        void submitSuccess(String obj);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
