package com.self.covaxinavailibilityselfalert.repositories;

import com.self.covaxinavailibilityselfalert.models.MailSubscribers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailSubscribersRepository extends JpaRepository<MailSubscribers, Long> {
}
