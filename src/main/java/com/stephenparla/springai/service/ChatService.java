package com.stephenparla.springai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class ChatService {

    @Autowired
    private ChatClient chatClient;

    public String getChatCompletion(String userMessage) {
        return chatClient.prompt(userMessage).call().content();
    }
}