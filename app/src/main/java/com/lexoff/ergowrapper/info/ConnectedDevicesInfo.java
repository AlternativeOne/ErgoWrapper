package com.lexoff.ergowrapper.info;

import java.util.ArrayList;

public class ConnectedDevicesInfo extends Info {
    private ArrayList<ConnectedDevice> connDevicesList;

    public ConnectedDevicesInfo(){
        connDevicesList=new ArrayList<>();
    }

    public ArrayList<ConnectedDevice> getDevices(){
        return connDevicesList;
    }

    public void addDevice(ConnectedDevice device){
        connDevicesList.add(device);
    }
}
