package com.hintro.meeting_intelligence.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequestDto {
    @NotBlank
    private String name;

    @Email
    private String email;

    @NotBlank
    private String password;
}
