package com.lexoff.ergowrapper.info;

import com.lexoff.ergowrapper.Utils;

public class WiFi extends Info {
    private String SSID;
    private String MAC;

    private boolean is24Hz;
    private boolean is5G;

    private int channel;

    private String encryption;

    private boolean enabled;

    public WiFi(){
        //empty
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public String getMAC() {
        return MAC.toUpperCase();
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public String getEncryption() {
        return Utils.formatEncryption(encryption);
    }

    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getStatus(){
        return enabled ? "Enabled" : "Disabled";
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getChannelAsString(){
        return Utils.formatChannel(channel);
    }

    public boolean is24Hz() {
        return is24Hz;
    }

    public void set24Hz(boolean is24Hz) {
        this.is24Hz = is24Hz;
    }

    public boolean is5G() {
        return is5G;
    }

    public void setIs5G(boolean is5G) {
        this.is5G = is5G;
    }
}
