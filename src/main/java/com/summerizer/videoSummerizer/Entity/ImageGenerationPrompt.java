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
@Table(name = "image_generation_prompt")
public class ImageGenerationPrompt {

    @Id
    private String promptId;

    private String promptText;

    private LocalDateTime localDateTime;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    public void generateId() {
        if (this.promptId == null) {
            this.promptId = UUID.randomUUID().toString();
        }
    }
}
