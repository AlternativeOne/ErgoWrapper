package com.lexoff.ergowrapper.info;

import com.lexoff.ergowrapper.Utils;

public class Contact extends Info {

    private int realIndex;

    private String name;
    private String mobile;
    private String home;
    private String office;
    private String email;

    private int location;

    public Contact(){
        //empty
    }

    public String getEncodedName() {
        return name;
    }

    public String getDecodedName(){
        return Utils.decodeSMSBody(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public boolean hasHome(){
        return home!=null && !home.isEmpty();
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public boolean hasOffice(){
        return office!=null && !office.isEmpty();
    }

    public String getEncodedEmail() {
        return email;
    }

    public String getDecodedEmail(){
        return Utils.decodeSMSBody(email);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean hasEmail(){
        return email!=null && !email.isEmpty();
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getIndex() {
        return realIndex;
    }

    public void setIndex(int index) {
        this.realIndex = index;
    }
}
