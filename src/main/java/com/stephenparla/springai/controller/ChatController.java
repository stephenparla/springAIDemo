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
            response = chatService.chatCompletion(userMessage);
        } catch (Exception e){
            log.error("Error:{}", e.getMessage());
        }
        return response;
    }
}
