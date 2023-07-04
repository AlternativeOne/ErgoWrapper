package com.lexoff.ergowrapper.extractor;

import com.lexoff.ergowrapper.Client;
import com.lexoff.ergowrapper.info.Info;
import com.lexoff.ergowrapper.info.LANIPInfo;

public class LANIPExtractor extends Extractor {
    private String PATH="xml_action.cgi?method=set";
    private String DATA="<?xml version=\"1.0\" encoding=\"US-ASCII\"?> <RGW><param><method>call</method><session>000</session><obj_path>router</obj_path><obj_method>router_get_lan_ip</obj_method></param></RGW>";

    public LANIPExtractor(Client client){
        super(client, "");

        setUrl(BASE_URL+PATH);
        setData(DATA);
    }

    @Override
    protected Info buildInfo() {
        LANIPInfo info=new LANIPInfo();

        int lanIPOpen = xmlStrResponse.indexOf("<lan_ip>") + "<lan_ip>".length();
        int lanIPClose = xmlStrResponse.indexOf("</lan_ip>");

        String lanIP = xmlStrResponse.substring(lanIPOpen, lanIPClose);

        info.setLanIP(lanIP);

        int maskOpen = xmlStrResponse.indexOf("<lan_netmask>") + "<lan_netmask>".length();
        int maskClose = xmlStrResponse.indexOf("</lan_netmask>");

        String mask = xmlStrResponse.substring(maskOpen, maskClose);

        info.setNetworkMask(mask);

        return info;
    }
}
