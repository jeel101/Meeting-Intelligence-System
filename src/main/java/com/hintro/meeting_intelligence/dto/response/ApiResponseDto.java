package com.hintro.meeting_intelligence.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hintro.meeting_intelligence.logging.TraceFilter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.MDC;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDto<T> {
    private String traceId;
    private boolean success;
    private T data;

    public static <T> ApiResponseDto<T> success(T data, String traceId) {
        return ApiResponseDto.<T>builder()
                .traceId(traceId)
                .success(true)
                .data(data)
                .build();
    }

}
