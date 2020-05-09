package com.spark.szhb_master.entity;

import com.spark.szhb_master.factory.socket.NEWCMD;

public class TcpEntity {
    private String tcpKey;
    private String status;
    private NEWCMD newcmd;

    public TcpEntity(String key,NEWCMD cmd){
        tcpKey = key;
        newcmd = cmd;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setNewcmd(NEWCMD newcmd) {
        this.newcmd = newcmd;
    }

    public String getStatus() {
        return status;
    }

    public NEWCMD getNewcmd() {
        return newcmd;
    }

    public String getTcpKey() {
        return tcpKey;
    }

    public void setTcpKey(String tcpKey) {
        this.tcpKey = tcpKey;
    }
}
