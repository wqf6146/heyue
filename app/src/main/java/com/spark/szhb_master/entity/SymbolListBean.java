package com.spark.szhb_master.entity;

import java.io.Serializable;
import java.util.List;

public class SymbolListBean implements Serializable{

    private List<Symbol> list;

    public void setSymbolList(List<Symbol> symbolList) {
        this.list = symbolList;
    }

    public List<Symbol> getSymbolList() {
        return list;
    }

    public class Symbol implements Serializable {
        private static final long serialVersionUID = -2083503801433301445L;
        private String leverage;
        private String mark;
        private String depth_config;
        private String depth_default;

        public void setDepth_config(String depth_config) {
            this.depth_config = depth_config;
        }

        public void setDepth_default(String depth_default) {
            this.depth_default = depth_default;
        }

        public String getDepth_config() {
            return depth_config;
        }

        public String getDepth_default() {
            return depth_default;
        }

        public void setLeverage(String leverage) {
            this.leverage = leverage;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }

        public String getMark() {
            return mark;
        }

        public String getLeverage() {
            return leverage;
        }
    }
}
