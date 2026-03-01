package com.stephenparla.springai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;


@Service
@Slf4j
public class ChatService {

    private final ChatClient chatClient;

    @Autowired
    public ChatService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String getChatCompletion(String userMessage) {
        return chatClient.prompt(userMessage).call().content();
    }
}