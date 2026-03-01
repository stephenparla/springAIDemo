package com.stephenparla.springai.controller;

import com.stephenparla.springai.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/aichat")
public class ChatController {

    @Autowired
    ChatService chatService;

    @GetMapping("/{userMessage}")
    public String getFirstMessage(@PathVariable String userMessage) {
        String response = "";
        try {
            response = chatService.getChatCompletion(userMessage);
        } catch (Exception e){
            log.error("Error:{}", e.getMessage());
        }
        return response;
    }
}
