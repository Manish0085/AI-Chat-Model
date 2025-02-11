package com.summerizer.videoSummerizer.Config;

import com.summerizer.videoSummerizer.Entity.User;
import com.summerizer.videoSummerizer.Repository.UserRepo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;


@Component
@Slf4j
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepo userRepo;



    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        log.info("onAuthenticationSuccessHandler");

        //fetch user from oauth2
        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();

        String email = user.getAttribute("email");
        String name = user.getAttribute("name");



        //create user and save to DB

        User user1 = new User();
        user1.setEmail(email);
        user1.setUsername(name);
        user1.setPassword(UUID.randomUUID().toString());

        User user2 = userRepo.findByEmail(email).orElse(null);
        if (user2 == null){

            userRepo.save(user1);
            log.info("User with email "+email+" has been saved");

        }
        response.sendRedirect("http://localhost:5173");
    }
}
