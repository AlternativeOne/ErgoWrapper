package com.lexoff.ergowrapper.extractor;

import com.lexoff.ergowrapper.Client;
import com.lexoff.ergowrapper.info.Info;
import com.lexoff.ergowrapper.info.ResponseInfo;

public class SaveSMSDraftExtractor extends Extractor {
    private String PATH="xml_action.cgi?method=set";
    private String DATA="<?xml version=\"1.0\" encoding=\"US-ASCII\"?> <RGW><param><method>call</method><session>000</session><obj_path>sms</obj_path><obj_method>sms.save</obj_method></param><sms_info><sms><id>%d</id><gsm7>%d</gsm7><address>%s</address><body>%s</body><date>%s</date><type>%d</type><protocol>%d</protocol></sms></sms_info></RGW>";

    public SaveSMSDraftExtractor(Client client, int id, int isGSM7, String address, String body, String date, int type, int protocol){
        super(client, "");

        setUrl(BASE_URL+PATH);
        setData(String.format(DATA, id, isGSM7, address, body, date, type, protocol));
    }

    @Override
    protected Info buildInfo() {
        ResponseInfo info=new ResponseInfo();

        if (xmlStrResponse.contains("<resp>0</resp>"))
            info.setSuccess(1);
        else
            info.setSuccess(0);

        info.setMessage("");

        return info;
    }
}
