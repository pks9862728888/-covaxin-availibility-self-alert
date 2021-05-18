package com.self.covaxinavailibilityselfalert.models;

public class VaccinationCenterData {

    private String name;
    private Integer pinCode;
    private String date;
    private Integer minAgeLimit;
    private String firstDoseAvailability;
    private String secondDoseAvailability;
    private String vaccine;
    private String slots;

    public VaccinationCenterData(String name, Integer pinCode, String date, Integer minAgeLimit, String vaccine, String firstDoseAvailability, String secondDoseAvailability, String slots) {
        this.name = name;
        this.pinCode = pinCode;
        this.date = date;
        this.minAgeLimit = minAgeLimit;
        this.vaccine = vaccine;
        this.firstDoseAvailability = firstDoseAvailability;
        this.secondDoseAvailability = secondDoseAvailability;
        this.slots = slots;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPinCode() {
        return pinCode;
    }

    public void setPinCode(Integer pinCode) {
        this.pinCode = pinCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getMinAgeLimit() {
        return minAgeLimit;
    }

    public void setMinAgeLimit(Integer minAgeLimit) {
        this.minAgeLimit = minAgeLimit;
    }

    public String getVaccine() {
        return vaccine;
    }

    public void setVaccine(String vaccine) {
        this.vaccine = vaccine;
    }

    public String getFirstDoseAvailability() {
        return firstDoseAvailability;
    }

    public void setFirstDoseAvailability(String firstDoseAvailability) {
        this.firstDoseAvailability = firstDoseAvailability;
    }

    public String getSecondDoseAvailability() {
        return secondDoseAvailability;
    }

    public void setSecondDoseAvailability(String secondDoseAvailability) {
        this.secondDoseAvailability = secondDoseAvailability;
    }

    public String getSlots() {
        return slots;
    }

    public void setSlots(String slots) {
        this.slots = slots;
    }

    @Override
    public String toString() {
        return "VaccinationCenterData {" +
                "\n\tname = '" + name + '\'' +
                ",\n\tdate = '" + date + '\'' +
                ",\n\tfirst_dose_availability = '" + firstDoseAvailability + '\'' +
                ",\n\tsecond_dose_availability = '" + secondDoseAvailability + '\'' +
                ",\n\tmin_age_limit = " + minAgeLimit +
                ",\n\tvaccine = '" + vaccine + '\'' +
                ",\n\tslots = '" + slots + '\'' +
                "\n}";
    }
}


