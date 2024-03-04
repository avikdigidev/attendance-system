package com.attendance.email.system.service;

import com.attendance.email.system.dto.AttendanceEmail;
import lombok.extern.slf4j.Slf4j;
import models.SendEnhancedRequestBody;
import models.SendEnhancedResponseBody;
import models.SendRequestMessage;
import models.SendRequestMessageRouting;
import org.springframework.stereotype.Service;
import services.Courier;
import services.SendService;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;

@Slf4j
@Service
public class AttendanceEmailNotificationService {


    public void sendEmail(AttendanceEmail notification) {
        Courier.init("YOUR_AUTH_TOKEN_HERE");

        SendEnhancedRequestBody request = new SendEnhancedRequestBody();
        SendRequestMessage message = new SendRequestMessage();

        HashMap<String, String> to = new HashMap<String, String>();
        to.put("email", notification.getEmployee().getEmailId());
        message.setTo(to);
        HashMap<String, Object> content = new HashMap<String, Object>();
        content.put("subject", notification.getSubject());
        content.put("body", notification.getBody());
        message.setContent(content);
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("subject data", notification.getSubject());
        data.put("body data", notification.getBody());

        HashMap<String, Object> routing = new HashMap<String, Object>();
        routing.put("method", "single");
        routing.put("channels", "myemail@gmail.com");

        SendRequestMessageRouting srmr = new SendRequestMessageRouting();
        srmr.setChannels(Collections.singletonList("Email"));
        srmr.setMethod("single");
        message.setRouting(srmr);
        request.setMessage(message);

        try {
            SendEnhancedResponseBody response = new SendService().sendEnhancedMessage(request);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
      /*  try {
            Email email = new SimpleEmail();
            email.setHostName("smtp.gmail.com");
            email.setSmtpPort(465);
            email.setAuthenticator(new DefaultAuthenticator("email@prakashok.co.in", "password"));
            email.setStartTLSEnabled(true);

            email.setFrom("myemail@gmail");

            email.addTo(notification.getEmployee().getEmailId());
            email.setSubject(notification.getSubject());
            email.setMsg(notification.getBody());
            log.info("Mail" , email);
            email.send();
            log.info("Mail" , email);
        } catch (EmailException e) {
            e.printStackTrace();
        }

        log.info("Successfully sent mail notification");*/
    }
}
