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
- **화장실 목록을 조회하는 기능을 두 가지 방법으로 구현했습니다.  🔗 [Link](https://velog.io/@finebears/%EC%A2%8C%ED%91%9C%EA%B0%84-%EA%B1%B0%EB%A6%AC-%EA%B5%AC%ED%95%98%EA%B8%B0)**
    - **첫 번째는 MySQL의 내장함수를 사용하는 방법입니다.
    정확한 거리값을 제공하고 데이터베이스 내장 함수를 활용하여 간결한 코드를 작성할 수 있는 장점이 있습니다. 하지만 이 방법은 데이터베이스에 종속적이며, 내장 함수의 성능에 의존하므로 데이터베이스 변경 시 호환성 문제가 발생할 수 있습니다.**
        
        ```java
        private JPQLQuery<ToiletDto> createQuery(Predicate predicate, ToiletSearchCondition cond) {
        
                return jpaQueryFactory
                        .select(constructor(ToiletDto.class,
                                toilet.id,
                                toilet.name,
                                toilet.road_address,
                                toilet.latitude,
                                toilet.longitude,
                                getDistance(toilet.longitude, toilet.latitude, cond.getUserLongitude(), cond.getUserLatitude()).as("distance"),
                                toilet.male_disabled,
                                toilet.female_disabled,
                                toilet.male_kids,
                                toilet.female_kids,
                                toilet.diaper,
                                toilet.operation_time,
                                review.score.coalesce((double) 0).avg().as("score"),
                                review.count().as("scoreCount")))
                        .from(toilet)
                        .leftJoin(toilet.reviewList, review)
                        .where(predicate)
                        .groupBy(toilet.id)
                        .orderBy(getDistance(toilet.longitude, toilet.latitude, cond.getUserLongitude(), cond.getUserLatitude()).asc());
            }
        
            //두 좌표 간의 거리를 계산하는 내장함수를 호출하는 메서드입니다. 
            private NumberTemplate<Double> getDistance(NumberPath<Double> longitude, NumberPath<Double> latitude, Double userLongitude, Double userLatitude) {
                return numberTemplate(Double.class, "ST_Distance_Sphere(POINT({0}, {1}), POINT({2}, {3}))",
                        userLongitude,
                        userLatitude,
                        longitude,
                        latitude);
            }
        ```
        
    - **두 번째는 x,y 좌표를 이용하여 두 지점 간의 대원거리(또는 최단 거리)를 구하는 수학적 공식인 SQL 하버사인 공식을 사용한 구현입니다.
    데이터베이스와 독립적으로 구현되어 DB 변경 시 코드 수정이 필요하지 않으며, 알고리즘이 간단하여 유지보수가 용이합니다. 하지만 이 방법은 지구의 곡률을 계산하지 않아 약 3%의 오차가 발생합니다.**
        
        ```java
        @Transactional(readOnly = true)
        public ToiletListDto findAllByConditionUsingJPQL(ToiletSearchCondition cond) {
                Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        
        		//생성자에 사용자의 위치정보와 검색할 범위를 전달합니다.
                UserLocationCalculator locationService = new UserLocationCalculator(new PointDto(cond.getUserLatitude(), cond.getUserLongitude()), cond.getLimit());
        
        		//사용자 위치에서 검색 범위만큼 떨어진 경도와 위도를 계산하여 사각형 모양의 지역을 나타냅니다.
                List<ToiletDto> toiletDtoList = toiletRepository.findAllByCondition(cond.getKids(), cond.getDisabled(), cond.getDiaper(),
                        locationService.getLatitudeMinus(),
                        locationService.getLatitudePlus(), locationService.getLongitudeMinus(),
                        locationService.getLongitudePlus());
        
        		//검색 범위 안에 있는 화장실을 필터링하고 정렬합니다.
                List<ToiletDto> updatedToiletDtos =
                        locationService.calculateDistanceAndRemove(toiletDtoList);
        
                return ToiletListDto.toDto(new PageImpl<>(updatedToiletDtos, pageable, updatedToiletDtos.size()));
            }
        ```
        

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
