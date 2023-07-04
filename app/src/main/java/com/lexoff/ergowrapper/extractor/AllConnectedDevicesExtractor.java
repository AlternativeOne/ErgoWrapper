package com.lexoff.ergowrapper.extractor;

import com.lexoff.ergowrapper.Client;
import com.lexoff.ergowrapper.XMLUtils;
import com.lexoff.ergowrapper.info.ConnectedDevicesInfo;
import com.lexoff.ergowrapper.info.ExtentedConnDevice;
import com.lexoff.ergowrapper.info.ExtentedConnDevicesInfo;
import com.lexoff.ergowrapper.info.Info;

public class AllConnectedDevicesExtractor extends Extractor {
    private String PATH="xml_action.cgi?method=set";
    private String DATA="<?xml version=\"1.0\" encoding=\"US-ASCII\"?> <RGW><param><method>call</method><session>000</session><obj_path>statistics</obj_path><obj_method>get_all_clients_info</obj_method></param></RGW>";

    public AllConnectedDevicesExtractor(Client client){
        super(client, "");

        setUrl(BASE_URL+PATH);
        setData(DATA);
    }

    @Override
    protected Info buildInfo() {
        ExtentedConnDevicesInfo info=new ExtentedConnDevicesInfo();

        String clientsInfo=XMLUtils.getString(xmlStrResponse, "clients_info");

        int searchAfter=0;

        while (true){
            try {
                ExtentedConnDevice device = new ExtentedConnDevice();

                int macOpen = clientsInfo.indexOf("<mac>", searchAfter) + "<mac>".length();
                int macClose = clientsInfo.indexOf("</mac>", searchAfter);

                String mac = clientsInfo.substring(macOpen, macClose);

                device.setMac(mac);

                int connTypeOpen = clientsInfo.indexOf("<type>", searchAfter) + "<type>".length();
                int connTypeClose = clientsInfo.indexOf("</type>", searchAfter);

                String connType = clientsInfo.substring(connTypeOpen, connTypeClose);

                device.setConnectionType(connType);

                int ipOpen = clientsInfo.indexOf("<ip>", searchAfter) + "<ip>".length();
                int ipClose = clientsInfo.indexOf("</ip>", searchAfter);

                String ip = clientsInfo.substring(ipOpen, ipClose);

                device.setIP(ip);

                int nameOpen = clientsInfo.indexOf("<name>", searchAfter) + "<name>".length();
                int nameClose = clientsInfo.indexOf("</name>", searchAfter);

                String name = clientsInfo.substring(nameOpen, nameClose);

                device.setName(name);

                int connTimeOpen = clientsInfo.indexOf("<cur_conn_time>", searchAfter) + "<cur_conn_time>".length();
                int connTimeClose = clientsInfo.indexOf("</cur_conn_time>", searchAfter);

                long connTime = Long.parseLong(clientsInfo.substring(connTimeOpen, connTimeClose));

                device.setConnectionTime(connTime);

                int statusOpen = clientsInfo.indexOf("<status>", searchAfter) + "<status>".length();
                int statusClose = clientsInfo.indexOf("</status>", searchAfter);

                String status = clientsInfo.substring(statusOpen, statusClose);

                device.setStatus(status);

                int lastConnTimeOpen = clientsInfo.indexOf("<last_conn_time>", searchAfter) + "<last_conn_time>".length();
                int lastConnTimeClose = clientsInfo.indexOf("</last_conn_time>", searchAfter);

                String lastConnTime = clientsInfo.substring(lastConnTimeOpen, lastConnTimeClose);

                device.setLastConnectionTime(lastConnTime);

                int totalConnTimeOpen = clientsInfo.indexOf("<total_conn_time>", searchAfter) + "<total_conn_time>".length();
                int totalConnTimeClose = clientsInfo.indexOf("</total_conn_time>", searchAfter);

                long totalConnTime = Long.parseLong(clientsInfo.substring(totalConnTimeOpen, totalConnTimeClose));

                device.setTotalConnectionTime(totalConnTime);

                info.addDevice(device);

                searchAfter = totalConnTimeClose + "</total_conn_time>".length();

                if (searchAfter < 0 || searchAfter >= clientsInfo.length()) break;
            } catch (Exception e){
                break;
            }
        }

        return info;
    }
}
