package com.lexoff.ergowrapper.extractor;

import com.lexoff.ergowrapper.Client;
import com.lexoff.ergowrapper.XMLUtils;
import com.lexoff.ergowrapper.info.Info;
import com.lexoff.ergowrapper.info.WANConfigInfo;

public class WANConfigExtractor extends Extractor {
    private String PATH="xml_action.cgi?method=set";
    private String DATA="<?xml version=\"1.0\" encoding=\"US-ASCII\"?> <RGW><param><method>call</method><session>000</session><obj_path>cm</obj_path><obj_method>get_wan_configs</obj_method></param></RGW>";

    public WANConfigExtractor(Client client){
        super(client, "");

        setUrl(BASE_URL+PATH);
        setData(DATA);
    }

    @Override
    protected Info buildInfo() {
        WANConfigInfo info = new WANConfigInfo();

        String proto = XMLUtils.getString(xmlStrResponse, "proto");

        info.setProto(proto);

        String dialSwitch = XMLUtils.getString(xmlStrResponse, "dial_switch");

        info.setDialSwitch(dialSwitch);

        return info;
    }

}
