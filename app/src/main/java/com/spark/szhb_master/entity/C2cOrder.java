package com.spark.szhb_master.entity;

import java.io.Serializable;
import java.util.List;

public class C2cOrder {

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

    public static class ListBean implements Serializable {
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

        private int cancel_type;
        private String card;
        private String card_name;
        private String created_at;
        private int id;
        private String name;
        private double num;
        private double price;
        private int status;
        private String stop_at;
        private String time;
        private int trade_num;
        private int trade_rate;
        private int type;

        public void setCard(String card) {
            this.card = card;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setTrade_rate(int trade_rate) {
            this.trade_rate = trade_rate;
        }

        public void setTrade_num(int trade_num) {
            this.trade_num = trade_num;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public void setNum(double num) {
            this.num = num;
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setCancel_type(int cancel_type) {
            this.cancel_type = cancel_type;
        }

        public void setCard_name(String card_name) {
            this.card_name = card_name;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public void setStop_at(String stop_at) {
            this.stop_at = stop_at;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getCard() {
            return card;
        }

        public int getStatus() {
            return status;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getTrade_rate() {
            return trade_rate;
        }

        public int getTrade_num() {
            return trade_num;
        }

        public int getType() {
            return type;
        }

        public double getNum() {
            return num;
        }

        public double getPrice() {
            return price;
        }

        public int getCancel_type() {
            return cancel_type;
        }

        public String getCard_name() {
            return card_name;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getStop_at() {
            return stop_at;
        }

        public String getTime() {
            return time;
        }
    }
}
