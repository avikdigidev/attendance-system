package com.attendance.email.system.service;

import com.attendance.email.system.dto.AttendanceEmail;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AttendanceEmailNotificationService {


    public void sendEmail(AttendanceEmail notification) {
        try {
            Email email = new SimpleEmail();
            email.setHostName("smtp.gmail.com");
            email.setSmtpPort(465);
            email.setAuthenticator(new DefaultAuthenticator("email@prakashok.co.in", "password"));
            email.setStartTLSEnabled(true);

            email.setFrom("avikdigidev@gmail");

            email.addTo(notification.getEmployee().getEmailId());
            email.setSubject(notification.getSubject());
            email.setMsg(notification.getBody());
            log.info("Mail" , email);
            email.send();
            log.info("Mail" , email);
        } catch (EmailException e) {
            e.printStackTrace();
        }

        log.info("Successfully sent mail notification");
    }
}
