package com.lexoff.ergowrapper.extractor;

import com.lexoff.ergowrapper.Client;
import com.lexoff.ergowrapper.info.Info;
import com.lexoff.ergowrapper.info.ResponseInfo;

public class DeleteContactExtractor extends Extractor {
    private String PATH="xml_action.cgi?method=set";
    private String DATA="<?xml version=\"1.0\" encoding=\"US-ASCII\"?> <RGW><param><method>call</method><session>000</session><obj_path>phonebook</obj_path><obj_method>delete_pb</obj_method></param><phonebook><delete_pb><location>%d</location><count>1</count><indexarray>%d,</indexarray></delete_pb></phonebook></RGW>";

    public DeleteContactExtractor(Client client, int location, int index){
        super(client, "");

        setUrl(BASE_URL+PATH);
        setData(String.format(DATA, location, index));
    }

    @Override
    protected Info buildInfo() {
        ResponseInfo info=new ResponseInfo();

        if (xmlStrResponse.contains("<result>0</result>"))
            info.setSuccess(1);
        else
            info.setSuccess(0);

        info.setMessage("");

        return info;
    }
}
