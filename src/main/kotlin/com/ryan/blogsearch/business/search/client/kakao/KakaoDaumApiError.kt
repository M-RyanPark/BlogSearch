package com.ryan.blogsearch.business.search.client.kakao

/**
 * kakao daum api 에러 공통 규격
 *
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @since 2022-09-21
 */
data class KakaoDaumApiError(
    val errorType: String,
    val message: String
)


