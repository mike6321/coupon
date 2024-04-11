package com.example.couponcore.service;

import com.example.couponcore.component.DistributeLockExecutor;
import com.example.couponcore.exception.CouponIssueException;
import com.example.couponcore.exception.ErrorCode;
import com.example.couponcore.repository.redis.RedisRepository;
import com.example.couponcore.repository.redis.dto.CouponIssueRequest;
import com.example.couponcore.repository.redis.dto.CouponRedisEntity;
import com.example.couponcore.util.CouponRedisUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AsyncCouponIssueServiceV1 {

    private final RedisRepository redisRepository;
    private final CouponIssueRedisService couponIssueRedisService;
    private final ObjectMapper objectMapper;
    private final DistributeLockExecutor distributeLockExecutor;
    private final CouponCacheService couponCacheService;

    /**
     * 1. 유저의 요청을 sorted set 적재
     * 2. 조회 결과를 선착순 조건과 비교
     * 3. 쿠폰 발급 queue 에 적재
     * */
    public void issue(Long couponId, Long userId) {
        CouponRedisEntity coupon = couponCacheService.getCouponCache(couponId);
        coupon.checkIssuableCoupon();

        distributeLockExecutor.execute("lock_%s".formatted(couponId), 3000, 3000, () -> {
            couponIssueRedisService.checkCouponIssueQuantity(coupon, userId);
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
