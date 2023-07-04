package com.lexoff.ergowrapper.info;

import com.lexoff.ergowrapper.Utils;

public class USSDResponseInfo extends Info {
    private String response;
    private boolean needToRepeat;

    public USSDResponseInfo(){
        //empty
    }

    public String getResponse() {
        return response;
    }

    public String getDecodedResponse() {
        return Utils.decodeUSSDResponse(response);
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean isNeedToRepeat() {
        return needToRepeat;
    }

    public void setNeedToRepeat(boolean needToRepeat) {
        this.needToRepeat = needToRepeat;
    }
}
