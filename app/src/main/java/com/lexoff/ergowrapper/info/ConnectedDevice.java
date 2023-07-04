package com.lexoff.ergowrapper.info;

import com.lexoff.ergowrapper.Utils;

public class ConnectedDevice extends Info {
    private String name;
    private String ip;
    private String mac;
    private String connType;
    private long connTime;

    public ConnectedDevice(){
        //empty
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIP() {
        return ip;
    }

    public void setIP(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getConnectionType() {
        return connType;
    }

    public void setConnectionType(String connType) {
        this.connType = connType;
    }

    public String getConnectionTime() {
        return Utils.formatSeconds(connTime);
    }

    public void setConnectionTime(long connTime) {
        this.connTime = connTime;
    }
}
