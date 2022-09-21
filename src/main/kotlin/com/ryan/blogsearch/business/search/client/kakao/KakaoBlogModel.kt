package com.ryan.blogsearch.business.search.client.kakao

import com.ryan.blogsearch.business.search.model.SearchBlog
import java.time.LocalDateTime

/**
 *
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @since 2022-09-21
 */
class KakaoBlogModel {

    data class Request(
        val query: String?,
        val sort: String? = SortType.RECENCY.name.lowercase(),
        val page: Int? = 1,
        val size: Int? = 10,
    )

    data class Response(
        val meta: MetaDto,
        val documents: List<DocumentDto>
    )

    data class DocumentDto(
        val title: String,
        val contents: String,
        val url: String,
        val blogname: String,
        val thumbnail: String,
        val datetime: LocalDateTime,
    )

    data class MetaDto(
        val total_count: Int,
        val pageable_count: Int,
        val is_end: Boolean,
    )

    enum class SortType {
        ACCURACY, RECENCY
    }
}

val SearchBlogToKakaoBlogRequestModel: (SearchBlog.Request) -> KakaoBlogModel.Request =
    { request ->
        KakaoBlogModel.Request(
            query = request.keyword,
            page = request.page,
            size = request.size,
            sort = request.sortType.name.lowercase(),
        )
    }

val KakaoBlogToSearchBlogResponse: (SearchBlog.Request, KakaoBlogModel.Response) -> SearchBlog.Response =
    { searchRequest, kakaoResponse ->
        SearchBlog.Response(
            meta = kakaoResponse.meta.let {
                SearchBlog.MetaDto(
                    keyword = searchRequest.keyword ?: "",
                    pageSize = searchRequest.size,
                    currentPage = searchRequest.page,
                    totalPage = it.total_count.div(searchRequest.size),
                    referer = SearchBlog.Referer.KAKAO.name

                )
            },
            results = kakaoResponse.documents.map {
                SearchBlog.SearchResultDto(
                    title = it.title,
                    link = it.url,
                    contents = it.contents,
                    blogName = it.blogname,
                    postDate = it.datetime.toLocalDate()
                )
            }
        )
    }
