package com.lexoff.ergowrapper.info;

import com.lexoff.ergowrapper.Utils;

public class SMS extends Info {
    private int id;
    private String addresser;
    private String encodedBody;
    private String arrivalTime;
    private boolean read;

    private int segmentNum;
    private int totalSegmentsNum;

    public SMS(){
        //empty
    }

    public String getAddresser() {
        return addresser;
    }

    public void setAddresser(String addresser) {
        this.addresser = addresser;
    }

    public String getEncodedBody() {
        return encodedBody;
    }

    public void setEncodedBody(String encodedBody) {
        this.encodedBody = encodedBody;
    }

    public String getDecodedBody() {
        return Utils.decodeSMSBody(encodedBody);
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getPrettiedArrivalTime() {
        String d=arrivalTime.replaceAll("%2B", "+");
        String p1=d.substring(0, 8);
        p1="20"+p1.replaceAll(",", "/");
        String p2=d.substring(9, d.indexOf(",+"));
        p2=p2.replaceAll(",", ":");

        return p1+"\n"+p2;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public int getSegmentNum() {
        return segmentNum;
    }

    public void setSegmentNum(int segmentNum) {
        this.segmentNum = segmentNum;
    }

    public int getTotalSegmentsNum() {
        return totalSegmentsNum;
    }

    public void setTotalSegmentsNum(int totalSegmentsNum) {
        this.totalSegmentsNum = totalSegmentsNum;
    }

    public String getSegmentsString(){
        return totalSegmentsNum==0 ? "" : "["+segmentNum+"/"+totalSegmentsNum+"]";
    }
}
