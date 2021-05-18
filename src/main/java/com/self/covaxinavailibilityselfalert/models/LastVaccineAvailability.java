package com.self.covaxinavailibilityselfalert.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "last_vaccine_availability")
public class LastVaccineAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String districtCode;

    @Column(name = "last_available_on")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Kolkata")
    private Date lastAvailableOn;

    @Column(name = "is_available")
    private boolean isAvailable;

    public LastVaccineAvailability() {}

    public LastVaccineAvailability(String name, String districtCode, boolean isAvailable, Date lastAvailableOn) {
        this.name = name;
        this.districtCode = districtCode;
        this.isAvailable = isAvailable;
        this.lastAvailableOn = lastAvailableOn;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public Date getLastAvailableOn() {
        return lastAvailableOn;
    }

    public void setLastAvailableOn(Date lastAvailableOn) {
        this.lastAvailableOn = lastAvailableOn;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
