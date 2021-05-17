package com.self.covaxinavailibilityselfalert.services;

import com.self.covaxinavailibilityselfalert.events.VaccineAvailableSendMailEvent;
import com.self.covaxinavailibilityselfalert.models.SessionData;
import com.self.covaxinavailibilityselfalert.models.VaccineData;
import com.self.covaxinavailibilityselfalert.repositories.VaccineDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SendMailService {

    private VaccineDataRepository vaccineDataRepository;

    private ApplicationEventPublisher applicationEventPublisher;

    private Logger LOGGER  = LoggerFactory.getLogger(getClass());

    @Autowired
    public SendMailService(VaccineDataRepository vaccineDataRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.vaccineDataRepository = vaccineDataRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void sendMailIfNotSentBefore(List<SessionData> sessionDataList) {
        if (vaccineDataRepository.countAllBySessionDataContaining(sessionDataList.toString()) == 0) {
            // Send mail
            this.applicationEventPublisher.publishEvent(new VaccineAvailableSendMailEvent(this, sessionDataList));

            // Save data in database
            vaccineDataRepository.saveAndFlush(new VaccineData(sessionDataList.toString()));
        } else {
            LOGGER.info("Skipping sending mail because mail already sent.");
        }
    }
}
