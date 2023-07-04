package com.lexoff.ergowrapper.info;

public class UnreadSMSInfo extends Info {
    private int unreadCount;

    public UnreadSMSInfo(){
        //empty
    }


    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

}
