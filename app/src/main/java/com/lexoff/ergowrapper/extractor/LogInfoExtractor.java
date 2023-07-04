package com.lexoff.ergowrapper.extractor;

import com.lexoff.ergowrapper.Client;
import com.lexoff.ergowrapper.XMLUtils;
import com.lexoff.ergowrapper.info.Info;
import com.lexoff.ergowrapper.info.LogInfo;

public class LogInfoExtractor extends Extractor {
    private String PATH="xml_action.cgi?method=set";
    private String DATA="<?xml version=\"1.0\" encoding=\"US-ASCII\"?> <RGW><param><method>call</method><session>000</session><obj_path>log</obj_path><obj_method>userlog</obj_method></param></RGW>";

    public LogInfoExtractor(Client client){
        super(client, "");

        setUrl(BASE_URL+PATH);
        setData(DATA);
    }

    @Override
    protected Info buildInfo() {
        LogInfo info=new LogInfo();

        String fullLog= XMLUtils.getString(xmlStrResponse, "log");
        info.addLogs(fullLog.split("\n"));

        return info;
    }
}
