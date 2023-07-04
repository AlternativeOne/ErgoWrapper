package com.lexoff.ergowrapper.info;

public class ResponseInfo extends Info {
    private int success;
    private String message;

    public ResponseInfo(){
        //empty
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
