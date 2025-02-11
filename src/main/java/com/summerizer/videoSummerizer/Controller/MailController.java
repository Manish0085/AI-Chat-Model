package com.summerizer.videoSummerizer.Controller;

import com.summerizer.videoSummerizer.Entity.User;
import com.summerizer.videoSummerizer.Repository.UserRepo;
import com.summerizer.videoSummerizer.Service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/mail")
@Slf4j
public class MailController {

    @Autowired
    private MailService mailService;

    @Autowired
    private UserRepo userRepo;

    @PostMapping("/send")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> sendMail(
            @RequestParam String subject,
            @RequestParam String body,
            @AuthenticationPrincipal OAuth2User principal
    ) {
        // Extract user email from OAuth2 principal
        String userEmail = principal.getAttribute("email");
        log.info("Authenticated user email: " + userEmail);

        // Find user by email
        Optional<User> userOptional = userRepo.findByEmail(userEmail);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        try {
            // Use authenticated user's email as the sender
            mailService.sendMail(userEmail, subject, body);
            return ResponseEntity.ok("Mail sent successfully!");
        } catch (Exception e) {
            log.error("Failed to send mail", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send mail: " + e.getMessage());
        }
    }
}
