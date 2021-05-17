package com.self.covaxinavailibilityselfalert.repositories;

import com.self.covaxinavailibilityselfalert.models.VaccineData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccineDataRepository extends JpaRepository<VaccineData, Long> {

    int countAllBySessionDataContaining(String sessionData);

}
