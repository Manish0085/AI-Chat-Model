package com.summerizer.videoSummerizer.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "normal_prompt")
public class NormalPrompt {

    @Id
    @Column(name = "prompt_id", updatable = false, nullable = false)
    private String promptId;

    @Column(nullable = false)
    private String promptText;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime localDateTime;

    @PrePersist
    public void generateId() {
        if (this.promptId == null) {
            this.promptId = UUID.randomUUID().toString();
        }
    }

    public String getPromptId() {
        return promptId;
    }

    public void setPromptId(String promptId) {
        this.promptId = promptId;
    }

    public String getPromptText() {
        return promptText;
    }

    public void setPromptText(String promptText) {
        this.promptText = promptText;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getTimestamp() {
        return localDateTime;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.localDateTime = timestamp;
    }
}
