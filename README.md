# FIND_TOILET

---

회원의 위치정보나 여러가지 조건으로 화장실 목록을 필터링하여 조회하는 서비스입니다.

- 프로젝트 명칭 : FindToilet
- 개발 인원 : 1명
- 개발 기간 : 2024.01~

📄 [API 명세](https://www.notion.so/d0ef1bdd0bfd4aa8bae448e0eab95326?pvs=21)

---

### 사용 Skills

- Java, SpringBoot, Gradle
- MySQL, Redis
- Junit5, Mockito
- Swagger, Rest Docs
- Github

---

### 주요 기능

- 회원의 위치, 장애인용 화장실 여부, 유아용 화장실 여부, 기저귀 교환대 유무로 필터링이 가능합니다.
- 화장실 목록을 조회하는 동적 쿼리 작성에 QueryDSL을 사용했습니다.
- Swagger + RestDoc을 사용하여 프로덕션 코드에 추가적인 애노테이션 없이 API 명세서를 제작했습니다.
- API 명세서를 제작하는 과정에서 테스트를 거치도록 하여 신뢰성 있는 API 명세서를 제작했습니다.
- Kakao에서 제공하는 Geocoding API를 사용해 화장실의 경도, 위도를 구하였고 거리 계산을 위해 MySQL의 내장함수를 사용했습니다.
- 프로젝트에서 사용된 화장실 정보를 Excel에서 추출해 직접 Insert Query를 작성해주었습니다.
- docker-compose를 작성해 어디서든 도커 환경을 통한 빌드를 할 수 있습니다.

---

### ERD

![findToilet_ERD](https://github.com/hankyu0301/findToilet/assets/77604789/5422d140-869c-41dc-aa5e-55b4f2f8f3bb)


---
