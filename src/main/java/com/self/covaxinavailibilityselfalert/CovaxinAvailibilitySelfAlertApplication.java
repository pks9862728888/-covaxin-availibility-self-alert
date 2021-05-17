package com.self.covaxinavailibilityselfalert;

import com.self.covaxinavailibilityselfalert.controllers.GetVaccineAvailabilityController;
import com.self.covaxinavailibilityselfalert.exceptions.FetchAppointmentsException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class CovaxinAvailibilitySelfAlertApplication {

	public static void main(String[] args) {
	    ConfigurableApplicationContext applicationContext = SpringApplication.run(CovaxinAvailibilitySelfAlertApplication.class, args);
        GetVaccineAvailabilityController controller = (GetVaccineAvailabilityController) applicationContext.getBean(GetVaccineAvailabilityController.class);
        controller.getVaccineSchedule();
	}

}
