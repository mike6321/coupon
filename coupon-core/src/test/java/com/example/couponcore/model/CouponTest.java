package com.example.couponcore.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CouponTest {

    @Test
    @DisplayName("발급 수량이 소진되었다면 false를 반환")
    void availableIssueQuantity_1() {
        Coupon coupon = Coupon.builder()
                .totalQuantity(100)
                .issuedQuantity(100)
                .build();

        boolean result = coupon.availableIssueQuantity();

        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("발급 수량이 소진되었다면 true를 반환")
    void availableIssueQuantity_2() {
        Coupon coupon = Coupon.builder()
                .totalQuantity(100)
                .issuedQuantity(99)
                .build();

        boolean result = coupon.availableIssueQuantity();

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("발급 수량이 설정되어 있지 않으면 true를 반환")
    void availableIssueQuantity_3() {
        Coupon coupon = Coupon.builder()
                .totalQuantity(null)
                .issuedQuantity(99)
                .build();

        boolean result = coupon.availableIssueQuantity();

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("발급기간에 해당되지않으면 false를 반환")
    void availableIssueDate_1() {
        Coupon coupon = Coupon.builder()
                .dateIssueStart(LocalDateTime.now().plusDays(1))
                .dateIssueEnd(LocalDateTime.now().plusDays(2))
                .build();

        boolean result = coupon.availableIssueDate();

        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("발급기간에 해당되면 true를 반환")
    void availableIssueDate_2() {
        Coupon coupon = Coupon.builder()
                .dateIssueStart(LocalDateTime.now().minusDays(1))
                .dateIssueEnd(LocalDateTime.now().plusDays(2))
                .build();

        boolean result = coupon.availableIssueDate();

        Assertions.assertTrue(result);
    }

}