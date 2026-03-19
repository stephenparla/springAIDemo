package com.stephenparla.springai.controller;

import com.stephenparla.springai.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/aichat")
public class ChatController {

    @Autowired
    ChatService chatService;

    @PostMapping("/prompt")
    public String prompt(@RequestBody String userMessage) {
        String response = "";
        try {
            log.info("inside chatbot!!");
            response = chatService.chatCompletion(userMessage);
            log.info("chatbot request success");
        } catch (Exception e){
            log.error("Error:{}", e.getMessage());
        }
        return response;
    }

    @GetMapping("/ping")
    public String ping() {
        log.info("ping success");
        return "success";
    }
}

