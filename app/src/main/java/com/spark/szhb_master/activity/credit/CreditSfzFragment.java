package com.spark.szhb_master.activity.credit;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.spark.szhb_master.R;
import com.spark.szhb_master.base.BaseFragment;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class CreditSfzFragment extends BaseFragment implements CreditContract.View  {

    @BindView(R.id.et_name)
    EditText etName;

    @BindView(R.id.et_code)
    EditText etCode;

    @BindView(R.id.tv_nextstep)
    TextView tvNextStep;

//    private int type;
//

    private CreditContract.Presenter presenter;

    public static CreditSfzFragment getInstance(int type) {
        CreditSfzFragment creditFragment = new CreditSfzFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        creditFragment.setArguments(bundle);
        return creditFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sfz;
    }

    @Override
    protected void initData() {
        super.initData();
//        Bundle bundle = getArguments();
//        type = bundle.getInt("type");
    }

    @Override
    public void setPresenter(CreditContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void doCreditSuccess(String obj) {
        ToastUtils.showToast("初级认证成功");
        showActivity(CreditSfzActivity.class,null);
        finish();
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    @OnClick({R.id.tv_nextstep})
    @Override
    protected void setOnClickListener(View v) {
        switch (v.getId()){
            case R.id.tv_nextstep:
                String idCard = etCode.getText().toString().trim();
                String realName = etName.getText().toString().trim();

                if (StringUtils.isEmpty(realName, idCard)) {
                    ToastUtils.showToast(getString(R.string.incomplete_information));
                    break;
                }

//                if (!StringUtils.isIDCard(idCard)) {
//                    ToastUtils.showToast(getString(R.string.idcard_diff));
//                    break;
//                }
//                Bundle bundle = new Bundle();
//                bundle.putString("name",realName);
//                bundle.putString("id",idCard);
//                bundle.putInt("type",1);
//                showActivity(CreditSfzActivity.class,bundle);

                HashMap map = new HashMap<>();
                map.put("real_name", realName);
                map.put("id_card", idCard);
                presenter.credit(map);
                break;
        }
    }
}
