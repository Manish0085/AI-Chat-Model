package com.summerizer.videoSummerizer.Repository;

import com.summerizer.videoSummerizer.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<Object> findByUserId(String userId);
}
