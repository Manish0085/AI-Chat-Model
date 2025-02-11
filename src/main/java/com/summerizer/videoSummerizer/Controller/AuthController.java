package com.summerizer.videoSummerizer.Controller;

import com.summerizer.videoSummerizer.Entity.User;
import com.summerizer.videoSummerizer.Service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/me")
    public ResponseEntity<?> getLoggedInUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("loggedIn", false, "message", "User not logged in"));
        }

        String username = authentication.getName(); // Default fallback

        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
            username = oauthUser.getAttribute("name"); // Try getting the name field from OAuth2 response
            if (username == null) {
                username = oauthUser.getAttribute("email"); // Fallback to email if name is missing
            }
        }

        return ResponseEntity.ok(Map.of("loggedIn", true, "username", username));
    }

    @GetMapping("/is-logged-in")
    public boolean isLoggedIn(HttpServletRequest request) {
        // Check if the user has a session or OAuth token (you can adapt this based on your implementation)
        if (request.getSession().getAttribute("oauth_token") != null) {
            return true;  // User is logged in
        }
        return false;  // User is not logged in
    }

}

