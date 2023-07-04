package com.lexoff.ergowrapper.extractor;

import com.lexoff.ergowrapper.Client;
import com.lexoff.ergowrapper.info.Info;
import com.lexoff.ergowrapper.info.ResponseInfo;

public class DisableCellNWExtractor extends Extractor {
    private String PATH="xml_action.cgi?method=set";
    private String DATA="<?xml version=\"1.0\" encoding=\"US-ASCII\"?> <RGW><param><method>call</method><session>000</session><obj_path>cm</obj_path><obj_method>connection_switch</obj_method></param><wan><connect_switch><proto>%s</proto><dial_switch>%s</dial_switch></connect_switch></wan></RGW>";

    public DisableCellNWExtractor(Client client, String proto, String dial_switch){
        super(client, "");

        setUrl(BASE_URL+PATH);
        setData(String.format(DATA, proto, dial_switch));
    }

    @Override
    protected Info buildInfo() {
        ResponseInfo info=new ResponseInfo();

        if (xmlStrResponse.contains("<response_status>OK</response_status>"))
            info.setSuccess(1);
        else
            info.setSuccess(0);

        int settingResponseOpen = xmlStrResponse.indexOf("<response_status>") + "<response_status>".length();
        int settingResponseClose = xmlStrResponse.indexOf("</response_status>");

        String message = xmlStrResponse.substring(settingResponseOpen, settingResponseClose);

        info.setMessage(message);

        return info;
    }
}
