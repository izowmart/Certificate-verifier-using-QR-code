package com.marttech.certverifier;

public class Filed_Reports {

    public String capturedimage,descr,email,name,place,radiogroup;

    public Filed_Reports() {
    }

    public Filed_Reports(String capturedimage, String descr, String email, String name, String place, String radiogroup) {
        this.capturedimage = capturedimage;
        this.descr = descr;
        this.email = email;
        this.name = name;
        this.place = place;
        this.radiogroup = radiogroup;
    }

    public String getCapturedimage() {
        return capturedimage;
    }

    public void setCapturedimage(String capturedimage) {
        this.capturedimage = capturedimage;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getRadiogroup() {
        return radiogroup;
    }

    public void setRadiogroup(String radiogroup) {
        this.radiogroup = radiogroup;
    }
}
