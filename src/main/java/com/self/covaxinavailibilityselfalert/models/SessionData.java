package com.self.covaxinavailibilityselfalert.models;

public class SessionData {

    private String name;
    private Integer pincode;
    private String date;
    private Integer min_age_limit;
    private String vaccine;

    public SessionData(String name, Integer pincode, String date, Integer min_age_limit, String vaccine) {
        this.name = name;
        this.pincode = pincode;
        this.date = date;
        this.min_age_limit = min_age_limit;
        this.vaccine = vaccine;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPincode() {
        return pincode;
    }

    public void setPincode(Integer pincode) {
        this.pincode = pincode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getMin_age_limit() {
        return min_age_limit;
    }

    public void setMin_age_limit(Integer min_age_limit) {
        this.min_age_limit = min_age_limit;
    }

    public String getVaccine() {
        return vaccine;
    }

    public void setVaccine(String vaccine) {
        this.vaccine = vaccine;
    }

    @Override
    public String toString() {
        return "SessionData{" +
                "name='" + name + '\'' +
                ", pincode='" + pincode + '\'' +
                ", date='" + date + '\'' +
                ", min_age_limit=" + min_age_limit +
                ", vaccine='" + vaccine + '\'' +
                '}';
    }
}


