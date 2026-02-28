package com.stephenparla.springai.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.springai.chat.CompletionRequest;
import com.springai.chat.CompletionResponse;
import com.springai.chat.ChatAI;

@Service
public class ChatService {

    @Autowired

    private ChatAI chatAI;

    public String getChatCompletion(String userMessage) {

        CompletionRequest request = new CompletionRequest(userMessage);

        CompletionResponse response = chatAI.complete(request);

        return response.getCompletion();
    }
}