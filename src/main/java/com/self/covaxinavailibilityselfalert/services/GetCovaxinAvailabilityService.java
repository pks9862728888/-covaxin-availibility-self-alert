package com.self.covaxinavailibilityselfalert.services;

import com.self.covaxinavailibilityselfalert.enumerations.AgeTrackingType;
import com.self.covaxinavailibilityselfalert.enumerations.DoseTrackingType;
import com.self.covaxinavailibilityselfalert.enumerations.VaccineTrackingType;
import com.self.covaxinavailibilityselfalert.exceptions.FetchAppointmentsException;
import com.self.covaxinavailibilityselfalert.models.SessionData;
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

    @Autowired
    private RestService restService;

    public List<SessionData> getVaccineAvailability(int districtCode, Date date) throws FetchAppointmentsException, JSONException {
        String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(date);
        String url = GET_VACCINE_AVAILABILITY_BY_DISTRICT_URL + String.format("?district_id=%s&date=%s", districtCode, formattedDate);
        List data = this.restService.getSessionsAsString(url);
        List<SessionData> sessionDataList = new ArrayList<>();

        if (data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                Map dataObj = (Map) data.get(i);

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
                sessionDataList.add(new SessionData(
                        (String) dataObj.get("name"),
                        (Integer) dataObj.get("pincode"),
                        (String) dataObj.get("date"),
                        (Integer) dataObj.get("min_age_limit"),
                        (String) dataObj.get("vaccine")
                ));
            }
        }
        return sessionDataList;
    }
}
