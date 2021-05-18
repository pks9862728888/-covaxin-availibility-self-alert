package com.self.covaxinavailibilityselfalert.services;

import com.self.covaxinavailibilityselfalert.enumerations.AgeTrackingType;
import com.self.covaxinavailibilityselfalert.enumerations.DoseTrackingType;
import com.self.covaxinavailibilityselfalert.enumerations.VaccineTrackingType;
import com.self.covaxinavailibilityselfalert.exceptions.FetchAppointmentsException;
import com.self.covaxinavailibilityselfalert.models.VaccinationCenterData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class GetCovaxinAvailabilityService {

    @Value("${covaxin.get-availability-by-district}")
    private String GET_VACCINE_AVAILABILITY_BY_DISTRICT_URL;

    @Value("${age.tracking-type}")
    private String ageTrackingType;

    @Value("${dose.tracking-type}")
    private String doseTrackingType;

    @Value("${vaccine.tracking-type}")
    private String vaccineTrackingType;

    private RestService restService;

    @Autowired
    public GetCovaxinAvailabilityService(RestService restService) {
        this.restService = restService;
    }

    public List<VaccinationCenterData> getVaccineAvailability(String districtCode, Date date) throws FetchAppointmentsException, JSONException {
        String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(date);
        String url = GET_VACCINE_AVAILABILITY_BY_DISTRICT_URL + String.format("?district_id=%s&date=%s", districtCode, formattedDate);
        List data = this.restService.getSessionsAsString(url);
        List<VaccinationCenterData> vaccinationCenterDataList = new ArrayList<>();

        if (data.size() > 0) {
            for (Object dataMap : data) {
                Map dataObj = (Map) dataMap;

                // Check vaccine type
                if (!(vaccineTrackingType.equals(VaccineTrackingType.BOTH.getName()) || ((String) dataObj.get("vaccine")).equals(vaccineTrackingType))) {
                    continue;
                }

                // Check age
                if (!ageTrackingType.equals(AgeTrackingType.TRACK_BOTH.getName())) {
                    Integer vaccineAge = (Integer) dataObj.get("min_age_limit");

                    if (ageTrackingType.equals(AgeTrackingType.TRACK_18.getName())) {
                        if (!vaccineAge.equals(18)) {
                            continue;
                        }
                    } else {
                        if (!vaccineAge.equals(45)) {
                            continue;
                        }
                    }
                }

                // check dose
                if (!doseTrackingType.equals(DoseTrackingType.TRACK_BOTH.getName())) {
                    if (doseTrackingType.equals(DoseTrackingType.TRACK_FIRST_DOSE.getName())) {
                        Integer capacity = (Integer) dataObj.get("available_capacity_dose1");
                        if (capacity.equals(0)) {
                            continue;
                        }
                    } else {
                        Integer capacity = (Integer) dataObj.get("available_capacity_dose2");
                        if (capacity.equals(0)) {
                            continue;
                        }
                    }
                }

                // Prepare data since all conditions satisfied
                // Preparing data for availability of doses
                String firstDoseAvailability = "";
                String secondDoseAvailability = "";
                Integer capacityDose1 = (Integer) dataObj.get("available_capacity_dose1");
                if (capacityDose1.equals(0)) {
                    firstDoseAvailability = "None";
                } else {
                    firstDoseAvailability = String.format("Available (%s)", capacityDose1);
                }
                Integer capacityDose2 = (Integer) dataObj.get("available_capacity_dose2");
                if (capacityDose2.equals(0)) {
                    secondDoseAvailability = "None";
                } else {
                    secondDoseAvailability = String.format("Available (%s)", capacityDose2);
                }

                vaccinationCenterDataList.add(new VaccinationCenterData(
                        (String) dataObj.get("name"),
                        (Integer) dataObj.get("pincode"),
                        (String) dataObj.get("date"),
                        (Integer) dataObj.get("min_age_limit"),
                        (String) dataObj.get("vaccine"),
                        firstDoseAvailability,
                        secondDoseAvailability,
                        getFormattedSlots((List) dataObj.get("slots"))
                ));
            }
        }
        return vaccinationCenterDataList;
    }

    private String getFormattedSlots(List<String> slots) {
        if (slots.size() == 0) {
            return "[]";
        }
        StringBuilder str = new StringBuilder().append("[\n");
        for (String s: slots) {
            str.append("\t\t").append(s).append(",\n");
        }
        return str.append("\t]").toString();
    }
}
