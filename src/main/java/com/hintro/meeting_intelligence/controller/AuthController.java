package com.hintro.meeting_intelligence.controller;

import com.hintro.meeting_intelligence.dto.request.LoginRequestDto;
import com.hintro.meeting_intelligence.dto.request.RegisterRequestDto;
import com.hintro.meeting_intelligence.dto.response.ApiResponseDto;
import com.hintro.meeting_intelligence.dto.response.AuthResponseDto;
import com.hintro.meeting_intelligence.logging.TraceFilter;
import com.hintro.meeting_intelligence.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<AuthResponseDto>> login(
            @Valid @RequestBody LoginRequestDto request) {
        String token = authService.login(request);

        return ResponseEntity.ok(
                ApiResponseDto.success(
                        new AuthResponseDto(token),
                        MDC.get(TraceFilter.TRACE_ID_MDC_KEY)
                )
        );
    }
    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto<String>> register(
            @Valid @RequestBody RegisterRequestDto request) {

        authService.register(request);

        return ResponseEntity.ok(
                ApiResponseDto.success(
                        "User registered successfully",
                        MDC.get(TraceFilter.TRACE_ID_MDC_KEY)
                )
        );
    }
}
