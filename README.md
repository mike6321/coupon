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

