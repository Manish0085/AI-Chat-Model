package com.summerizer.videoSummerizer.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;


    public void sendMail(String from, String subject, String body){
       try {
           SimpleMailMessage mail = new SimpleMailMessage();
           mail.setTo("manishk57107@gmail.com");
           mail.setFrom(from);
           mail.setSubject(subject);
           mail.setText(body);
           javaMailSender.send(mail);
           log.info("Mail has been sent successfully");
       } catch (Exception e){
           log.error("Exception while sending mail ", e);
       }
    }
}
