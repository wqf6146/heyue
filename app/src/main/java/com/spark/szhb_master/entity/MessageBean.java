package com.spark.szhb_master.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/2/6.
 */

public class MessageBean {

    private int page;
    private int total_count;

    public void setList(List<Message> list) {
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

    public List<Message> getList() {
        return list;
    }

    private List<Message> list;

    public static class Message implements Serializable {

        private String id;
        private String title;
        private String body;
        private String created_at;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getBody() {
            return body;
        }
    }
}
