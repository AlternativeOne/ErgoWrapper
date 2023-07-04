package com.lexoff.ergowrapper.info;

import java.util.ArrayList;

public class ExtentedConnDevicesInfo extends Info {
    private ArrayList<ExtentedConnDevice> connDevicesList;

    public ExtentedConnDevicesInfo() {
        connDevicesList = new ArrayList<>();
    }

    public ArrayList<ExtentedConnDevice> getDevices() {
        return connDevicesList;
    }

    public void addDevice(ExtentedConnDevice device) {
        connDevicesList.add(device);
    }
}
