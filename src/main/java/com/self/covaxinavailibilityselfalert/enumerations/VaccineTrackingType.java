package com.self.covaxinavailibilityselfalert.enumerations;

public enum VaccineTrackingType {
    COVAXIN("COVAXIN"),
    COVISHIELD("COVISHIELD"),
    BOTH("BOTH");

    private String name;

    VaccineTrackingType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
