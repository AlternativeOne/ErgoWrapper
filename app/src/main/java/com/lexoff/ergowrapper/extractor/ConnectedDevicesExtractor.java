package com.lexoff.ergowrapper.extractor;

import com.lexoff.ergowrapper.Client;
import com.lexoff.ergowrapper.XMLUtils;
import com.lexoff.ergowrapper.info.ConnectedDevice;
import com.lexoff.ergowrapper.info.ConnectedDevicesInfo;
import com.lexoff.ergowrapper.info.Info;

public class ConnectedDevicesExtractor extends Extractor {
    private String PATH="xml_action.cgi?method=set";
    private String DATA="<?xml version=\"1.0\" encoding=\"US-ASCII\"?> <RGW><param><method>call</method><session>000</session><obj_path>statistics</obj_path><obj_method>get_conn_clients_info</obj_method></param></RGW>";

    public ConnectedDevicesExtractor(Client client){
        super(client, "");

        setUrl(BASE_URL+PATH);
        setData(DATA);
    }

    @Override
    protected Info buildInfo() {
        ConnectedDevicesInfo info=new ConnectedDevicesInfo();

        String clientsInfo=XMLUtils.getString(xmlStrResponse, "clients_info");

        int searchAfter=0;

        while (true){
            try {
                ConnectedDevice device = new ConnectedDevice();

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

                info.addDevice(device);

                searchAfter = connTimeClose + "</cur_conn_time>".length();

                if (searchAfter < 0 || searchAfter >= clientsInfo.length()) break;
            } catch (Exception e){
                break;
            }
        }

        return info;
    }
}
