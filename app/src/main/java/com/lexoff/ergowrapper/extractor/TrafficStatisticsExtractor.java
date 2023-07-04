package com.lexoff.ergowrapper.extractor;

import com.lexoff.ergowrapper.Client;
import com.lexoff.ergowrapper.info.Info;
import com.lexoff.ergowrapper.info.TrafficStatisticsInfo;

public class TrafficStatisticsExtractor extends Extractor {
    private String PATH="xml_action.cgi?method=set";
    private String DATA="<?xml version=\"1.0\" encoding=\"US-ASCII\"?> <RGW><param><method>call</method><session>000</session><obj_path>statistics</obj_path><obj_method>stat_get_common_data</obj_method></param></RGW>";

    public TrafficStatisticsExtractor(Client client){
        super(client, "");

        setUrl(BASE_URL+PATH);
        setData(DATA);
    }

    @Override
    protected Info buildInfo() {
        TrafficStatisticsInfo info=new TrafficStatisticsInfo();

        int receivedBytesOpen = xmlStrResponse.indexOf("<rx_bytes>") + "<rx_bytes>".length();
        int receivedBytesClose = xmlStrResponse.indexOf("</rx_bytes>");

        long receivedBytes = Long.parseLong(xmlStrResponse.substring(receivedBytesOpen, receivedBytesClose));

        info.setReceivedBytes(receivedBytes);

        int sentBytesOpen = xmlStrResponse.indexOf("<tx_bytes>") + "<tx_bytes>".length();
        int sentBytesClose = xmlStrResponse.indexOf("</tx_bytes>");

        long sentBytes = Long.parseLong(xmlStrResponse.substring(sentBytesOpen, sentBytesClose));

        info.setSentBytes(sentBytes);

        int completeBytesOpen = xmlStrResponse.indexOf("<rx_tx_bytes>") + "<rx_tx_bytes>".length();
        int completeBytesClose = xmlStrResponse.indexOf("</rx_tx_bytes>");

        long completeBytes = Long.parseLong(xmlStrResponse.substring(completeBytesOpen, completeBytesClose));

        info.setCompleteBytes(completeBytes);

        int errorBytesOpen = xmlStrResponse.indexOf("<error_bytes>") + "<error_bytes>".length();
        int errorBytesClose = xmlStrResponse.indexOf("</error_bytes>");

        long errorBytes = Long.parseLong(xmlStrResponse.substring(errorBytesOpen, errorBytesClose));

        info.setErrorBytes(errorBytes);

        int allReceivedBytesOpen = xmlStrResponse.indexOf("<total_rx_bytes>") + "<total_rx_bytes>".length();
        int allReceivedBytesClose = xmlStrResponse.indexOf("</total_rx_bytes>");

        long allReceivedBytes = Long.parseLong(xmlStrResponse.substring(allReceivedBytesOpen, allReceivedBytesClose));

        info.setAllReceivedBytes(allReceivedBytes);

        int allSentBytesOpen = xmlStrResponse.indexOf("<total_tx_bytes>") + "<total_tx_bytes>".length();
        int allSentBytesClose = xmlStrResponse.indexOf("</total_tx_bytes>");

        long allSentBytes = Long.parseLong(xmlStrResponse.substring(allSentBytesOpen, allSentBytesClose));

        info.setAllSentBytes(allSentBytes);

        int allCompleteBytesOpen = xmlStrResponse.indexOf("<total_rx_tx_bytes>") + "<total_rx_tx_bytes>".length();
        int allCompleteBytesClose = xmlStrResponse.indexOf("</total_rx_tx_bytes>");

        long allCompleteBytes = Long.parseLong(xmlStrResponse.substring(allCompleteBytesOpen, allCompleteBytesClose));

        info.setAllCompleteBytes(allCompleteBytes);

        int allErrorBytesOpen = xmlStrResponse.indexOf("<total_error_bytes>") + "<total_error_bytes>".length();
        int allErrorBytesClose = xmlStrResponse.indexOf("</total_error_bytes>");

        long allErrorBytes = Long.parseLong(xmlStrResponse.substring(allErrorBytesOpen, allErrorBytesClose));

        info.setAllErrorBytes(allErrorBytes);

        return info;
    }
}
