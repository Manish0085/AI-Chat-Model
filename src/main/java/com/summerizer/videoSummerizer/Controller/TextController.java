package com.summerizer.videoSummerizer.Controller;

import com.summerizer.videoSummerizer.Service.TextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/summarize")
public class TextController {

    private final TextService textService;

    @Autowired
    public TextController(TextService textService) {
        this.textService = textService;
    }

    @PostMapping
    public Mono<String> summarize(@RequestBody String text) {
        return textService.summarizeText(text);
    }
}
