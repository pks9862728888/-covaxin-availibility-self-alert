package com.self.covaxinavailibilityselfalert.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity(name = "vaccine_data")
public class VaccineData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    private String sessionData;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Kolkata")
    private Date timeStamp;

    public VaccineData() {
    }

    public VaccineData(String sessionData) {
        this.sessionData = sessionData;
        this.timeStamp = new Date();
    }

    public VaccineData(long id, String sessionData) {
        this.id = id;
        this.sessionData = sessionData;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSessionData() {
        return sessionData;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setSessionData(String sessionData) {
        this.sessionData = sessionData;
    }
}
