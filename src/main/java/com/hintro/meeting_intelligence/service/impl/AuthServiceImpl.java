package com.hintro.meeting_intelligence.service.impl;

import com.hintro.meeting_intelligence.dto.request.LoginRequestDto;
import com.hintro.meeting_intelligence.dto.request.RegisterRequestDto;
import com.hintro.meeting_intelligence.entity.User;
import com.hintro.meeting_intelligence.exception.AppException;
import com.hintro.meeting_intelligence.repository.UserRepository;
import com.hintro.meeting_intelligence.service.AuthService;
import com.hintro.meeting_intelligence.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public void register(RegisterRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw AppException.conflict("Email already exists");
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );
        userRepository.save(user);
    }

    @Override
    public String login(LoginRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->AppException.unauthorized("Invalid credentials"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {

            throw AppException.unauthorized(
                    "Invalid credentials"
            );
        }

        return jwtUtil.generateToken(
                user.getEmail()
        );    }
}
