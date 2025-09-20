package com.summerizer.videoSummerizer.Controller;

import com.summerizer.videoSummerizer.Entity.ImageGenerationPrompt;
import com.summerizer.videoSummerizer.Entity.NormalPrompt;
import com.summerizer.videoSummerizer.Entity.User;
import com.summerizer.videoSummerizer.Repository.ImageGenerationPromptRepository;
import com.summerizer.videoSummerizer.Repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.stabilityai.api.StabilityAiImageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/image")
@Slf4j
public class ImageController {

    @Autowired
    @Qualifier("stabilityAiImageModel")
    private ImageModel openaiImageModel;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ImageGenerationPromptRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @GetMapping("/generateImage")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getImage(
            @RequestParam("prompt") String prompt,
            @RequestParam("referenceImage") Optional<MultipartFile> referenceImage,
            @AuthenticationPrincipal OAuth2User principal) throws IOException {

        String userEmail = principal.getAttribute("email");
        logger.info("Request from user: {}", userEmail);
        Optional<User> userOptional = userRepo.findByEmail(userEmail);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }

        User user = userOptional.get();
        Optional<ImageGenerationPrompt> existingPrompt = repository.findByUserAndPromptText(user, prompt);

        if (existingPrompt.isPresent()) {
            ImageGenerationPrompt entity = existingPrompt.get();
            entity.setLocalDateTime(LocalDateTime.now());
            repository.save(entity);
            log.info("Existing prompt updated");
        } else {
            ImageGenerationPrompt entity = new ImageGenerationPrompt();
            entity.setPromptText(prompt);
            entity.setLocalDateTime(LocalDateTime.now());
            entity.setUser(user);
            repository.save(entity);
            log.info("New prompt has been saved");
        }

        StabilityAiImageOptions.Builder optionsBuilder = StabilityAiImageOptions.builder()
                .withStylePreset("cinematic")
                .withN(1)
                .withHeight(1024)
                .withWidth(1024);

        if (referenceImage.isPresent() && !referenceImage.get().isEmpty()) {
            byte[] imageBytes = referenceImage.get().getBytes();
            String base64ReferenceImage = Base64.getEncoder().encodeToString(imageBytes);
            optionsBuilder.withResponseFormat(base64ReferenceImage);
        }

        ImagePrompt imagePrompt = new ImagePrompt(prompt, optionsBuilder.build());
        ImageResponse response = openaiImageModel.call(imagePrompt);
        String base64Image = response.getResult().getOutput().getB64Json();

        return ResponseEntity.ok("{\"image\": \"data:image/png;base64," + base64Image + "\"}");
    }

//    @GetMapping("/prompts")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<List<ImageGenerationPrompt>> getAllPrompts(@AuthenticationPrincipal OAuth2User principal) {
//        String userEmail = principal.getAttribute("email");
//        Optional<User> userOptional = userRepo.findByEmail(userEmail);
//
//        if (userOptional.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(Collections.emptyList());
//        }
//
//        User user = userOptional.get();
//        List<ImageGenerationPrompt> prompts = repository.findByUser(user);
//
//        return ResponseEntity.ok(prompts);
//    }


}
