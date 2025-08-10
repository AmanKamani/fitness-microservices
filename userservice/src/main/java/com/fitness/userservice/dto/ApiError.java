package com.fitness.userservice.dto;

import org.springframework.http.HttpStatus;

public record ApiError(HttpStatus httpStatus, Integer code, String message) {
}
