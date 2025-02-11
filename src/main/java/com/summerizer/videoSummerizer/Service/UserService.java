package com.summerizer.videoSummerizer.Service;


import com.summerizer.videoSummerizer.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;
}
