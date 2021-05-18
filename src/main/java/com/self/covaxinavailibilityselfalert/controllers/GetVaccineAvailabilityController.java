package com.self.covaxinavailibilityselfalert.controllers;

import com.self.covaxinavailibilityselfalert.exceptions.FetchAppointmentsException;
import com.self.covaxinavailibilityselfalert.models.VaccinationCenterData;
import com.self.covaxinavailibilityselfalert.services.GetCovaxinAvailabilityService;
import com.self.covaxinavailibilityselfalert.services.SendMailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller("GetVaccineAvailabilityController")
public class GetVaccineAvailabilityController {

    @Value("${district.code}")
    private String districtCode;

    @Value("${days-to-track}")
    private int daysToTrack;

    @Value("${scan-interval-in-minutes}")
    private long scanIntervalInMilliseconds;

    private GetCovaxinAvailabilityService availabilityData;

    private SendMailService sendMailService;

    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    @PostConstruct
    private void newData() {
        this.scanIntervalInMilliseconds = scanIntervalInMilliseconds * 60 * 1000;
    }

    @Autowired
    public GetVaccineAvailabilityController(GetCovaxinAvailabilityService availabilityData, SendMailService sendMailService) {
        this.availabilityData = availabilityData;
        this.sendMailService = sendMailService;
    }

    public void getVaccineSchedule() {
        while (true) {
            List<VaccinationCenterData> vaccinationCenterData = new ArrayList<>();

            // Fetching data for n number of days
            for (int dayCount = 1; dayCount <= daysToTrack; dayCount++) {
                try {
                    vaccinationCenterData.addAll(availabilityData.getVaccineAvailability(districtCode, new Date(System.currentTimeMillis() + dayCount * 24 * 60 * 60 * 1000)));
                } catch (FetchAppointmentsException | JSONException e) {
                    e.printStackTrace();
                }
            }

            LOGGER.info("Fetching one sweep data complete: " + ((vaccinationCenterData.size() > 0) ? "Vaccine available! " + vaccinationCenterData : "No vaccine available at the moment :("));

            // If vaccines are available then send mail if not sent before
            if (vaccinationCenterData.size() > 0) {
                sendMailService.sendMailIfNotSentBefore(vaccinationCenterData, districtCode);
            }

            // Sleep for n minutes after one sweep
            LOGGER.info("Sleeping for " + scanIntervalInMilliseconds / (60 * 1000) + " minutes.");
            try {
                Thread.sleep(scanIntervalInMilliseconds);
                LOGGER.info("Woke up! Sweeping data again for next " + daysToTrack + " days");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
