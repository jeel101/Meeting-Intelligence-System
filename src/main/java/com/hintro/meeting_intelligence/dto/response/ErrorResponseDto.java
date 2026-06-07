package com.hintro.meeting_intelligence.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hintro.meeting_intelligence.dto.ErrorDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDto<T> {
    private String traceId;
    private boolean success;
    private ErrorDetail error;

    public static <T> ErrorResponseDto<T> error(String code, String message, String traceId) {
        return ErrorResponseDto.<T>builder()
                .traceId(traceId)
                .success(false)
                .error(new ErrorDetail(code, message))
                .build();
    }
}
