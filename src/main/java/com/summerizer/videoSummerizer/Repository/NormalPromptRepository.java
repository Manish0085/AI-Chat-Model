package com.summerizer.videoSummerizer.Repository;

import com.summerizer.videoSummerizer.Entity.NormalPrompt;
import com.summerizer.videoSummerizer.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NormalPromptRepository extends JpaRepository<NormalPrompt, String> {
    List<NormalPrompt> findByUser(User user);

    Optional<NormalPrompt> findByUserAndPromptText(User user, String message);
}
