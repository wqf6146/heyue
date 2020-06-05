package com.spark.szhb_master.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/2/6.
 */

public class FreshGitBean {

    private int page;
    private int total_count;

    public void setList(List<Gift> list) {
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

    public List<Gift> getList() {
        return list;
    }

    private List<Gift> list;

    public static class Gift implements Serializable {

        private int id;
        private int full_num;
        private String name;
        private String remark;
        private int is_received;
        private int num;
        private String created_at;
        private String usage_at;

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public void setUsage_at(String usage_at) {
            this.usage_at = usage_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUsage_at() {
            return usage_at;
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

        public int getNum() {
            return num;
        }

        public int getFull_num() {
            return full_num;
        }

        public int getIs_received() {
            return is_received;
        }

        public String getRemark() {
            return remark;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public void setFull_num(int full_num) {
            this.full_num = full_num;
        }

        public void setIs_received(int is_received) {
            this.is_received = is_received;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
