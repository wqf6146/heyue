package com.spark.szhb_master.entity;

import java.util.List;

public class SymbolListBean {

    private List<Symbol> list;

    public void setSymbolList(List<Symbol> symbolList) {
        this.list = symbolList;
    }

    public List<Symbol> getSymbolList() {
        return list;
    }

    public class Symbol {
        private String leverage;
        private String mark;

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
