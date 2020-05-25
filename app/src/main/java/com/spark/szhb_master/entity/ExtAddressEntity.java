package com.spark.szhb_master.entity;

import java.util.List;

public class ExtAddressEntity {
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
        private String address;
        private int id;
        private String name;
        private int type;


        public void setAddress(String address) {
            this.address = address;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public int getId() {
            return id;
        }

        public int getType() {
            return type;
        }

        public String getName() {
            return name;
        }
    }
}
