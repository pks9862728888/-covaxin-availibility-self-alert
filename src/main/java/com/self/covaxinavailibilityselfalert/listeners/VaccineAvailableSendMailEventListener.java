package com.self.covaxinavailibilityselfalert.listeners;

import com.self.covaxinavailibilityselfalert.events.VaccineAvailableSendMailEvent;
import com.self.covaxinavailibilityselfalert.models.MailSubscribers;
import com.self.covaxinavailibilityselfalert.models.VaccinationCenterData;
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
        String message = getFormattedMessageBody(vaccineAvailableSendMailEvent.getNewSessionAvailabilityDataList(), vaccineAvailableSendMailEvent.getOldSessionAvailabilityDataList());

        // Creating mail
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSentDate(new Date());
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setFrom(fromEmail);
        simpleMailMessage.setReplyTo(fromEmail);
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

    private String getFormattedMessageBody(List<VaccinationCenterData> newSessionAvailabilityDataList, List<VaccinationCenterData> oldSessionAvailabilityDataList) {
        String header = "Hi there,";
        String newVaccineAvailabilityBody = "Vaccines are available in following new locations:";
        String oldVaccineAvailabilityBody = "Vaccines are also available in the following previously mailed locations:";
        String footer = "Thanking you,\n\nYours faithfully,\nVaccine Tracker Team\n";
        StringBuilder stringBuilder = new StringBuilder()
                .append(header)
                .append("\n\n")
                .append(newVaccineAvailabilityBody)
                .append("\n\n")
                .append(formattedMessage(newSessionAvailabilityDataList));

        if (oldSessionAvailabilityDataList.size() > 0) {
            stringBuilder
                    .append("\n\n")
                    .append(oldVaccineAvailabilityBody)
                    .append("\n\n")
                    .append(formattedMessage(oldSessionAvailabilityDataList));
        }

        return  stringBuilder
                .append(footer)
                .toString();
    }

    private String formattedMessage(List<VaccinationCenterData> vaccinationCenterDataList) {
        StringBuilder formattedLocationData = new StringBuilder();

        for (VaccinationCenterData vaccinationCenterData : vaccinationCenterDataList) {
            formattedLocationData.append(vaccinationCenterData).append("\n\n");
        }

        return formattedLocationData.toString();
    }

}
