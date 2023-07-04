package com.lexoff.ergowrapper.extractor;

import com.lexoff.ergowrapper.Client;
import com.lexoff.ergowrapper.info.Info;
import com.lexoff.ergowrapper.info.UnreadSMSInfo;

public class UnreadSMSExtractor extends Extractor {
    private String PATH="xml_action.cgi?method=set";
    private String DATA="<?xml version=\"1.0\" encoding=\"US-ASCII\"?> <RGW><param><method>call</method><session>000</session><obj_path>sms</obj_path><obj_method>sms.query</obj_method></param><sms_info><sms><type>0</type><read>0</read><location>2</location></sms></sms_info></RGW>";

    public UnreadSMSExtractor(Client client){
        super(client, "");

        setUrl(BASE_URL+PATH);
        setData(DATA);
    }

    @Override
    protected Info buildInfo() {
        UnreadSMSInfo info=new UnreadSMSInfo();

        int unreadCountOpen = xmlStrResponse.indexOf("<count>") + "<count>".length();
        int unreadCountClose = xmlStrResponse.indexOf("</count>");

        int unreadCount = Integer.parseInt(xmlStrResponse.substring(unreadCountOpen, unreadCountClose));

        info.setUnreadCount(unreadCount);

        return info;
    }
}
