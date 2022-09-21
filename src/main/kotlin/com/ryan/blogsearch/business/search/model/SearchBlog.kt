package com.ryan.blogsearch.business.search.model

import java.time.LocalDate
import javax.validation.constraints.NotNull

/**
 *
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @since 2022-09-21
 */
class SearchBlog {
    data class Request(
        @field:NotNull
        val keyword: String? = null,
        val page: Int = 1,
        val size: Int = 10,
        val sortType: SortType = SortType.ACCURACY,
    ) {
        fun <T> toClientRequest(block: (Request) -> T): T = block(this)
    }

    data class Response(
        val meta: MetaDto,
        val results: List<SearchResultDto>,
    )

    data class MetaDto(
        val keyword: String,
        val pageSize: Int,
        val currentPage: Int,
        val totalPage: Int,
        val referer: String,
    )

    data class SearchResultDto(
        val title: String,
        val link: String,
        val contents: String,
        val blogName: String,
        val postDate: LocalDate,
    )

    enum class SortType {
        ACCURACY, RECENCY
    }

    enum class Referer {
        KAKAO, NAVER
    }
}
