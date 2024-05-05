FIND_TOILET
---
---

## 주변의 화장실을 찾는 위치기반 서비스

**Github :  📄 [Github Link](https://github.com/hankyu0301/findToilet)**

**API 명세서 : 📄 [API 명세서](https://www.notion.so/d0ef1bdd0bfd4aa8bae448e0eab95326?pvs=21)**

**개발 인원 : 1명**

**개발 기간 : 2024.01.03 ~2024.02.26**

---

### 빌드 방법

```docker
docker-compose build && docker-compose up -d
```

[**http://localhost:8080/docs/swagger-ui/index.html](http://localhost:8080/docs/swagger-ui/index.html) 에서 API 명세를 확인할 수 있습니다.**

---

### Tech Stack

- **Java**
- **Spring Boot, Spring Security**
- **Spring Data JPA, Spring Data Redis, QueryDSL, MySQL**
- **JWT, Docker, Swagger + RestDocs**

---

### **Features**

### 화장실 기능

- **위치 정보와 다양한 화장실 조건을 필터링하는 목록 조회 가능 합니다.**
- **화장실 목록을 조회하는 기능의 구현에  MySQL의 공간 자료형과 공간 인덱스를 사용함으로써 133ms에서29ms로 성능 향상**  🔗 ****[성능 개선기](https://velog.io/@finebears/FindToilet-%EC%84%B1%EB%8A%A5-%EA%B0%9C%EC%84%A0%EA%B8%B0)

### 회원 기능

- **Spring Security를 사용한 JWT 로그인/로그아웃을 구현했습니다.**
    - **로그아웃 시 사용자의 AccessToken을 받아 남은 유효기간만큼 Redis에 저장하여 로그아웃 된 사용자의 AccessToken 재사용을 방지했습니다.**

### 리뷰 기능

- **화장실 관련 리뷰를 남기는 기능을 구현했습니다.**

### **ETC**

- **Apache POI 라이브러리를 사용하여 공공화장실의 정보가 담겨 있는 Excel 파일에서 데이터를 추출했습니다. 데이터를 추출하는 과정에서 책임을 나누며 객체지향적 코드를 작성 했습니다.**
- **문서화 코드와 운영 코드가 합쳐진 Swagger의 단점과 asciidoc 문서의 불편함을 극복하기 위해 RestDocs + Swagger API문서화를 구현 했습니다.**
- **Kakao와 Naver에서 제공하는 Geocoding API를 사용해 화장실의 좌표를 구했습니다.**
- **docker-compose를 작성해 어디서든 도커 환경을 통한 빌드를 할 수 있습니다.**

---

### ERD

---
![findToilet_ERD](https://github.com/hankyu0301/findToilet/assets/77604789/5422d140-869c-41dc-aa5e-55b4f2f8f3bb)


---
