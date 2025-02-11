package com.summerizer.videoSummerizer.Repository;

import com.summerizer.videoSummerizer.Entity.ImageGenerationPrompt;
import com.summerizer.videoSummerizer.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageGenerationPromptRepository extends JpaRepository<ImageGenerationPrompt, String> {
    Optional<ImageGenerationPrompt> findByUserAndPromptText(User user, String prompt);

    List<ImageGenerationPrompt> findByUser(User user);
}
