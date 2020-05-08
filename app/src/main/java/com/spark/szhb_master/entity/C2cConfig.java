package com.spark.szhb_master.entity;

import java.io.Serializable;

public class C2cConfig implements Serializable {

    private double buy_price;
    private double fee;
    private double max_num;
    private double min_num;
    private double sell_price;


    public void setBuy_price(double buy_price) {
        this.buy_price = buy_price;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public void setMax_num(double max_num) {
        this.max_num = max_num;
    }

    public void setMin_num(double min_num) {
        this.min_num = min_num;
    }

    public void setSell_price(double sell_price) {
        this.sell_price = sell_price;
    }

    public double getBuy_price() {
        return buy_price;
    }

    public double getFee() {
        return fee;
    }

    public double getMax_num() {
        return max_num;
    }

    public double getMin_num() {
        return min_num;
    }

    public double getSell_price() {
        return sell_price;
    }
}
