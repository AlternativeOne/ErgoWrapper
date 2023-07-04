package com.lexoff.ergowrapper.extractor;

import com.lexoff.ergowrapper.Client;
import com.lexoff.ergowrapper.XMLUtils;
import com.lexoff.ergowrapper.info.CellNetworkInfo;
import com.lexoff.ergowrapper.info.Info;

public class CellNetworkInfoExtractor extends Extractor {
    private String PATH = "xml_action.cgi?method=set";
    private String DATA = "<?xml version=\"1.0\" encoding=\"US-ASCII\"?> <RGW><param><method>call</method><session>000</session><obj_path>cm</obj_path><obj_method>get_link_context</obj_method></param></RGW>";

    public CellNetworkInfoExtractor(Client client) {
        super(client, "");

        setUrl(BASE_URL + PATH);
        setData(DATA);
    }

    @Override
    protected Info buildInfo() {
        CellNetworkInfo info = new CellNetworkInfo();

        boolean isShortVersion=!xmlStrResponse.contains("<connection_status>");

        if (xmlStrResponse.contains("<roaming>")) {
            info.setRoaming(true);

            String networkName = XMLUtils.getString(xmlStrResponse, "roaming_network_name");

            info.setNetworkName(networkName);
        } else {
            info.setRoaming(false);

            int networkNameOpen = xmlStrResponse.indexOf("<network_name>") + "<network_name>".length();
            int networkNameClose = xmlStrResponse.indexOf("</network_name>");

            String networkName = xmlStrResponse.substring(networkNameOpen, networkNameClose);

            info.setNetworkName(networkName);
        }

        int imeiOpen = xmlStrResponse.indexOf("<IMEI>") + "<IMEI>".length();
        int imeiClose = xmlStrResponse.indexOf("</IMEI>");

        String imei = xmlStrResponse.substring(imeiOpen, imeiClose);

        info.setIMEI(imei.isEmpty() ? "N/A" : imei);

        if (!isShortVersion) {
            int connStatusOpen = xmlStrResponse.indexOf("<connection_status>") + "<connection_status>".length();
            int connStatusClose = xmlStrResponse.indexOf("</connection_status>");

            int connStatus = Integer.parseInt(xmlStrResponse.substring(connStatusOpen, connStatusClose));

            info.setConnectionStatus(connStatus);
        } else {
            info.setConnectionStatus(0);
        }

        int rssiOpen = xmlStrResponse.indexOf("<rssi>") + "<rssi>".length();
        int rssiClose = xmlStrResponse.indexOf("</rssi>");

        int rssi = Integer.parseInt(xmlStrResponse.substring(rssiOpen, rssiClose));

        info.setRssi(rssi);

        int sysModeOpen = xmlStrResponse.indexOf("<sys_mode>") + "<sys_mode>".length();
        int sysModeClose = xmlStrResponse.indexOf("</sys_mode>");

        int sysMode = Integer.parseInt(xmlStrResponse.substring(sysModeOpen, sysModeClose));

        info.setSysMode(sysMode);

        int dataModeOpen = xmlStrResponse.indexOf("<data_mode>") + "<data_mode>".length();
        int dataModeClose = xmlStrResponse.indexOf("</data_mode>");

        int dataMode = Integer.parseInt(xmlStrResponse.substring(dataModeOpen, dataModeClose));

        info.setDataMode(dataMode);

        if (!isShortVersion) {
            int pdpType = Integer.parseInt(XMLUtils.getString(xmlStrResponse, "pdp_type"));

            info.setPdpType(pdpType);

            int ipType = Integer.parseInt(XMLUtils.getString(xmlStrResponse, "ip_type"));

            info.setIpType(ipType);

            if (2 == sysMode) {
                if (!xmlStrResponse.contains("<lte_apn>") || "" == XMLUtils.getString(xmlStrResponse, "lte_apn")) {
                    String apnName = XMLUtils.getString(xmlStrResponse, "apn");

                    info.setApnName(apnName);
                } else {
                    String apnName = XMLUtils.getString(xmlStrResponse, "lte_apn");

                    info.setApnName(apnName);
                }

            } else {
                String apnName = XMLUtils.getString(xmlStrResponse, "apn");

                info.setApnName(apnName);
            }

            if (ipType == 0 || ipType == 1) {
                String ipv4Addr = XMLUtils.getString(xmlStrResponse, "ipv4_ip");

                info.setIpv4Addr(ipv4Addr);

                String ipv4Dns1 = XMLUtils.getString(xmlStrResponse, "ipv4_dns1");

                info.setIpv4Dns1(ipv4Dns1);

                String ipv4Dns2 = XMLUtils.getString(xmlStrResponse, "ipv4_dns2");

                info.setIpv4Dns2(ipv4Dns2);

                String ipv4GateWay = XMLUtils.getString(xmlStrResponse, "ipv4_gateway");

                info.setIpv4GateWay(ipv4GateWay);

                String ipv4NetMask = XMLUtils.getString(xmlStrResponse, "ipv4_submask");

                info.setIpv4NetMask(ipv4NetMask);
            }

            if (ipType == 0 || ipType == 2) {
                String ipv6Addr = XMLUtils.getString(xmlStrResponse, "ipv6_ip");

                info.setIpv6Addr(ipv6Addr);

                String ipv6Dns1 = XMLUtils.getString(xmlStrResponse, "ipv6_dns1");

                info.setIpv6Dns1(ipv6Dns1);

                String ipv6Dns2 = XMLUtils.getString(xmlStrResponse, "ipv6_dns2");

                info.setIpv6Dns2(ipv6Dns2);

                String ipv6GateWay = XMLUtils.getString(xmlStrResponse, "ipv6_gateway");

                info.setIpv6GateWay(ipv6GateWay);

                String ipv6NetMask = XMLUtils.getString(xmlStrResponse, "ipv6_submask");

                info.setIpv6NetMask(ipv6NetMask);
            }
        }

        return info;
    }

}
