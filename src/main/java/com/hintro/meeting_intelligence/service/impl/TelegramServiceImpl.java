package com.hintro.meeting_intelligence.service.impl;

import com.hintro.meeting_intelligence.service.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TelegramServiceImpl implements TelegramService {
    private final RestTemplate restTemplate;
    @Value("${telegram.bot.token}")
    private String botToken;
    @Value("${telegram.chat.id}")
    private String chatId;

    @Override
    public String sendReminder(String message) {
        String url ="https://api.telegram.org/bot" + botToken + "/sendMessage";
        Map<String, Object> request = new HashMap<>();

        request.put("chat_id", chatId);
        request.put("text", message);

        ResponseEntity<String> response = restTemplate.postForEntity(
                        url,
                        request,
                        String.class
                );

        return response.getBody();
    }
}
