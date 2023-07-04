package com.lexoff.ergowrapper.extractor;

import com.lexoff.ergowrapper.Client;
import com.lexoff.ergowrapper.XMLUtils;
import com.lexoff.ergowrapper.info.Info;
import com.lexoff.ergowrapper.info.WiFi;

public class WiFiInfoExtractor extends Extractor {
    private String PATH = "xml_action.cgi?method=set";
    private String DATA = "<?xml version=\"1.0\" encoding=\"US-ASCII\"?> <RGW><param><method>call</method><session>000</session><obj_path>wireless</obj_path><obj_method>wifi_get_detail</obj_method></param></RGW>";

    public WiFiInfoExtractor(Client client) {
        super(client, "");

        setUrl(BASE_URL + PATH);
        setData(DATA);
    }

    @Override
    protected Info buildInfo() {
        WiFi info = new WiFi();

        info.set24Hz(true);
        info.setIs5G(false);

        String wifi24HzXML = XMLUtils.getString(xmlStrResponse, "wifi_if_24G");

        int channel = Integer.parseInt(XMLUtils.getString(wifi24HzXML, "channel"));

        info.setChannel(channel);

        String ssid0 = XMLUtils.getString(wifi24HzXML, "ssid0");

        String ssid = XMLUtils.getString(ssid0, "ssid");

        info.setSSID(ssid);

        String mac = XMLUtils.getString(ssid0, "mac");

        info.setMAC(mac);

        String encryption = XMLUtils.getString(xmlStrResponse, "encryption");

        info.setEncryption(encryption);

        int disabled = Integer.parseInt(XMLUtils.getString(xmlStrResponse, "disabled"));

        info.setEnabled(disabled == 0);

        return info;
    }

}
