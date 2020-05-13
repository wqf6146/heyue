package com.spark.szhb_master.entity;

import java.util.List;

public class NewEntrust {

    /**
     * list : [{"consume_num":41.3329,"created_at":1587757308,"id":11,"leverage":"60","mark":"BTC","new_price":7535.7,"num":1,"price":6500,"type":1},{"consume_num":42.2078,"created_at":1587757301,"id":10,"leverage":"60","mark":"BTC","new_price":7535.7,"num":1,"price":7000,"type":0}]
     * page : 1
     * total_count : 2
     */

    private int page;
    private int total_count;
    private List<ListBean> list;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * consume_num : 41.3329
         * created_at : 1587757308
         * id : 11
         * leverage : 60
         * mark : BTC
         * new_price : 7535.7
         * num : 1
         * price : 6500
         * type : 1
         */

        private double consume_num;
        private int created_at;
        private int id;
        private String leverage;
        private String mark;
        private double new_price;
        private int num;
        private double price;
        private int type;

        private double fee;
        private double harvest_price;
        private double loss_price;
        private double overnight_fee;
        private double harvest_num;
        private String income_rate;

        public void setHarvest_num(double harvest_num) {
            this.harvest_num = harvest_num;
        }

        public void setIncome_rate(String income_rate) {
            this.income_rate = income_rate;
        }

        public double getHarvest_num() {
            return harvest_num;
        }

        public String getIncome_rate() {
            return income_rate;
        }

        public void setOvernight_fee(double overnight_fee) {
            this.overnight_fee = overnight_fee;
        }

        public void setLoss_price(double loss_price) {
            this.loss_price = loss_price;
        }

        public void setHarvest_price(double harvest_price) {
            this.harvest_price = harvest_price;
        }

        public void setFee(double fee) {
            this.fee = fee;
        }

        public double getFee() {
            return fee;
        }

        public double getHarvest_price() {
            return harvest_price;
        }

        public double getLoss_price() {
            return loss_price;
        }

        public double getOvernight_fee() {
            return overnight_fee;
        }

        public double getConsume_num() {
            return consume_num;
        }

        public void setConsume_num(double consume_num) {
            this.consume_num = consume_num;
        }

        public int getCreated_at() {
            return created_at;
        }

        public void setCreated_at(int created_at) {
            this.created_at = created_at;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLeverage() {
            return leverage;
        }

        public void setLeverage(String leverage) {
            this.leverage = leverage;
        }

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }

        public double getNew_price() {
            return new_price;
        }

        public void setNew_price(double new_price) {
            this.new_price = new_price;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
