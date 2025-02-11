package com.summerizer.videoSummerizer.Controller;

import com.summerizer.videoSummerizer.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/save")
public class UserController {

    @Autowired
    private UserService userService;


}
