package com.self.covaxinavailibilityselfalert.repositories;

import com.self.covaxinavailibilityselfalert.models.LastVaccineAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LastVaccineAvailabilityRepository extends JpaRepository<LastVaccineAvailability, Long> {

    LastVaccineAvailability findFirstByNameAndDistrictCode(String name, String districtCode);
    List<LastVaccineAvailability> findAllByDistrictCode(String districtCode);

}
