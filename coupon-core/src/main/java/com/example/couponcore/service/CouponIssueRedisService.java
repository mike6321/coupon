package com.example.couponcore.service;

import com.example.couponcore.exception.CouponIssueException;
import com.example.couponcore.exception.ErrorCode;
import com.example.couponcore.repository.redis.RedisRepository;
import com.example.couponcore.repository.redis.dto.CouponRedisEntity;
import com.example.couponcore.util.CouponRedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static java.util.Objects.*;

@RequiredArgsConstructor
@Service
public class CouponIssueRedisService {

    private final RedisRepository redisRepository;

    public boolean availableUserIssueQuantity(Long couponId, Long userId) {
        String key = CouponRedisUtils.getIssueRequestKey(couponId);
        return !redisRepository.sisMember(key, String.valueOf(userId));
    }

    public boolean availableTotalIssueQuantity(Integer totalQuantity, Long couponId) {
        if (isNull(totalQuantity)) {
            return true;
        }

        String key = CouponRedisUtils.getIssueRequestKey(couponId);
        return totalQuantity > redisRepository.sCard(key);
    }

    public void checkCouponIssueQuantity(CouponRedisEntity couponRedisEntity, Long userId) {
        if (!availableTotalIssueQuantity(couponRedisEntity.totalQuantity(), couponRedisEntity.id())) {
            throw new CouponIssueException(ErrorCode.INVALID_COUPON_ISSUE_QUANTITY, "발급 가능한 수량을 초과 합니다. couponId: %s, userId: %s".formatted(couponRedisEntity.id(), userId));
        }
        if (!availableUserIssueQuantity(couponRedisEntity.id(), userId)) {
            throw new CouponIssueException(ErrorCode.DUPLICATED_COUPON_ISSUE, "이미 발급된 쿠폰 입니다. couponId: %s, userId: %s".formatted(couponRedisEntity.id(), userId));
        }
    }

}
