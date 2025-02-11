package com.summerizer.videoSummerizer.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Getter
@Setter
@Table(name = "resume")
public class ResumePrompt {

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
