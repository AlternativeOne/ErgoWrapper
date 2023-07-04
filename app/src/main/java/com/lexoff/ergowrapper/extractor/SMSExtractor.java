package com.lexoff.ergowrapper.extractor;

import com.lexoff.ergowrapper.Client;
import com.lexoff.ergowrapper.info.Info;
import com.lexoff.ergowrapper.info.SMS;

public class SMSExtractor extends Extractor {
    private String PATH="xml_action.cgi?method=set";
    private String DATA="<?xml version=\"1.0\" encoding=\"US-ASCII\"?> <RGW><param><method>call</method><session>000</session><obj_path>sms</obj_path><obj_method>sms.get_by_id</obj_method></param><sms_info><sms><id>%d</id></sms></sms_info></RGW>";

    public SMSExtractor(Client client, int id){
        super(client, "");

        setUrl(BASE_URL+PATH);
        setData(String.format(DATA, id));
    }

    @Override
    protected Info buildInfo() {
        SMS sms = new SMS();

        int idOpen = xmlStrResponse.indexOf("<id>") + "<id>".length();
        int idClose = xmlStrResponse.indexOf("</id>");

        int id = Integer.parseInt(xmlStrResponse.substring(idOpen, idClose));

        sms.setId(id);

        int addresserOpen = xmlStrResponse.indexOf("<address>") + "<address>".length();
        int addresserClose = xmlStrResponse.indexOf("</address>");

        String addresser = xmlStrResponse.substring(addresserOpen, addresserClose);

        sms.setAddresser(addresser);

        int bodyOpen = xmlStrResponse.indexOf("<body>") + "<body>".length();
        int bodyClose = xmlStrResponse.indexOf("</body>");

        String body = xmlStrResponse.substring(bodyOpen, bodyClose);

        sms.setEncodedBody(body);

        int dateOpen = xmlStrResponse.indexOf("<date>") + "<date>".length();
        int dateClose = xmlStrResponse.indexOf("</date>");

        String date = xmlStrResponse.substring(dateOpen, dateClose);

        sms.setArrivalTime(date);

        return sms;
    }
}
