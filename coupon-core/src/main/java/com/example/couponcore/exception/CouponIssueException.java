package com.example.couponcore.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CouponIssueException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String message;

    @Override
    public String getMessage() {
        return"[%s] %s".formatted(errorCode, message);
    }

}
