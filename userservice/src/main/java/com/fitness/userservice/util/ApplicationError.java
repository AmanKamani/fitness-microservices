package com.fitness.userservice.util;

import com.fitness.userservice.dto.ApiError;
import org.springframework.http.HttpStatus;

public final class ApplicationError {
    private static final int CODE = 1000;

    public static ApiError USER_NOT_FOUND = new ApiError(HttpStatus.NOT_FOUND, CODE + 1, "User not found");
    public static ApiError USER_EXISTS_WITH_EMAIL = new ApiError(HttpStatus.BAD_REQUEST, CODE + 2, "User exists with email");
    public static ApiError INVALID_REQUEST_BODY = new ApiError(HttpStatus.BAD_REQUEST, CODE + 3, "Invalid request body");
    public static ApiError UNKNOWN_ERROR = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, CODE + 4, "Unknown error");
    public static ApiError BAD_REQUEST = new ApiError(HttpStatus.BAD_REQUEST, CODE + 6, "Bad Request");
}
