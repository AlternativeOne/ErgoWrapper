package com.lexoff.ergowrapper.extractor;

import com.lexoff.ergowrapper.Client;
import com.lexoff.ergowrapper.info.Info;
import com.lexoff.ergowrapper.info.ResponseInfo;

public class PowerOffExtractor extends Extractor {
    private String PATH="xml_action.cgi?method=set";
    private String DATA="<?xml version=\"1.0\" encoding=\"US-ASCII\"?> <RGW><param><method>call</method><session>000</session><obj_path>router</obj_path><obj_method>router_poweroff</obj_method></param></RGW>";

    public PowerOffExtractor(Client client){
        super(client, "");

        setUrl(BASE_URL+PATH);
        setData(DATA);
    }

    @Override
    protected Info buildInfo() {
        ResponseInfo info=new ResponseInfo();

        if (xmlStrResponse.contains("<setting_response>OK</setting_response>"))
            info.setSuccess(1);
        else
            info.setSuccess(0);

        int settingResponseOpen = xmlStrResponse.indexOf("<setting_response>") + "<setting_response>".length();
        int settingResponseClose = xmlStrResponse.indexOf("</setting_response>");

        String message = xmlStrResponse.substring(settingResponseOpen, settingResponseClose);

        info.setMessage(message);

        return info;
    }
}
