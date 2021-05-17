package com.self.covaxinavailibilityselfalert.events;

import com.self.covaxinavailibilityselfalert.models.SessionData;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class VaccineAvailableSendMailEvent extends ApplicationEvent {

    List<SessionData> sessionDataList;

    public VaccineAvailableSendMailEvent(Object source, List<SessionData> sessionDataList) {
        super(source);
        this.sessionDataList = sessionDataList;
    }

    public List<SessionData> getSessionDataList() {
        return this.sessionDataList;
    }

}
