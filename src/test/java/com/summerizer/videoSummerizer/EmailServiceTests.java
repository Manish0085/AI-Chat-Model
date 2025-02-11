package com.summerizer.videoSummerizer;


import com.summerizer.videoSummerizer.Service.MailService;
import jakarta.persistence.Table;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTests {

    @Autowired
    private MailService mailService;


    @Test
    void testSendMail(){
        mailService.sendMail("manish2213198@akgec.ac.com", "Testing mail", "Hii Akash");
    }
}
