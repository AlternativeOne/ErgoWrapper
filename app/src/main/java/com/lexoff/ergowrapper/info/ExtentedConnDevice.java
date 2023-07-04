package com.lexoff.ergowrapper.info;

import com.lexoff.ergowrapper.Utils;

public class ExtentedConnDevice extends ConnectedDevice {

    private String status;

    private String lastConnTime;
    private long totalConnTime;

    public ExtentedConnDevice(){
        super();

        //empty
    }

    public String getStatus() {
        if (status.equals("conn")) return "Connected";
        else if (status.equals("dis_conn")) return "Disconnected";
        else return "Blocked";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastConnectionTime() {
        return lastConnTime.replaceAll("-", ".");
    }

    public void setLastConnectionTime(String lastConnTime) {
        this.lastConnTime = lastConnTime;
    }

    public void setTotalConnectionTime(long totalConnTime) {
        this.totalConnTime = totalConnTime;
    }

    public String getTotalConnectionTime() {
        return Utils.formatSeconds(totalConnTime);
    }

    public boolean isBlocked(){
        return status.equals("block");
    }
}
