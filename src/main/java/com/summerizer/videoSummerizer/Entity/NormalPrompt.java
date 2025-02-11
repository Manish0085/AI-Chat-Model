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
    private String promptId;

    private String promptText;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime timestamp;

    @PrePersist
    public void generateId() {
        if (this.promptId == null) {
            this.promptId = UUID.randomUUID().toString();
        }
        this.timestamp = LocalDateTime.now();
    }
}
