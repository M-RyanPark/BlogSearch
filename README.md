# 블로그 검색 서비스

## 요구 사항

### 블로그 검색

- 키워드를 통해 블로그를 검색할 수 있어야 합니다.
- 검색 결과에서 Sorting(정확도순, 최신순) 기능을 지원해야 합니다.
- 검색 결과는 Pagination 형태로 제공해야 합니다.
- 검색 소스는 카카오 API의 키워드로 블로그 검색(https://developers.kakao.com/docs/latest/ko/daum-search/dev-guide#search-blog)을 활용합니다.
- 추후 카카오 API 이외에 새로운 검색 소스가 추가될 수 있음을 고려해야 합니다.

### 인기 검색어 목록

- 사용자들이 많이 검색한 순서대로, 최대 10개의 검색 키워드를 제공합니다.
- 검색어 별로 검색 횟수도 함께 제공해야 합니다.

## 추가 요구 사항

- 멀티 모듈 구성 및 모듈간 의존성 제약
- 트래픽이 많고, 저장되어 있는 데이터가 많음을 염두에 둔 구현
- 동시성 이슈가 발생할 수 있는 부분을 염두에 둔 구현 (예시. 키워드 별로 검색된 횟수의 정확도)
- 카카오 블로그 검색 API에 장애가 발생한 경우, 네이버 블로그 검색 API를 통해 데이터 제공
- 네이버 블로그 검색 API: https://developers.naver.com/docs/serviceapi/search/blog/blog.md

## API 명세

### 블로그 검색

키워드를 이용하여 블로그 검색

#### URI : GET /blog/search

#### Request param

- keyword : 검색어
- sortType : 정렬 유형 ACCURACY / RECENCY
- page : 페이지
- size : 페이지 사이즈

#### Response

- meta.keyword : 검색어
- meta.pageSize : 페이지 사이즈
- meta.currentPage : 현재 페이지
- meta.totalPage : 전체 페이지 수
- meta.referer : 검색 출처 KAKAO / NAVEr
- results.title : 블로그 타이틀
- results.link : 링크
- results.contents : 내용
- results.blogName : 블로그명
- results.postDate : 작성일

#### CURL

```
curl --location --request GET 'http://localhost:8080/blog/search?page=1&sortType=ACCURACY&keyword=안녕&size=10' \
--header 'Content-Type: application/json' \
--data-raw '{
    "id":"1",
    "name":"레이아웃1"
}'
```

### 상위 검색어 조회

검색 기록이 많은 상위 10개 검색어 조회

#### URK : GET /blog/search/popular-keyword

#### Response

- keyword : 검색어
- count : 횟수

#### CURL

```
curl --location --request GET 'http://localhost:8080/blog/search/popular-keyword'
```

## 구현

### 생각할 점

- 실제 사용 환경에서는 역할 별로 서버, 미들웨어 등이 나뉘지만 단일 application 내에서 구현해야 하므로 package와 서비스로 적절히 분리한다
- 검색연동에 다중 API 사용을 염두에 둬야 하므로 검색 서비스에서는 검색 연동 api의 항목을 종합하여 공통으로 사용할 수 있는 interface를 만든다
- kafka, rabbitmq등의 메시지 큐를 사용할 수 없으므로, 검색 완료 후 spring applicationEvent를 이용하여 이벤트를 생성하고 EventListner가 비동기로 검색어 횟수를 update
  하여 사용자 검색 결과를 빠르게 제공할 수 있도록 한다.
- 인기 검색어는 검색 기록 많아질 경우 부하가 줄이기 위해, 5초 단위로 스케쥴링 하여 캐시를 갱신하고 api는 캐시에서 조회하도록 한다. 블러그 검색 순위는 5초 단위로 갱신 되어도 사용자에게 문제를 야기하지
  않을 것으로 생각한다
- 검색결과가 없더라도 사용자가 검색을 한것이므로 검색결과 여부에 상관없이 검색 횟수를 업데이트 한다
- 카카오 블로그 검색 실패 시 네비어 API를 대신 사용할 기준 고민. 10초 안에 5회 이상 카카오 API 검색 실패 시 1분 동안 네이버 API를 이용하여 서비스 하는 방향을 고려
- h2 메모리 db 이고 스키마가 간단하므로 hibernate.ddl-auto를 이용하여 테이블을 생성한다

## Library

- framework : spring-boot-web, spring-boot-data-jpa, spring-boot-starter-validation
- Database : h2 (In Memory)
- http-client : spring-cloud-starter-openfeign

## download link

https://github.com/M-RyanPark/BlogSearch/blob/main/executable/kakaobank-ryan.jar
