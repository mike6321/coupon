package com.example.couponcore.service;

import com.example.couponcore.exception.CouponIssueException;
import com.example.couponcore.exception.ErrorCode;
import com.example.couponcore.model.Coupon;
import com.example.couponcore.model.CouponIssue;
import com.example.couponcore.repository.mysql.CouponIssueJpaRepository;
import com.example.couponcore.repository.mysql.CouponIssueRepository;
import com.example.couponcore.repository.mysql.CouponJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.couponcore.model.CouponIssue.createCouponIssue;
import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class CouponIssueService {

    private final CouponJpaRepository couponJpaRepository;
    private final CouponIssueJpaRepository couponIssueJpaRepository;
    private final CouponIssueRepository couponIssueRepository;

    @Transactional
    public void issue(Long couponId, Long userId) {
        Coupon coupon = findCoupon(couponId);
        coupon.issue();
        saveCouponIssue(couponId, userId);
    }

    @Transactional
    public CouponIssue saveCouponIssue(Long couponId, Long userId) {
        checkAlreadyIssuance(couponId, userId);
        CouponIssue couponIssue = createCouponIssue(couponId, userId);
        return couponIssueJpaRepository.save(couponIssue);
    }

    public Coupon findCoupon(Long couponId) {
        return couponJpaRepository.findById(couponId)
                .orElseThrow(() -> new CouponIssueException(ErrorCode.COUPON_NOT_EXIST, "쿠폰이 존재하지 않습니다. %s".formatted(couponId)));
    }

    private void checkAlreadyIssuance(Long couponId, Long userId) {
        CouponIssue issue = couponIssueRepository.findFirstCouponIssue(couponId, userId);
        if (!isNull(issue)) {
            throw new CouponIssueException(ErrorCode.DUPLICATED_COUPON_ISSUE, "이미 발급된 쿠폰입니다. couponId: %s, userId: %s".formatted(couponId, userId));
        }
    }

}
