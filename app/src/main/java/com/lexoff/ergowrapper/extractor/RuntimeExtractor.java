package com.lexoff.ergowrapper.extractor;

import com.lexoff.ergowrapper.Client;
import com.lexoff.ergowrapper.info.Info;
import com.lexoff.ergowrapper.info.RuntimeInfo;

public class RuntimeExtractor extends Extractor {
    private String PATH="xml_action.cgi?method=set";
    private String DATA="<?xml version=\"1.0\" encoding=\"US-ASCII\"?> <RGW><param><method>call</method><session>000</session><obj_path>router</obj_path><obj_method>get_router_runtime</obj_method></param></RGW>";

    public RuntimeExtractor(Client client){
        super(client, "");

        setUrl(BASE_URL+PATH);
        setData(DATA);
    }

    @Override
    protected Info buildInfo() {
        RuntimeInfo info=new RuntimeInfo();

        int daysOpen = xmlStrResponse.indexOf("<run_days>") + "<run_days>".length();
        int daysClose = xmlStrResponse.indexOf("</run_days>");

        int days = Integer.parseInt(xmlStrResponse.substring(daysOpen, daysClose));

        info.setDays(days);

        int hoursOpen = xmlStrResponse.indexOf("<run_hours>") + "<run_hours>".length();
        int hoursClose = xmlStrResponse.indexOf("</run_hours>");

        int hours = Integer.parseInt(xmlStrResponse.substring(hoursOpen, hoursClose));

        info.setHours(hours);

        int minsOpen = xmlStrResponse.indexOf("<run_min>") + "<run_min>".length();
        int minsClose = xmlStrResponse.indexOf("</run_min>");

        int mins = Integer.parseInt(xmlStrResponse.substring(minsOpen, minsClose));

        info.setMinutes(mins);

        int secsOpen = xmlStrResponse.indexOf("<run_sec>") + "<run_sec>".length();
        int secsClose = xmlStrResponse.indexOf("</run_sec>");

        int secs = Integer.parseInt(xmlStrResponse.substring(secsOpen, secsClose));

        info.setSeconds(secs);

        return info;
    }
}
