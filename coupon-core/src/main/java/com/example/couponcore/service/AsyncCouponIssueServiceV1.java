package com.example.couponcore.service;

import com.example.couponcore.repository.redis.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AsyncCouponIssueServiceV1 {

    private final RedisRepository redisRepository;

    /**
     * 1. 유저의 요청을 sorted set 적재
     * 2. 조회 결과를 선착순 조건과 비교
     * 3. 쿠폰 발급 queue 에 적재
     * */
    public void issue(Long couponId, Long userId) {
        String key = "issue.request.sorted_set_couponId=%s".formatted(couponId);
        redisRepository.zAdd(key, String.valueOf(userId), System.currentTimeMillis());
//        Coupon coupon = findCoupon(couponId);
//        coupon.issue();
//        saveCouponIssue(couponId, userId);
    }

}
