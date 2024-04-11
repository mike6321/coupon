package com.example.couponcore.service;

import com.example.couponcore.repository.redis.RedisRepository;
import com.example.couponcore.repository.redis.dto.CouponRedisEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AsyncCouponIssueServiceV2 {

    private final CouponCacheService couponCacheService;
    private final RedisRepository redisRepository;

    public void issue(Long couponId, Long userId) {
        CouponRedisEntity coupon = couponCacheService.getCouponCache(couponId);
        coupon.checkIssuableCoupon();
        issueRequest(couponId, userId, coupon.totalQuantity());
    }

    private void issueRequest(Long couponId, Long userId, Integer totalIssueQuantity) {
        if (totalIssueQuantity == null) {
            redisRepository.issueRequest(couponId, userId, Integer.MAX_VALUE);
        }
        redisRepository.issueRequest(couponId, userId, totalIssueQuantity);
    }

}
