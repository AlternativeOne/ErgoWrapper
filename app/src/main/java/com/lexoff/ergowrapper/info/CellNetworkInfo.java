package com.lexoff.ergowrapper.info;

import com.lexoff.ergowrapper.Utils;

public class CellNetworkInfo extends Info {
    private String networkName;
    private String IMEI;

    private int connectionStatus;

    private int pdpType;
    private int ipType;

    private boolean isRoaming;
    private String apnName;

    private String Ipv4Addr;
    private String Ipv4Dns1;
    private String Ipv4Dns2;
    private String Ipv4GateWay;
    private String Ipv4NetMask;

    private String Ipv6Addr;
    private String Ipv6Dns1;
    private String Ipv6Dns2;
    private String Ipv6GateWay;
    private String Ipv6NetMask;

    private int rssi;
    private int sysMode;
    private int dataMode;
    
    public CellNetworkInfo(){
        //empty
    }


    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public void setConnectionStatus(int connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getSysMode() {
        return sysMode;
    }

    public void setSysMode(int sysMode) {
        this.sysMode = sysMode;
    }

    public int getDataMode() {
        return dataMode;
    }

    public void setDataMode(int dataMode) {
        this.dataMode = dataMode;
    }

    public String getConnectionQuality(){
        return Utils.formatConnectionQuality(rssi, sysMode, dataMode);
    }

    public String getConnectionStatus(){
        if (connectionStatus==0) return "Disconnected";
        else if (connectionStatus==1) return "Connected";
        else if (connectionStatus==2) return "Connecting";

        return "Disconnected";
    }

    public String getNetworkMode(){
        if (0 == sysMode) {
            return "No service";
        } else if (1 == sysMode) {
            if (dataMode != 1 && dataMode != 2 && dataMode != 16) {
                return "3G";
            } else {
                return "2G";
            }
        } else if (2 == sysMode) {
            return "LTE";
        }

        return "";
    }

    public String getDataConnectionMode(){
        if (1 == dataMode) {
            return "GPRS";
        } else if (2 == dataMode) {
            return "EDGE";
        } else if (3 == dataMode) {
            return "UMTS";
        } else if (4 == dataMode) {
            return "IS95A";
        } else if (5 == dataMode) {
            return "IS95B";
        } else if (6 == dataMode) {
            return "1xRTT";
        } else if (7 == dataMode) {
            return "EVD0_0";
        } else if (8 == dataMode) {
            return "EVD0_A";
        } else if (9 == dataMode) {
            return "HSUPA";
        } else if (10 == dataMode) {
            return "HSDPA";
        } else if (11 == dataMode) {
            return "HSPA";
        } else if (12 == dataMode) {
            return "EVD0_B";
        } else if (13 == dataMode) {
            return "EHRPD";
        } else if (14 == dataMode) {
            return "LTE";
        } else if (15 == dataMode) {
            return "HSPAP";
        } else if (16 == dataMode) {
            return "GSM";
        } else if (17 == dataMode) {
            return "TD_SCDMA";
        } else if (19 == dataMode) {
            return "4G+";
        }

        return "";
    }

    public int getPdpType() {
        return pdpType;
    }

    public void setPdpType(int pdpType) {
        this.pdpType = pdpType;
    }

    public int getIpType() {
        return ipType;
    }

    public void setIpType(int ipType) {
        this.ipType = ipType;
    }

    public String getApnName() {
        return apnName;
    }

    public void setApnName(String apnName) {
        this.apnName = apnName;
    }

    public String getIpv4Addr() {
        return Ipv4Addr;
    }

    public void setIpv4Addr(String ipv4Addr) {
        Ipv4Addr = ipv4Addr;
    }

    public String getIpv4Dns1() {
        return Ipv4Dns1;
    }

    public void setIpv4Dns1(String ipv4Dns1) {
        Ipv4Dns1 = ipv4Dns1;
    }

    public String getIpv4Dns2() {
        return Ipv4Dns2;
    }

    public void setIpv4Dns2(String ipv4Dns2) {
        Ipv4Dns2 = ipv4Dns2;
    }

    public String getIpv4GateWay() {
        return Ipv4GateWay;
    }

    public void setIpv4GateWay(String ipv4GateWay) {
        Ipv4GateWay = ipv4GateWay;
    }

    public String getIpv4NetMask() {
        return Ipv4NetMask;
    }

    public void setIpv4NetMask(String ipv4NetMask) {
        Ipv4NetMask = ipv4NetMask;
    }

    public String getIpv6Addr() {
        return Ipv6Addr;
    }

    public void setIpv6Addr(String ipv6Addr) {
        Ipv6Addr = ipv6Addr;
    }

    public String getIpv6Dns1() {
        return Ipv6Dns1;
    }

    public void setIpv6Dns1(String ipv6Dns1) {
        Ipv6Dns1 = ipv6Dns1;
    }

    public String getIpv6Dns2() {
        return Ipv6Dns2;
    }

    public void setIpv6Dns2(String ipv6Dns2) {
        Ipv6Dns2 = ipv6Dns2;
    }

    public String getIpv6GateWay() {
        return Ipv6GateWay;
    }

    public void setIpv6GateWay(String ipv6GateWay) {
        Ipv6GateWay = ipv6GateWay;
    }

    public String getIpv6NetMask() {
        return Ipv6NetMask;
    }

    public void setIpv6NetMask(String ipv6NetMask) {
        Ipv6NetMask = ipv6NetMask;
    }

    public boolean isRoaming() {
        return isRoaming;
    }

    public void setRoaming(boolean roaming) {
        isRoaming = roaming;
    }
}
