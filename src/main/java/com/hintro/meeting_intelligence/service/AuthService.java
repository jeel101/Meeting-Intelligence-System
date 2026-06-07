package com.hintro.meeting_intelligence.service;

import com.hintro.meeting_intelligence.dto.request.LoginRequestDto;
import com.hintro.meeting_intelligence.dto.request.RegisterRequestDto;

public interface AuthService {
    void register(RegisterRequestDto request);
    String login(LoginRequestDto request);
}
