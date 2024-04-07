# coupon
쿠폰시스템 개발을 위한 repository 입니다.

------

## Setting

```sh
docker-compose up -d
```

```sh
docker-compose up -d --scale worker=3
```

## Load Test

<img width="1520" alt="image" src="https://github.com/mike6321/TIL/assets/33277588/c81eed9c-51f0-4a2b-b938-bd193ffd2204">

<img width="1564" alt="image" src="https://github.com/mike6321/TIL/assets/33277588/dff59647-37ee-450d-8b3d-6b0196b9cb91">

## Trouble Shooting

동시성 이슈 발생

* 쿠폰 갯수는 정상적으로 소진되었지만 더많은 쿠폰을 발급함
  * 쿠폰 한정수량 : 500
  * 발급된 쿠폰 : 505

<img width="2111" alt="image" src="https://github.com/mike6321/TIL/assets/33277588/431f8d3e-dce8-4bcd-b084-15ec5ecddad2">

<img width="1431" alt="image" src="https://github.com/mike6321/TIL/assets/33277588/aa4c781c-8833-439b-9767-ecb0c816700e">

## 기능 분석

검증

* 쿠폰 존재 검증
* 쿠폰 발급 유효 기간 검증
* 쿠폰 발급 수량 검증
* 중복 발급 검증

발급

* 쿠폰 발급 수량 증가
* 쿠폰 발급 내역 기록

------

## Sorted Set을 활용한 구조

예상 시나리오

1. 유저 요청이 들어온다. (coupon_id, user_id)
2. 쿠폰 캐시를 통한 유효성 검증
   1. 쿠폰의 존재
   2. 쿠폰의 유효기간
3. Sorted Set에 요청을 추가 (ZADD score = time stamp)
   1. ZADD의 응답 값 기반 발급 검증
4. 현재 요청의 순서 조회 (ZRANK) 및 발급 성공 여부 응답
5. 발급에 성공했다면 쿠폰 발급 Queue에 적재

Problem

* score에 timestamp는 동시 요청에 대한 순서를 보장할 수 있을까?

  -> score를 관리해야할 필요가 없음

* 발행 수 가 소진 되면 요청을 막을 건데 굳이 sorted set을 사용하는게 의미가 있을까?

* 시간 복잡도도 O(logn) 이므로 Set을 사용하는게 좋을것 같다.

