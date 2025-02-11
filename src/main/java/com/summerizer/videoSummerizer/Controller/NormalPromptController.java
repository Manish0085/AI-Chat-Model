package com.summerizer.videoSummerizer.Controller;

import com.summerizer.videoSummerizer.Entity.NormalPrompt;
import com.summerizer.videoSummerizer.Entity.User;
import com.summerizer.videoSummerizer.Repository.NormalPromptRepository;
import com.summerizer.videoSummerizer.Repository.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/text-prompts")
public class NormalPromptController {

    private final NormalPromptRepository normalPromptRepository;
    private final UserRepo userRepo;

    public NormalPromptController(NormalPromptRepository normalPromptRepository, UserRepo userRepo) {
        this.normalPromptRepository = normalPromptRepository;
        this.userRepo = userRepo;
    }

    // Store Prompt
    @PostMapping("/store")
    public ResponseEntity<?> storePrompt(Authentication authentication, @RequestBody String promptText) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: User not logged in");
        }

        String username = authentication.getName(); // Default fallback

        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
            username = oauthUser.getAttribute("name"); // Try getting the name field from OAuth2 response
            if (username == null) {
                username = oauthUser.getAttribute("email"); // Fallback to email if name is missing
            }
        }


        Optional<User> userOptional = userRepo.findByEmail(username);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = userOptional.get();

        NormalPrompt prompt = NormalPrompt.builder()
                .promptText(promptText)
                .user(user)
                .build();

        normalPromptRepository.save(prompt);

        return ResponseEntity.ok("Normal prompt stored successfully!");
    }


    // Fetch All Prompts for Logged-in User
    @GetMapping("/all")
    public ResponseEntity<?> getAllPrompts(@AuthenticationPrincipal OidcUser oidcUser) {
        if (oidcUser == null) {
            return ResponseEntity.status(401).body("Unauthorized: User not logged in");
        }

        String email = oidcUser.getEmail();
        Optional<User> userOptional = userRepo.findByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        User user = userOptional.get();
        Optional<NormalPrompt> prompts = normalPromptRepository.findById(user.getUserId());

        return ResponseEntity.ok(prompts);
    }
}
