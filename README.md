<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <img alt="npm" src="https://img.shields.io/badge/npm-%3E%3D%205.5.0-blue">
  <img alt="node" src="https://img.shields.io/badge/node-%3E%3D%209.3.0-blue">
  <img alt="license" src="https://img.shields.io/github/license/woowacourse/atdd-subway-2020">
</p>

<br>

# 레벨2 최종 미션 - 지하철 노선도 애플리케이션

## 🎯 요구사항
- [프론트엔드 미션](https://github.com/woowacourse/atdd-subway-2020/blob/master/frontend-mission.md)
- [백엔드 미션](https://github.com/woowacourse/atdd-subway-2020/blob/master/backend-mission.md)

## 🤔 미션 제출 방법
- 진행 방식은 오프라인 코딩 테스트와 동일하다.
- 저장소를 Fork하여 자신의 저장소에서 미션 구현을 완료하고, Pull Request를 통해 미션을 제출한다.
- Pull Request를 보낸 후 woowa_course@woowahan.com로 메일을 발송한다.

## 😌 레벨2 최종 미션을 임하는 자세
레벨2 과정을 스스로의 힘으로 구현했다는 것을 증명하는데 집중해라
- [ ] 기능 목록을 잘 작성한다.  
- [ ] 자신이 구현한 기능에 대해 인수 테스트를 작성한다.
- [ ] 자신이 구현한 코드에 대해 단위 테스트를 작성한다.
- [ ] TDD 사이클 이력을 볼 수 있도록 커밋 로그를 잘 작성한다.
- [ ] 사용자 친화적인 예외처리를 고민한다.
- [ ] 읽기 좋은 코드를 만든다.

## 🚀 Getting Started

### Install
#### npm 설치
```
cd frontend
npm install
```
> `frontend` 디렉토리에서 수행해야 합니다.

### Usage
#### webpack server 구동
```
npm run dev
```
#### application 구동
```
./gradlew bootRun
```
<br>

## 📝 License

This project is [MIT](https://github.com/woowacourse/atdd-subway-2020/blob/master/LICENSE.md) licensed.

# 기능 목록

-[ ] 역 추가
-[ ] 노선 추가
-[ ] 구간 관리
-[ ] 노선도 조회
-[ ] 경로 검색
    
    -[ ] 요금 계산 기능
    
        -[ ] 거리별 요금 계산 
        -[ ] 추가 요금 계산
        -[ ] 할인 정책 추가
            
            -[ ] 로그인된 사용자에 대한 할인을 계산
            
    -[ ] 최단 시간 경로 검색 기능
    -[ ] 최단 거리 경로 검색 기능
    -[ ] 가장 빠른 도착 경로 검색 기능
-[x] 인증 기능 
    
    -[ ] 회원가입
    -[ ] OAuth2 토큰을 이용하여 인증
    
# 백엔드 요구사항

- 경로 조회 응답 결과에 요금 정보 추가
-  1. 경로 조회 응답 결과에 요금 정보 추가
-  2. 가장 빠른 도착 경로 타입 추가
-      - 노선의 첫차/막차 시간은 24:00을 넘기지 못하며 첫차 시간은 막차 시간 보다 항상 이르다.
-      - 막차가 끊길 경우 다음날 첫차 기준으로 계산한다.
-      - 이동 시간과 승하차 시간은 고려하지 않는다. 1호선과 2호선이 교차하는 C역에서 1호선에서 2호선으로 환승해야 하는 경우 1호선 도착 시간이 14:30이고 2호선 출발 시간이 14:30일 경우에도 환승할 수 있음
-      
- 가장 빠른 경로 도착 경로 타입 추가
- 백엔드 요금 조회 api를 프론트엔드에서 사용할 수 있게 연동
- 인수 테스트를 작성하세요.
- 경로 조회 기능의 문서화를 진행하세요.
- 기능 구현 시 TDD를 활용하여 개발하세요. (커밋 단위를 TDD 사이클을 확인할 수 있으면 좋겠네요!)

## 제약사항

- 노선의 첫차/막차 시간은 24:00을 넘기지 못하며 첫차 시간은 막차 시간 보다 항상 이르다.
- 막차가 끊길 경우 다음날 첫차 기준으로 계산한다.
- 이동 시간과 승하차 시간은 고려하지 않는다. 1호선과 2호선이 교차하는 C역에서 1호선에서 2호선으로 환승해야 하는 경우 1호선 도착 시간이 14:30이고 2호선 출발 시간이 14:30일 경우에도 환승할 수 있음
- 노선의 첫차/막차 시간은 24:00을 넘기지 못하며 첫차 시간은 막차 시간 보다 항상 이르다.
- 막차가 끊길 경우 다음날 첫차 기준으로 계산한다.
- 이동 시간과 승하차 시간은 고려하지 않는다. 1호선과 2호선이 교차하는 C역에서 1호선에서 2호선으로 환승해야 하는 경우 1호선 도착 시간이 14:30이고 2호선 출발 시간이 14:30일 경우에도 환승할 수 있음


# 프론트 요구사항
- 템플릿 리터럴을 이용해 현재 시간을 사용자가 보기 편한 형식으로 문자열 렌더링
- validator를 구현해, form의 유효성을 검사
- 길찾기를 위해 사용자가 입력한 값을 이용해 검색결과를 불러오는 핸들러를 구현1. 백엔드 요금 조회 api를 프론트엔드에서 사용할 수 있게 연동


