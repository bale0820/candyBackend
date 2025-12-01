package com.tjg_project.candy.domain.openai.controller;

import com.tjg_project.candy.domain.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class OpenAiController {

    private final OpenAiService openAiService;

    @GetMapping("/test")
    public String test() {
        return openAiService.ask("Hello! Reply shortly.");
    }
}