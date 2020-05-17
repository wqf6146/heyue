package com.spark.szhb_master.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.szhb_master.R;
import com.spark.szhb_master.entity.WalletThreeBean;

import java.util.List;

public class WalletThreeAdapter extends BaseQuickAdapter<WalletThreeBean.ListBean, BaseViewHolder> {

    private int type; //0充币 1提币 2资金划转

    public WalletThreeAdapter(int type,List<WalletThreeBean.ListBean> listBeans){
        super(R.layout.item_walletthreedetail,listBeans);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, WalletThreeBean.ListBean item) {
        if (type == 0){
            helper.setText(R.id.tvRechargeDate,item.getCreated_at()).setVisible(R.id.rlRechargeDate,true);
            helper.setText(R.id.tvRechargeEndAddress,item.getTo_address()).setVisible(R.id.rlRechargeEndAddress,true);
            helper.setText(R.id.tvRechargeNum,String.valueOf(item.getNum())).setVisible(R.id.rlRechargeNum,true);
            helper.setText(R.id.tvRechargeStartAddress,String.valueOf(item.getFrom_address())).setVisible(R.id.rlRechargeStartAddress,true);
            helper.setVisible(R.id.rlRechargeStatus,true);
        }else if (type == 1){
            helper.setText(R.id.tvExtractAddress,item.getAddress()).setVisible(R.id.rlExtractAddress,true);
            helper.setText(R.id.tvExtractDate,item.getCreated_at()).setVisible(R.id.rlExtractDate,true);
            helper.setText(R.id.tvExtractNum,String.valueOf(item.getNum())).setVisible(R.id.rlExtractNum,true);
            helper.setText(R.id.tvExtractDoneDate,String.valueOf(item.getStop_at())).setVisible(R.id.rlExtractDoneDate,true);
            helper.setText(R.id.tvExtractStatus,item.getStatus() == 0 ? "未到账" : "已到账").setVisible(R.id.rlExtractStatus,true);
        }else{
            helper.setText(R.id.tvTransferDate,item.getCreated_at()).setVisible(R.id.rlTransferDate,true);
            helper.setText(R.id.tvTransferNum,String.valueOf(item.getNum())).setVisible(R.id.rlTransferNum,true);
            helper.setText(R.id.tvTransferType,item.getType() == 0 ? "合约账户至资金账户" : "资金账户至合约账户").setVisible(R.id.rlTransferType,true);
        }
    }
}
