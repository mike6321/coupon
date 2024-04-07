package com.example.couponcore.service;

import com.example.couponcore.component.DistributeLockExecutor;
import com.example.couponcore.exception.CouponIssueException;
import com.example.couponcore.exception.ErrorCode;
import com.example.couponcore.model.Coupon;
import com.example.couponcore.repository.redis.RedisRepository;
import com.example.couponcore.repository.redis.dto.CouponIssueRequest;
import com.example.couponcore.util.CouponRedisUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AsyncCouponIssueServiceV1 {

    private final RedisRepository redisRepository;
    private final CouponIssueService couponIssueService;
    private final CouponIssueRedisService couponIssueRedisService;
    private final ObjectMapper objectMapper;
    private final DistributeLockExecutor distributeLockExecutor;

    /**
     * 1. 유저의 요청을 sorted set 적재
     * 2. 조회 결과를 선착순 조건과 비교
     * 3. 쿠폰 발급 queue 에 적재
     * */
    public void issue(Long couponId, Long userId) {
        Coupon coupon = couponIssueService.findCoupon(couponId);
        if (!coupon.availableIssueDate()) {
            throw new CouponIssueException(ErrorCode.INVALID_COUPON_ISSUE_DATE, "발급 가능한 일자가 아닙니다. couponId: %s, userId: %s".formatted(couponId, userId));
        }

        distributeLockExecutor.execute("lock_%s".formatted(couponId), 3000, 3000, () -> {
            if (!couponIssueRedisService.availableTotalIssueQuantity(coupon.getTotalQuantity(), couponId)) {
                throw new CouponIssueException(ErrorCode.INVALID_COUPON_ISSUE_QUANTITY, "발급 가능한 수량을 초과 합니다. couponId: %s, userId: %s".formatted(couponId, userId));
            }
            if (!couponIssueRedisService.availableUserIssueQuantity(couponId, userId)) {
                throw new CouponIssueException(ErrorCode.DUPLICATED_COUPON_ISSUE, "이미 발급된 쿠폰 입니다. couponId: %s, userId: %s".formatted(couponId, userId));
            }

            issueRequest(couponId, userId);
        });
    }

    private void issueRequest(Long couponId, Long userId) {
        CouponIssueRequest couponIssueRequest = new CouponIssueRequest(couponId, userId);
        try {
            String value = objectMapper.writeValueAsString(couponIssueRequest);
            String issueRequestKey = CouponRedisUtils.getIssueRequestKey(couponId);
            redisRepository.sAdd(issueRequestKey, String.valueOf(userId));
            String issueRequestQueueKey = CouponRedisUtils.getIssueRequestQueueKey();
            redisRepository.rPush(issueRequestQueueKey, value);
        } catch (JsonProcessingException e) {
            throw new CouponIssueException(ErrorCode.FAIL_COUPON_ISSUE_REQUEST, "input: %s".formatted(couponIssueRequest));
        }

    }

}
