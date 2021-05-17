package com.self.covaxinavailibilityselfalert.enumerations;

public enum AgeTrackingType {
    TRACK_18("TRACK_18"),
    TRACK_45("TRACK_45"),
    TRACK_BOTH("TRACK_BOTH");

    private String name;

    AgeTrackingType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
