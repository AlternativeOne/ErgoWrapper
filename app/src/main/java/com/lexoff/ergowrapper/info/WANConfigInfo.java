package com.lexoff.ergowrapper.info;

public class WANConfigInfo extends Info {
    private String proto;
    private String dialSwitch;

    public WANConfigInfo(){
        //empty
    }


    public String getProto() {
        return proto;
    }

    public void setProto(String proto) {
        this.proto = proto;
    }

    public String getDialSwitch() {
        return dialSwitch;
    }

    public void setDialSwitch(String dialSwitch) {
        this.dialSwitch = dialSwitch;
    }
}
