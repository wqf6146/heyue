package com.spark.szhb_master.entity;

public class TradeCOM {
    private int limit_buy;
    private int limit_sell;
    private int market_buy;
    private int market_sell;

    public void setLimit_buy(int limit_buy) {
        this.limit_buy = limit_buy;
    }

    public void setLimit_sell(int limit_sell) {
        this.limit_sell = limit_sell;
    }

    public void setMarket_buy(int market_buy) {
        this.market_buy = market_buy;
    }

    public void setMarket_sell(int market_sell) {
        this.market_sell = market_sell;
    }

    public int getLimit_buy() {
        return limit_buy;
    }

    public int getLimit_sell() {
        return limit_sell;
    }

    public int getMarket_buy() {
        return market_buy;
    }

    public int getMarket_sell() {
        return market_sell;
    }
}
