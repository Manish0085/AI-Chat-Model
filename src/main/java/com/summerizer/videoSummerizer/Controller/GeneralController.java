package com.summerizer.videoSummerizer.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/general")
public class GeneralController {

    @GetMapping("/chat")
    public String generalChat(){
        return "Hello ai";
    }
}
