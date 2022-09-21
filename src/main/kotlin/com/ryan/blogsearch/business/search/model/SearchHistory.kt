package com.ryan.blogsearch.business.search.model

/**
 * 검색 이력 응답
 *
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @since 2022-09-21
 */
class SearchHistory {
    data class Response(
        val keyword: String,
        val count: Int,
    )
}
