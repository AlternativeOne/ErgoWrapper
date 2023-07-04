package com.lexoff.ergowrapper.info;

public class TrafficStatisticsInfo extends Info {
    private long receivedBytes;
    private long sentBytes;
    private long completeBytes;
    private long errorBytes;

    private long allReceivedBytes;
    private long allSentBytes;
    private long allCompleteBytes;
    private long allErrorBytes;

    public TrafficStatisticsInfo(){
        //empty
    }

    public long getReceivedBytes() {
        return receivedBytes;
    }

    public void setReceivedBytes(long receivedBytes) {
        this.receivedBytes = receivedBytes;
    }

    public long getSentBytes() {
        return sentBytes;
    }

    public void setSentBytes(long sentBytes) {
        this.sentBytes = sentBytes;
    }

    public long getCompleteBytes() {
        return completeBytes;
    }

    public void setCompleteBytes(long completeBytes) {
        this.completeBytes = completeBytes;
    }

    public long getErrorBytes() {
        return errorBytes;
    }

    public void setErrorBytes(long errorBytes) {
        this.errorBytes = errorBytes;
    }

    public long getAllReceivedBytes() {
        return allReceivedBytes;
    }

    public void setAllReceivedBytes(long allReceivedBytes) {
        this.allReceivedBytes = allReceivedBytes;
    }

    public long getAllSentBytes() {
        return allSentBytes;
    }

    public void setAllSentBytes(long allSentBytes) {
        this.allSentBytes = allSentBytes;
    }

    public long getAllCompleteBytes() {
        return allCompleteBytes;
    }

    public void setAllCompleteBytes(long allCompleteBytes) {
        this.allCompleteBytes = allCompleteBytes;
    }

    public long getAllErrorBytes() {
        return allErrorBytes;
    }

    public void setAllErrorBytes(long allErrorBytes) {
        this.allErrorBytes = allErrorBytes;
    }

}
