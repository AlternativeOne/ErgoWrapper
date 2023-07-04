package com.lexoff.ergowrapper.extractor;

import com.lexoff.ergowrapper.Client;
import com.lexoff.ergowrapper.info.Info;
import com.lexoff.ergowrapper.info.ResponseInfo;

public class SendUSSDExtractor extends Extractor {
    private String PATH="xml_action.cgi?method=set";
    private String DATA="<?xml version=\"1.0\" encoding=\"US-ASCII\"?> <RGW><param><method>call</method><session>000</session><obj_path>ussd</obj_path><obj_method>send_ussd</obj_method></param><ussd><action>%d</action><param>%s</param></ussd></RGW>";

    public SendUSSDExtractor(Client client, String ussd){
        super(client, "");

        setUrl(BASE_URL+PATH);
        setData(String.format(DATA, ussd.startsWith("*") ? 1 : 3, ussd));
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
