package com.lexoff.ergowrapper.info;

public class LANIPInfo extends Info {
    private String lanIP;
    private String networkMask;

    public LANIPInfo(){
        //empty
    }

    public String getLanIP() {
        return lanIP;
    }

    public void setLanIP(String lanIP) {
        this.lanIP = lanIP;
    }

    public String getNetworkMask() {
        return networkMask;
    }

    public void setNetworkMask(String networkMask) {
        this.networkMask = networkMask;
    }
}
