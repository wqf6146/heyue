package com.spark.szhb_master.entity;

public class C2cOrderDetail {

    /**
     * cancel_type : 1
     * card : 1237182368127
     * card_name : 杭州银行
     * created_at : 2020-05-15 20:37:28 +0800 CST
     * id : 56
     * name : 官方快捷交易
     * num : 1
     * price : 21
     * status : 1
     * stop_at : 0001-01-01T00:00:00Z
     * time : 2020-05-15 20:47:28 +0800 CST
     * trade_num : 26
     * trade_rate : 0
     * type : 1
     */

    private int cancel_type;
    private String card;
    private String card_name;
    private String created_at;
    private int id;
    private String name;
    private Double num;
    private Double price;
    private int status;
    private String stop_at;
    private String time;
    private Double trade_num;
    private Double trade_rate;
    private int type;

    public int getCancel_type() {
        return cancel_type;
    }

    public void setCancel_type(int cancel_type) {
        this.cancel_type = cancel_type;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getNum() {
        return num;
    }

    public void setNum(Double num) {
        this.num = num;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStop_at() {
        return stop_at;
    }

    public void setStop_at(String stop_at) {
        this.stop_at = stop_at;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getTrade_num() {
        return trade_num;
    }

    public void setTrade_num(Double trade_num) {
        this.trade_num = trade_num;
    }

    public Double getTrade_rate() {
        return trade_rate;
    }

    public void setTrade_rate(Double trade_rate) {
        this.trade_rate = trade_rate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
