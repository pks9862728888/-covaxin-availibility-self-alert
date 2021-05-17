package com.self.covaxinavailibilityselfalert.listeners;

import com.self.covaxinavailibilityselfalert.events.VaccineAvailableSendMailEvent;
import com.self.covaxinavailibilityselfalert.models.MailSubscribers;
import com.self.covaxinavailibilityselfalert.models.SessionData;
import com.self.covaxinavailibilityselfalert.repositories.MailSubscribersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class VaccineAvailableSendMailEventListener implements ApplicationListener<VaccineAvailableSendMailEvent> {

    @Value("${spring.mail.username}")
    private String fromEmail;

    private JavaMailSender javaMailSender;

    private MailSubscribersRepository mailSubscribersRepository;

    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    public VaccineAvailableSendMailEventListener(JavaMailSender javaMailSender, MailSubscribersRepository mailSubscribersRepository) {
        this.javaMailSender = javaMailSender;
        this.mailSubscribersRepository = mailSubscribersRepository;
    }

    @Override
    public void onApplicationEvent(VaccineAvailableSendMailEvent vaccineAvailableSendMailEvent) {

        List<MailSubscribers> mailingList = mailSubscribersRepository.findAll();

        // Preparing message body
        String subject = "Vaccine Available!";
        String message = String.format("Hi there,\n\nVaccines are available in following locations:\n\n%s\nThanking you,\n\nYours faithfully,\nCowin Vaccine Tracker Team",
                formattedMessage(vaccineAvailableSendMailEvent.getSessionDataList()));

        // Creating mail
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSentDate(new Date());
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setFrom(fromEmail);
        simpleMailMessage.setText(message);

        // Adding recipient list
        String[] recipients = new String[mailingList.size()];

        for (int i = 0; i < mailingList.size(); i++) {
            recipients[i] = mailingList.get(i).getEmail();
        }

        simpleMailMessage.setTo(recipients);

        // Sending mail
        javaMailSender.send(simpleMailMessage);
        LOGGER.info("Mail sent!");
    }

    private String formattedMessage(List<SessionData> sessionDataList) {
        StringBuilder formattedLocationData = new StringBuilder();

        for (SessionData sessionData : sessionDataList) {
            formattedLocationData.append(sessionData).append("\n\n");
        }

        return formattedLocationData.toString();
    }

}
