package com.stephenparla.springai.controller;

import com.stephenparla.springai.dto.ChatResponse;
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
    public ChatResponse prompt(@RequestBody String userMessage) {
        ChatResponse response = new ChatResponse();
        try {
            log.info("inside chatbot!!");
            String message = chatService.chatCompletion(userMessage);
            response.setMessage(message);
            response.setStatusCode(200);
            log.info("chatbot request success");
        } catch (Exception e){
            log.error("Error:{}", e.getMessage());
            response.setMessage("");
            response.setStatusCode(400);
        }
        return response;
    }

    @GetMapping("/ping")
    public ChatResponse ping() {
        log.info("ping success");
        return new ChatResponse("success", 200);
    }
}

