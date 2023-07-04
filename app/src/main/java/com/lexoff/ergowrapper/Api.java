package com.lexoff.ergowrapper;

import com.lexoff.ergowrapper.extractor.AllConnectedDevicesExtractor;
import com.lexoff.ergowrapper.extractor.BlockDeviceExtractor;
import com.lexoff.ergowrapper.extractor.CellNetworkInfoExtractor;
import com.lexoff.ergowrapper.extractor.ConnectedDevicesExtractor;
import com.lexoff.ergowrapper.extractor.ContactsInfoExtractor;
import com.lexoff.ergowrapper.extractor.DeleteContactExtractor;
import com.lexoff.ergowrapper.extractor.DeleteSMSExtractor;
import com.lexoff.ergowrapper.extractor.DisableCellNWExtractor;
import com.lexoff.ergowrapper.extractor.GetUSSDResponseExtractor;
import com.lexoff.ergowrapper.extractor.LANIPExtractor;
import com.lexoff.ergowrapper.extractor.LogInfoExtractor;
import com.lexoff.ergowrapper.extractor.PowerOffExtractor;
import com.lexoff.ergowrapper.extractor.RebootExtractor;
import com.lexoff.ergowrapper.extractor.RuntimeExtractor;
import com.lexoff.ergowrapper.extractor.SMSExtractor;
import com.lexoff.ergowrapper.extractor.SMSInfoExtractor;
import com.lexoff.ergowrapper.extractor.SaveSMSDraftExtractor;
import com.lexoff.ergowrapper.extractor.SendSMSExtractor;
import com.lexoff.ergowrapper.extractor.SendUSSDExtractor;
import com.lexoff.ergowrapper.extractor.TrafficStatisticsExtractor;
import com.lexoff.ergowrapper.extractor.UnblockDeviceExtractor;
import com.lexoff.ergowrapper.extractor.UnreadSMSExtractor;
import com.lexoff.ergowrapper.extractor.WANConfigExtractor;
import com.lexoff.ergowrapper.extractor.WiFiInfoExtractor;
import com.lexoff.ergowrapper.info.CellNetworkInfo;
import com.lexoff.ergowrapper.info.ConnectedDevicesInfo;
import com.lexoff.ergowrapper.info.ContactsInfo;
import com.lexoff.ergowrapper.info.ExtentedConnDevicesInfo;
import com.lexoff.ergowrapper.info.LANIPInfo;
import com.lexoff.ergowrapper.info.LogInfo;
import com.lexoff.ergowrapper.info.ResponseInfo;
import com.lexoff.ergowrapper.info.RuntimeInfo;
import com.lexoff.ergowrapper.info.SMS;
import com.lexoff.ergowrapper.info.SMSInfo;
import com.lexoff.ergowrapper.info.TrafficStatisticsInfo;
import com.lexoff.ergowrapper.info.USSDResponseInfo;
import com.lexoff.ergowrapper.info.UnreadSMSInfo;
import com.lexoff.ergowrapper.info.WANConfigInfo;
import com.lexoff.ergowrapper.info.WiFi;

import java.io.IOException;

public class Api {
    public static int SMS_TYPE_INBOX=0;
    public static int SMS_TYPE_SIM_INBOX=3;
    public static int SMS_TYPE_SENT=1;
    public static int SMS_TYPE_DRAFTS=2;

    private Client client;

    public Api(){
        client=App.getApp().getClient();
    }

    public SMSInfo getSMSInfo(int type, int page) throws IOException, NotAuthorizedException {
        SMSInfoExtractor siExtractor=new SMSInfoExtractor(client, type, page);
        SMSInfo smsInfo=(SMSInfo) siExtractor.getInfo();
        return smsInfo;
    }

    public SMS getSMS(int id) throws IOException, NotAuthorizedException {
        SMSExtractor smsExtractor=new SMSExtractor(client, id);
        SMS sms=(SMS) smsExtractor.getInfo();
        return sms;
    }

    public ResponseInfo deleteSMS(int id) throws IOException, NotAuthorizedException {
        DeleteSMSExtractor dsExtractor=new DeleteSMSExtractor(client, id);
        ResponseInfo info=(ResponseInfo) dsExtractor.getInfo();
        return info;
    }

    public ResponseInfo saveSMSDraft(int id, int isGSM7, String address, String body, String date, int type, int protocol) throws IOException, NotAuthorizedException {
        SaveSMSDraftExtractor ssdExtractor=new SaveSMSDraftExtractor(client, id, isGSM7, address, body, date, type, protocol);
        ResponseInfo info=(ResponseInfo) ssdExtractor.getInfo();
        return info;
    }

    public ResponseInfo sendSMS(int id, int isGSM7, String address, String body, String date, int protocol) throws IOException, NotAuthorizedException {
        SendSMSExtractor ssExtractor=new SendSMSExtractor(client, id, isGSM7, address, body, date, protocol);
        ResponseInfo info=(ResponseInfo) ssExtractor.getInfo();
        return info;
    }

    public ContactsInfo getContactsInfo(int page) throws IOException, NotAuthorizedException {
        ContactsInfoExtractor ciExtractor=new ContactsInfoExtractor(client, page);
        ContactsInfo contactsInfo=(ContactsInfo) ciExtractor.getInfo();
        return contactsInfo;
    }

