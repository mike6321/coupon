package com.example.couponapi.controller;

import com.example.couponapi.controller.dto.CouponIssueRequestDto;
import com.example.couponapi.service.CouponIssueRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CouponIssueController {

    private final CouponIssueRequestService couponIssueRequestService;

    @PostMapping("/v1/issue")
    public boolean issueV1(@RequestBody CouponIssueRequestDto requestDto) {
        couponIssueRequestService.issueRequestV1(requestDto);
        return true;
    }

    @PostMapping("/v1/issue-async")
    public boolean issueV2(@RequestBody CouponIssueRequestDto requestDto) {
        couponIssueRequestService.issueRequestV2(requestDto);
        return true;
    }

    @PostMapping("/v2/issue-async")
    public boolean issueV3(@RequestBody CouponIssueRequestDto requestDto) {
        couponIssueRequestService.issueRequestV3(requestDto);
        return true;
    }


}
