package com.fitness.userservice.exception;

import com.fitness.userservice.dto.ApiError;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final ApiError apiError;

    private BaseException(ApiError apiError) {
        super(apiError.message());
        this.apiError = apiError;
    }

    public static BaseException build(ApiError apiError) {
        return new BaseException(apiError);
    }
}
