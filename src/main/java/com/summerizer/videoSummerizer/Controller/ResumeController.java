package com.summerizer.videoSummerizer.Controller;

import com.summerizer.videoSummerizer.Entity.User;
import com.summerizer.videoSummerizer.Helper.ResumeRequest;
import com.summerizer.videoSummerizer.Repository.UserRepo;
import com.summerizer.videoSummerizer.Service.ResumeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/resume")
@Slf4j
public class ResumeController {

    private ResumeService resumeService;

    @Autowired
    private UserRepo userRepo;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }


    @PostMapping("/generate")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> getResumeData(
            @RequestBody ResumeRequest resumeRequest,
            @AuthenticationPrincipal OAuth2User principal
    ) throws IOException {
        // Extract user email from OAuth2 principal
        String userEmail = principal.getAttribute("email");
        log.info("User email: " + userEmail);

        // Find user by email
        Optional<User> userOptional = userRepo.findByEmail(userEmail);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }

        User user = userOptional.get();

        // Call resume service with user information
        Map<String, Object> resumeResponse = resumeService.generateResumeResponse(
                resumeRequest.userDescription()
        );

        return ResponseEntity.ok(resumeResponse);
    }



}