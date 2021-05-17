package com.self.covaxinavailibilityselfalert.enumerations;

public enum DoseTrackingType {
    TRACK_FIRST_DOSE("TRACK_FIRST_DOSE"),
    TRACK_SECOND_DOSE("TRACK_SECOND_DOSE"),
    TRACK_BOTH("TRACK_BOTH");

    private String name;

    DoseTrackingType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
