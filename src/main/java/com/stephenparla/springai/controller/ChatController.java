package com.stephenparla.springai.controller;

import com.stephenparla.springai.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/aichat")
public class ChatController {

    @Autowired
    ChatService chatService;

    @GetMapping("/{message}")
    public String getFirstMessage(@PathVariable String userMessage) {
        return chatService.getChatCompletion(userMessage);
    }
}
