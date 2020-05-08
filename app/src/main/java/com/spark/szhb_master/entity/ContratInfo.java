package com.spark.szhb_master.entity;

public class ContratInfo {

    private int hidden_leverage;
    private int num_min;
    private double overnight_fee;
    private int leverage;

    public void setOvernight_fee(double overnight_fee) {
        this.overnight_fee = overnight_fee;
    }

    public void setLeverage(int leverage) {
        this.leverage = leverage;
    }

    public void setHidden_leverage(int hidden_leverage) {
        this.hidden_leverage = hidden_leverage;
    }

    public void setNum_min(int num_min) {
        this.num_min = num_min;
    }

    public double getOvernight_fee() {
        return overnight_fee;
    }

    public int getHidden_leverage() {
        return hidden_leverage;
    }

    public int getLeverage() {
        return leverage;
    }

    public int getNum_min() {
        return num_min;
    }
}
