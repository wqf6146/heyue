package com.spark.szhb_master.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.spark.szhb_master.R;
import com.spark.szhb_master.entity.BankEntity;
import com.spark.szhb_master.entity.NewEntrust;
import com.spark.szhb_master.utils.DateUtils;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.PermissionUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.Date;
import java.util.List;

/**
 * 委托
 * author: wuzongjie
 * time  : 2018/4/17 0017 10:18
 * desc  : 当前委托的适配器
 */

public class BankItemAdapter extends BaseQuickAdapter<BankEntity.ListBean, BaseViewHolder> {

    private OnItemCheckChange onItemCheckChange;

    private Activity mActivity;

    public BankItemAdapter(Activity activity, @Nullable List<BankEntity.ListBean> data) {
        super(R.layout.item_bank, data);
        mActivity = activity;
    }

    public void setOnItemCheckChange(OnItemCheckChange onItemCheckChange) {
        this.onItemCheckChange = onItemCheckChange;
    }

    @Override
    protected void convert(BaseViewHolder helper, BankEntity.ListBean item) {

        helper.setText(R.id.tvName,item.getName()).setText(R.id.tvAccount,item.getCard())
                .setText(R.id.tvBankHang,item.getBank()).setText(R.id.tvBankBrang,item.getSubbranch());

        if (item.getIs_open() == 1){
            ((Switch)helper.getView(R.id.swEnable)).setChecked(true);
        }else{
            ((Switch)helper.getView(R.id.swEnable)).setChecked(false);
        }

        if (item.getStatus() == 0){
            helper.setText(R.id.tvSfk,"用于收款");
        }else if (item.getStatus() == 1){
            helper.setText(R.id.tvSfk,"用于付款");
        }else{
            helper.setText(R.id.tvSfk,"用于收付款");
        }

        helper.getView(R.id.tvUnBind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemCheckChange!=null)
                    onItemCheckChange.deleteItem(item.getId());
            }
        });

        ((Switch)helper.getView(R.id.swEnable)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if (onItemCheckChange!=null)
                        onItemCheckChange.onItemCheckChange(item.getId(),1,item.getStatus());
                }else {
                    if (onItemCheckChange!=null)
                        onItemCheckChange.onItemCheckChange(item.getId(),0,item.getStatus());
                }
            }
        });

        helper.getView(R.id.tvSfk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] stringItems = {"收款","付款","收付款"};
                final ActionSheetDialog dialog = new ActionSheetDialog(mActivity, stringItems, null);
                dialog.isTitleShow(false).show();
                dialog.setOnOperItemClickL(new OnOperItemClickL() {
                    @Override
                    public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                if (onItemCheckChange!=null)
                                    onItemCheckChange.onItemCheckChange(item.getId(),item.getIs_open(),0);
                                break;
                            case 1:
                                if (onItemCheckChange!=null)
                                    onItemCheckChange.onItemCheckChange(item.getId(),item.getIs_open(),1);
                                break;
                            case 2:
                                if (onItemCheckChange!=null)
                                    onItemCheckChange.onItemCheckChange(item.getId(),item.getIs_open(),2);
                                break;
                        }
                        dialog.dismiss();
                    }
                });
            }
        });

    }

    public interface OnItemCheckChange {
        void onItemCheckChange(int bankid,int isOpen,int status);
        void deleteItem(int id);
    }
}
