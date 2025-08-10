package com.fitness.userservice.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ApiErrorResponse {
    private String message;
    private Integer code;
    private Map<String, String> subErrors = new HashMap<>();

    public ApiErrorResponse(ApiError apiError) {
        this(apiError, new HashMap<>());
    }

    public ApiErrorResponse(ApiError apiError, Map<String, String> subErrors) {
        this.message = apiError.message();
        this.code = apiError.code();
        this.subErrors = subErrors;
    }

}
