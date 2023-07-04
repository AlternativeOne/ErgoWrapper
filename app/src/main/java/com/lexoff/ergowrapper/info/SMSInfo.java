package com.lexoff.ergowrapper.info;

import java.util.ArrayList;

public class SMSInfo extends Info {
    private ArrayList<SMS> smsList;

    private int smsMax;

    public SMSInfo(){
        smsList=new ArrayList<>();
    }


    public ArrayList<SMS> getSMS() {
        return smsList;
    }

    public void addSMS(SMS sms) {
        smsList.add(sms);
    }

    public int getSMSMax() {
        return smsMax;
    }

    public void setSmsMax(int smsMax) {
        this.smsMax = smsMax;
    }
}