    public ResponseInfo deleteContact(int location, int index) throws IOException, NotAuthorizedException {
        DeleteContactExtractor dcExtractor=new DeleteContactExtractor(client, location, index);
        ResponseInfo info=(ResponseInfo) dcExtractor.getInfo();
        return info;
    }

    public ResponseInfo doReboot() throws IOException, NotAuthorizedException {
        RebootExtractor rExtractor=new RebootExtractor(client);
        ResponseInfo info=(ResponseInfo) rExtractor.getInfo();
        return info;
    }

    public ResponseInfo doPowerOff() throws IOException, NotAuthorizedException {
        PowerOffExtractor poExtractor=new PowerOffExtractor(client);
        ResponseInfo info=(ResponseInfo) poExtractor.getInfo();
        return info;
    }

    public TrafficStatisticsInfo getTrafficStatistics() throws IOException, NotAuthorizedException {
        TrafficStatisticsExtractor tsExtractor=new TrafficStatisticsExtractor(client);
        TrafficStatisticsInfo info=(TrafficStatisticsInfo) tsExtractor.getInfo();
        return info;
    }

    public ResponseInfo sendUSSD(String ussd) throws IOException, NotAuthorizedException {
        SendUSSDExtractor suExtractor=new SendUSSDExtractor(client, ussd);
        ResponseInfo info=(ResponseInfo) suExtractor.getInfo();
        return info;
    }

    public USSDResponseInfo getUSSDResponse() throws IOException, NotAuthorizedException {
        GetUSSDResponseExtractor urExtractor=new GetUSSDResponseExtractor(client);
        USSDResponseInfo info=(USSDResponseInfo) urExtractor.getInfo();
        return info;
    }

    public UnreadSMSInfo getUnreadSMSInfo() throws IOException, NotAuthorizedException {
        UnreadSMSExtractor usExtractor=new UnreadSMSExtractor(client);
        UnreadSMSInfo info=(UnreadSMSInfo) usExtractor.getInfo();
        return info;
    }

    public RuntimeInfo getRuntimeInfo() throws IOException, NotAuthorizedException {
        RuntimeExtractor rExtractor=new RuntimeExtractor(client);
        RuntimeInfo info=(RuntimeInfo) rExtractor.getInfo();
        return info;
    }

    public CellNetworkInfo getCellNetworkInfo() throws IOException, NotAuthorizedException {
        CellNetworkInfoExtractor cniExtractor=new CellNetworkInfoExtractor(client);
        CellNetworkInfo info=(CellNetworkInfo) cniExtractor.getInfo();
        return info;
    }

    public WiFi getWiFiInfo() throws IOException, NotAuthorizedException {
        WiFiInfoExtractor wiExtractor=new WiFiInfoExtractor(client);
        WiFi info=(WiFi) wiExtractor.getInfo();
        return info;
    }

    public LANIPInfo getLANIPInfo() throws IOException, NotAuthorizedException {
        LANIPExtractor liExtractor=new LANIPExtractor(client);
        LANIPInfo info=(LANIPInfo) liExtractor.getInfo();
        return info;
    }

    public ResponseInfo getSwitchCellNWStateInfo(String proto, String dial_switch) throws IOException, NotAuthorizedException {
        DisableCellNWExtractor scnwsExtractor=new DisableCellNWExtractor(client, proto, dial_switch);
        ResponseInfo info=(ResponseInfo) scnwsExtractor.getInfo();
        return info;
    }

    public WANConfigInfo getWANConfigInfo() throws IOException, NotAuthorizedException {
        WANConfigExtractor wcExtractor=new WANConfigExtractor(client);
        WANConfigInfo info=(WANConfigInfo) wcExtractor.getInfo();
        return info;
    }

    public ConnectedDevicesInfo getConnectedDevicesInfo() throws IOException, NotAuthorizedException {
        ConnectedDevicesExtractor cdExtractor=new ConnectedDevicesExtractor(client);
        ConnectedDevicesInfo info=(ConnectedDevicesInfo) cdExtractor.getInfo();
        return info;
    }

    public ExtentedConnDevicesInfo getAllConnectedDevicesInfo() throws IOException, NotAuthorizedException {
        AllConnectedDevicesExtractor acdExtractor=new AllConnectedDevicesExtractor(client);
        ExtentedConnDevicesInfo info=(ExtentedConnDevicesInfo) acdExtractor.getInfo();
        return info;
    }

    public ResponseInfo blockDevice(String mac) throws IOException, NotAuthorizedException {
        BlockDeviceExtractor bdExtractor=new BlockDeviceExtractor(client, mac);
        ResponseInfo info=(ResponseInfo) bdExtractor.getInfo();
        return info;
    }

    public ResponseInfo unblockDevice(String mac) throws IOException, NotAuthorizedException {
        UnblockDeviceExtractor ubdExtractor=new UnblockDeviceExtractor(client, mac);
        ResponseInfo info=(ResponseInfo) ubdExtractor.getInfo();
        return info;
    }

    public LogInfo getLogInfo() throws IOException, NotAuthorizedException {
        LogInfoExtractor liExtractor=new LogInfoExtractor(client);
        LogInfo info=(LogInfo) liExtractor.getInfo();
        return info;
    }

}
