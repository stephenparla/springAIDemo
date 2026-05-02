package com.stephenparla.springai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
@Slf4j
public class ChatService {

    private final ChatClient chatClient;

    // Tracks words used per session
    private final Map<String, Integer> sessionWordCounts = new ConcurrentHashMap<>();

    public ChatService(ChatClient.Builder chatClientBuilder) {

        this.chatClient = chatClientBuilder
                // Forces the AI to be extremely brief to save tokens
                .defaultSystem("You are a concise chatbot. Be direct. Do not exceed 50 words per response.")
                .build();
    }

    public String chatCompletion(String sessionId, String userMessage) {

        // Enforce the word limit per session
        int currentCount = sessionWordCounts.getOrDefault(sessionId, 0);
        if (currentCount >= 30) {
            return "Your limit has reached";
        }

        // Get AI response
        String response = chatClient.prompt(userMessage).call().content();

        // Calculate and save new word count
        if (response != null) {
            int responseWords = response.split("\\s+").length;
            sessionWordCounts.put(sessionId, currentCount + responseWords);
        } else {
            log.warn("Received null response from AI for session {}", sessionId);
            response = "Sorry, I couldn't generate a response.";
        }

        return response;
    }
}