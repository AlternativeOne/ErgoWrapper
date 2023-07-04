package com.lexoff.ergowrapper.extractor;

import com.lexoff.ergowrapper.Client;
import com.lexoff.ergowrapper.info.Info;
import com.lexoff.ergowrapper.info.USSDResponseInfo;

public class GetUSSDResponseExtractor extends Extractor {
    private String PATH="xml_action.cgi?method=set";
    private String DATA="<?xml version=\"1.0\" encoding=\"US-ASCII\"?> <RGW><param><method>call</method><session>000</session><obj_path>ussd</obj_path><obj_method>get_ussd_ind</obj_method></param></RGW>";

    public GetUSSDResponseExtractor(Client client){
        super(client, "");

        setUrl(BASE_URL+PATH);
        setData(DATA);
    }

    @Override
    protected Info buildInfo() {
        USSDResponseInfo info=new USSDResponseInfo();

        if (!xmlStrResponse.contains("<ussd_str>")){
            info.setResponse("");

            info.setNeedToRepeat(true);
        } else {
            int ussdResponseOpen = xmlStrResponse.indexOf("<ussd_str>") + "<ussd_str>".length();
            int ussdResponseClose = xmlStrResponse.indexOf("</ussd_str>");

            String response = xmlStrResponse.substring(ussdResponseOpen, ussdResponseClose);

            info.setResponse(response);

            info.setNeedToRepeat(false);
        }

        return info;
    }
}
