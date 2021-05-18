package com.self.covaxinavailibilityselfalert.services;

import com.self.covaxinavailibilityselfalert.exceptions.FetchAppointmentsException;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;

@Service
public class RestService {

    public ArrayList getSessionsAsString(String url) throws FetchAppointmentsException {
        Response response = given()
                .headers(
                        "Accept", MimeTypeUtils.APPLICATION_JSON_VALUE,
                        "Accept-Language", "hi_IN",
                        "User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                .get(url)
                .then()
                .extract()
                .response();
        if (response.getStatusCode() == HttpStatus.OK.value()) {
            return response.path("sessions");
        } else {
            throw new FetchAppointmentsException(String.valueOf(response.getStatusCode()));
        }
    }
}
