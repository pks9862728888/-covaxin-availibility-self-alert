package com.self.covaxinavailibilityselfalert.services;

import com.self.covaxinavailibilityselfalert.events.VaccineAvailableSendMailEvent;
import com.self.covaxinavailibilityselfalert.models.LastVaccineAvailability;
import com.self.covaxinavailibilityselfalert.models.VaccinationCenterData;
import com.self.covaxinavailibilityselfalert.models.VaccineData;
import com.self.covaxinavailibilityselfalert.repositories.LastVaccineAvailabilityRepository;
import com.self.covaxinavailibilityselfalert.repositories.VaccineDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SendMailService {

    private VaccineDataRepository vaccineDataRepository;

    private LastVaccineAvailabilityRepository lastVaccineAvailabilityRepository;

    private ApplicationEventPublisher applicationEventPublisher;

    private Logger LOGGER  = LoggerFactory.getLogger(getClass());

    @Autowired
    public SendMailService(VaccineDataRepository vaccineDataRepository, LastVaccineAvailabilityRepository lastVaccineAvailabilityRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.vaccineDataRepository = vaccineDataRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.lastVaccineAvailabilityRepository = lastVaccineAvailabilityRepository;
    }

    public void sendMailIfNotSentBefore(List<VaccinationCenterData> vaccinationCenterDataList, String districtCode) {
        boolean sendMail = false;
        List<VaccinationCenterData> newSessionAvailability = new ArrayList<>();
        List<VaccinationCenterData> oldSessionAvailability = new ArrayList<>();
        List<String> centerNames = new ArrayList<>();

        // Preparing data on whether mail should be sent
        for (VaccinationCenterData vaccinationCenterData : vaccinationCenterDataList) {
            centerNames.add(vaccinationCenterData.getName());

            // Get the last availability of vaccine for this station
            LastVaccineAvailability availability = lastVaccineAvailabilityRepository.findFirstByNameAndDistrictCode(vaccinationCenterData.getName(), districtCode);

            // Set send mail flag to true if any new station is found or existing station vaccine is found for which mail was not sent
            if (availability == null) {
                newSessionAvailability.add(vaccinationCenterData);
                lastVaccineAvailabilityRepository.saveAndFlush(new LastVaccineAvailability(vaccinationCenterData.getName(), districtCode, true, new Date()));
                sendMail = true;
            } else {
                if (availability.isAvailable()) {
                    oldSessionAvailability.add(vaccinationCenterData);
                    availability.setLastAvailableOn(new Date());
                } else {
                    newSessionAvailability.add(vaccinationCenterData);
                    availability.setAvailable(true);
                    availability.setLastAvailableOn(new Date());
                    sendMail = true;
                }
                lastVaccineAvailabilityRepository.saveAndFlush(availability);
            }
        }

        // Update data for which slots are not available
        updateDataAndSetVaccineAvailabilityToFalse(centerNames, districtCode);

        // Send mail if new slot is available
        if (sendMail) {
            this.applicationEventPublisher.publishEvent(new VaccineAvailableSendMailEvent(this, newSessionAvailability, oldSessionAvailability));

            // Save data in database
            vaccineDataRepository.saveAndFlush(new VaccineData(newSessionAvailability.toString() + "|" + oldSessionAvailability.toString()));
        } else {
            LOGGER.info("Skipping sending mail because mail already sent.");
        }
    }

    private void updateDataAndSetVaccineAvailabilityToFalse(List<String> centerNames, String districtCode) {
        // Get all availability info for particular district
        List<LastVaccineAvailability> availabilities = lastVaccineAvailabilityRepository.findAllByDistrictCode(districtCode);

        // Update the vaccine availability not previously updated
        for (LastVaccineAvailability availability: availabilities) {
            if (!centerNames.contains(availability.getName())) {
                availability.setAvailable(false);
                lastVaccineAvailabilityRepository.saveAndFlush(availability);
            }
        }
    }
}
