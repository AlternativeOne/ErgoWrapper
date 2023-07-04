package com.lexoff.ergowrapper.info;

public class RuntimeInfo extends Info {
    private int days;
    private int hours;
    private int minutes;
    private int seconds;

    public RuntimeInfo(){
        //empty
    }


    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public String getPrettiedTime(){
        return days+":"+hours+":"+minutes+":"+seconds;
    }

}
