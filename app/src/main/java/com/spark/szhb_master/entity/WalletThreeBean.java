package com.spark.szhb_master.entity;

import java.util.List;

public class WalletThreeBean {

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

        private String created_at;  // 到账时间 提币时间    转移时间
        private String from_address; //发起地址
        private int id;
        private double num;
        private String to_address;//   到帐地址
        private int type;           //类型: 0ERC20 ，1omni


        private String address;     //提币地址
        private int status;         //状态：0未到账1已到账
        private String stop_at;     //到账时间;

        public void setStatus(int status) {
            this.status = status;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public void setNum(double num) {
            this.num = num;
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setFrom_address(String from_address) {
            this.from_address = from_address;
        }

        public void setStop_at(String stop_at) {
            this.stop_at = stop_at;
        }

        public void setTo_address(String to_address) {
            this.to_address = to_address;
        }

        public int getStatus() {
            return status;
        }

        public String getCreated_at() {
            return created_at;
        }

        public int getType() {
            return type;
        }

        public int getId() {
            return id;
        }

        public double getNum() {
            return num;
        }

        public String getAddress() {
            return address;
        }

        public String getFrom_address() {
            return from_address;
        }

        public String getTo_address() {
            return to_address;
        }

        public String getStop_at() {
            return stop_at;
        }
    }
}
