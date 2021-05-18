package com.self.covaxinavailibilityselfalert.events;

import com.self.covaxinavailibilityselfalert.models.VaccinationCenterData;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class VaccineAvailableSendMailEvent extends ApplicationEvent {

    private List<VaccinationCenterData> newSessionAvailabilityDataList;
    private List<VaccinationCenterData> oldSessionAvailabilityDataList;

    public VaccineAvailableSendMailEvent(Object source, List<VaccinationCenterData> newSessionAvailabilityDataList, List<VaccinationCenterData> oldSessionAvailabilityDataList) {
        super(source);
        this.newSessionAvailabilityDataList = newSessionAvailabilityDataList;
        this.oldSessionAvailabilityDataList = oldSessionAvailabilityDataList;
    }

    public List<VaccinationCenterData> getNewSessionAvailabilityDataList() {
        return newSessionAvailabilityDataList;
    }

    public List<VaccinationCenterData> getOldSessionAvailabilityDataList() {
        return oldSessionAvailabilityDataList;
    }
}
