package com.summerizer.videoSummerizer.Controller;

import com.summerizer.videoSummerizer.Entity.NormalPrompt;
import com.summerizer.videoSummerizer.Entity.User;
import com.summerizer.videoSummerizer.Repository.NormalPromptRepository;
import com.summerizer.videoSummerizer.Repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.mistralai.MistralAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/chat")
@Slf4j
public class ChatController {

    private final MistralAiChatModel chatModel;
    private final UserRepo userRepo;
    private final NormalPromptRepository promptRepository;

    @Autowired
    public ChatController(MistralAiChatModel chatModel, UserRepo userRepo, NormalPromptRepository promptRepository) {
        this.chatModel = chatModel;
        this.userRepo = userRepo;
        this.promptRepository = promptRepository;
    }



    @GetMapping("/ai/generate")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message,
                                                        @AuthenticationPrincipal OAuth2User principal) {
        String userEmail = principal.getAttribute("email");
        log.info("User email: " + userEmail);

        Optional<User> userOptional = userRepo.findByEmail(userEmail);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }

        User user = userOptional.get();

        Optional<NormalPrompt> existingPrompt = promptRepository.findByUserAndPromptText(user, message);

        if (existingPrompt.isPresent()) {
            NormalPrompt prompt = existingPrompt.get();
            prompt.setTimestamp(LocalDateTime.now());
            promptRepository.save(prompt);
            log.info("Existing prompt updated");
        } else {
            NormalPrompt prompt = new NormalPrompt();
            prompt.setPromptText(message);
            prompt.setTimestamp(LocalDateTime.now());
            prompt.setUser(user);
            promptRepository.save(prompt);
            log.info("New prompt has been saved");
        }

        return ResponseEntity.ok(Map.of("user", userEmail, "generation", chatModel.call(message)));
    }


    @GetMapping("/ai/generateStream")
    @PreAuthorize("isAuthenticated()")
    public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        var prompt = new Prompt(new UserMessage(message));
        return chatModel.stream(prompt);
    }

    @GetMapping("/prompts")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<NormalPrompt>> getAllPrompts(@AuthenticationPrincipal OAuth2User principal) {
        String userEmail = principal.getAttribute("email");
        Optional<User> userOptional = userRepo.findByEmail(userEmail);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.emptyList());
        }

        User user = userOptional.get();
        List<NormalPrompt> prompts = promptRepository.findByUser(user);

        return ResponseEntity.ok(prompts);
    }

}