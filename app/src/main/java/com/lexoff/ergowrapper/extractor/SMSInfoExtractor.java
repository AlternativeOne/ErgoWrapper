package com.lexoff.ergowrapper.extractor;

import com.lexoff.ergowrapper.Client;
import com.lexoff.ergowrapper.info.Info;
import com.lexoff.ergowrapper.info.SMS;
import com.lexoff.ergowrapper.info.SMSInfo;

public class SMSInfoExtractor extends Extractor {
    private String PATH="xml_action.cgi?method=set";
    private String DATA="<?xml version=\"1.0\" encoding=\"US-ASCII\"?> <RGW><param><method>call</method><session>000</session><obj_path>sms</obj_path><obj_method>sms.list_by_type</obj_method></param><sms_info><sms><page_index>%d</page_index><list_type>%d</list_type></sms></sms_info></RGW>";

    public SMSInfoExtractor(Client client, int type, int page){
        super(client, "");

        setUrl(BASE_URL+PATH);
        setData(String.format(DATA, page, type));
    }

    @Override
    protected Info buildInfo() {
        SMSInfo smsInfo=new SMSInfo();

        int totalOpen = xmlStrResponse.indexOf("<total>") + "<total>".length();
        int totalClose = xmlStrResponse.indexOf("</total>");

        int total = Integer.parseInt(xmlStrResponse.substring(totalOpen, totalClose));
        smsInfo.setSmsMax(total);

        if (total>0) {
            int nodeListOpen = xmlStrResponse.indexOf("<node_list>") + "<node_list>".length();
            int nodeListClose = xmlStrResponse.indexOf("</node_list>");

            String nodeList = xmlStrResponse.substring(nodeListOpen, nodeListClose);

            int searchAfter = 0;

            while (true) {
                try {
                    SMS sms = new SMS();

                    int idOpen = nodeList.indexOf("<id>", searchAfter) + "<id>".length();
                    int idClose = nodeList.indexOf("</id>", searchAfter);

                    int id = Integer.parseInt(nodeList.substring(idOpen, idClose));

                    sms.setId(id);

                    int addresserOpen = nodeList.indexOf("<address>", searchAfter) + "<address>".length();
                    int addresserClose = nodeList.indexOf("</address>", searchAfter);

                    String addresser = nodeList.substring(addresserOpen, addresserClose);

                    sms.setAddresser(addresser);

                    int bodyOpen = nodeList.indexOf("<body>", searchAfter) + "<body>".length();
                    int bodyClose = nodeList.indexOf("</body>", searchAfter);

                    String body = nodeList.substring(bodyOpen, bodyClose);

                    sms.setEncodedBody(body);

                    int dateOpen = nodeList.indexOf("<date>", searchAfter) + "<date>".length();
                    int dateClose = nodeList.indexOf("</date>", searchAfter);

                    String date = nodeList.substring(dateOpen, dateClose);

                    sms.setArrivalTime(date);

                    int readOpen = nodeList.indexOf("<read>", searchAfter) + "<read>".length();
                    int readClose = nodeList.indexOf("</read>", searchAfter);

                    boolean read = Integer.parseInt(nodeList.substring(readOpen, readClose)) == 1;

                    sms.setRead(read);

                    int segmentNumOpen = nodeList.indexOf("<segmentNum>", searchAfter) + "<segmentNum>".length();
                    int segmentNumClose = nodeList.indexOf("</segmentNum>", searchAfter);

                    int segmentNum = Integer.parseInt(nodeList.substring(segmentNumOpen, segmentNumClose));

                    sms.setSegmentNum(segmentNum);

                    int totalSegmentsNumOpen = nodeList.indexOf("<totalSegment>", searchAfter) + "<totalSegment>".length();
                    int totalSegmentsNumClose = nodeList.indexOf("</totalSegment>", searchAfter);

                    int totalSegmentsNum = Integer.parseInt(nodeList.substring(totalSegmentsNumOpen, totalSegmentsNumClose));

                    sms.setTotalSegmentsNum(totalSegmentsNum);

                    smsInfo.addSMS(sms);

                    searchAfter = bodyClose + "</body>".length();

                    if (searchAfter < 0 || searchAfter >= nodeList.length()) break;
                } catch (Exception e) {
                    break;
                }
            }
        }

        return smsInfo;
    }
}
