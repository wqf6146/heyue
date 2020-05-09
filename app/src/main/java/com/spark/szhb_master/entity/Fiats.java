package com.spark.szhb_master.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/2/28.
 */

public class Fiats {
    private int page;
    private int total_count;

    public void setList(List<FiatsBean> list) {
        this.list = list;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public int getPage() {
        return page;
    }

    public int getTotal_count() {
        return total_count;
    }

    public List<FiatsBean> getList() {
        return list;
    }

    private List<FiatsBean> list;

    public static class FiatsBean implements Serializable {

        private String created_at;
        private int status;
        private int type;
        private double volume;
        private int id;
        private double max_num;
        private double min_num;
        private String name;
        private double num;
        private double price;
        private double trade_num;
        private double trade_rate;

        public double getVolume() {
            return volume;
        }

        public int getStatus() {
            return status;
        }

        public int getType() {
            return type;
        }

        public void setVolume(double volume) {
            this.volume = volume;
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setNum(double num) {
            this.num = num;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setMin_num(double min_num) {
            this.min_num = min_num;
        }

        public void setMax_num(double max_num) {
            this.max_num = max_num;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public void setTrade_num(double trade_num) {
            this.trade_num = trade_num;
        }

        public void setTrade_rate(double trade_rate) {
            this.trade_rate = trade_rate;
        }

        public double getMin_num() {
            return min_num;
        }

        public double getMax_num() {
            return max_num;
        }

        public int getId() {
            return id;
        }

        public double getNum() {
            return num;
        }

        public double getPrice() {
            return price;
        }

        public double getTrade_num() {
            return trade_num;
        }

        public double getTrade_rate() {
            return trade_rate;
        }

        public String getName() {
            return name;
        }
    }
}
