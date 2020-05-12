package com.spark.szhb_master.entity;

import java.util.List;

public class BankEntity {

    private List<ListBean> list;


    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {

        private String bank;
        private String card;
        private String name;
        private String subbranch;

        private int id;
        private int is_open;
        private int status;
        private int type;

        public void setName(String name) {
            this.name = name;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }

        public void setCard(String card) {
            this.card = card;
        }

        public void setIs_open(int is_open) {
            this.is_open = is_open;
        }

        public void setSubbranch(String subbranch) {
            this.subbranch = subbranch;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public int getStatus() {
            return status;
        }

        public int getIs_open() {
            return is_open;
        }

        public String getBank() {
            return bank;
        }

        public String getCard() {
            return card;
        }

        public String getSubbranch() {
            return subbranch;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
